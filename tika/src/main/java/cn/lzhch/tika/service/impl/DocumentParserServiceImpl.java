package cn.lzhch.tika.service.impl;


import cn.lzhch.tika.dto.DocumentInfo;
import cn.lzhch.tika.service.IDocumentParserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.langdetect.optimaize.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageDetector;
import org.apache.tika.language.detect.LanguageResult;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.ToXMLContentHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 文档解析服务实现
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2024/12/4 11:29
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentParserServiceImpl implements IDocumentParserService {

    // 定义允许的文件类型
    public static final Set<String> ALLOWED_TYPES = Set.of(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "text/plain",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    );

    private final Tika tika;

    private final Parser parser;

    /**
     * 解析文件
     *
     * @param file 文件
     * @return 文件信息
     * @throws TikaException Tika异常
     * @throws IOException   IO异常
     */
    @Override
    public DocumentInfo parseDocument(MultipartFile file) throws TikaException, IOException {
        String fileName = file.getOriginalFilename();
        String mimeType = detectMimeType(file);

        // 验证文件类型
        validateFileType(mimeType);

        try (InputStream input = file.getInputStream()) {
            String content = extractText(input);
            Map<String, String> metadata = extractMetadata(new ByteArrayInputStream(file.getBytes()));
            String language = detectLanguage(content);

            return DocumentInfo.builder()
                    .fileName(fileName)
                    .mimeType(mimeType)
                    .content(content)
                    .metadata(metadata)
                    .fileSize(file.getSize())
                    .language(language)
                    .parseTime(LocalDateTime.now())
                    .build();
        }
    }

    /**
     * 验证文件类型
     *
     * @param mimeType 文件类型
     */
    private void validateFileType(String mimeType) {
        if (!ALLOWED_TYPES.contains(mimeType)) {
            throw new IllegalArgumentException("Unsupported file type: " + mimeType);
        }
    }

    /**
     * 提取文本
     *
     * @param inputStream 输入流
     * @return 文本
     * @throws TikaException Tika异常
     */
    @Override
    public String extractText(InputStream inputStream) throws TikaException {
        try {
            return tika.parseToString(inputStream);
        } catch (Exception e) {
            log.error("Error extracting text from document", e);
            throw new TikaException("Failed to extract text", e);
        }
    }

    /**
     * 提取元数据
     *
     * @param inputStream 输入流
     * @return 元数据
     * @throws TikaException Tika异常
     * @throws IOException   IO异常
     */
    @Override
    public Map<String, String> extractMetadata(InputStream inputStream) throws TikaException, IOException {
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();
        try {
            // 限制元数据长度, 防止OOM (默认: 100k characters)
            ContentHandler handler = new BodyContentHandler(10 * 1000);
            parser.parse(inputStream, handler, metadata, context);

            Map<String, String> metadataMap = new HashMap<>();
            Arrays.stream(metadata.names())
                    .forEach(name -> metadataMap.put(name, metadata.get(name)));

            return metadataMap;
        } catch (SAXException e) {
            throw new TikaException("Failed to extract metadata", e);
        }
    }

    /**
     * 检测语言
     *
     * @param content 内容
     * @return 语言
     */
    @Override
    public String detectLanguage(String content) {
        // 加载语言检测器
        LanguageDetector detector = new OptimaizeLangDetector().loadModels();
        LanguageResult result = detector.detect(content);

        // 输出检测结果
        log.info("Detected Language: {}; Confidence: {}", result.getLanguage(), result.getRawScore());
        return result.getLanguage();
    }

    /**
     * 检测文件类型
     *
     * @param file 文件
     * @return 文件类型
     * @throws IOException IO异常
     */
    @Override
    public String detectMimeType(MultipartFile file) throws IOException {
        try (InputStream input = file.getInputStream()) {
            return tika.detect(input);
        }
    }

    /**
     * 将文档转换为HTML格式
     *
     * @param file 文件
     * @return HTML格式
     * @throws Exception 异常
     */
    public String convertToHtml(MultipartFile file) throws Exception {
        ToXMLContentHandler handler = new ToXMLContentHandler();
        ParseContext context = new ParseContext();
        Metadata metadata = new Metadata();

        try (InputStream input = file.getInputStream()) {
            AutoDetectParser parser = new AutoDetectParser();
            parser.parse(input, handler, metadata, context);

            return handler.toString();
        }
    }

}
