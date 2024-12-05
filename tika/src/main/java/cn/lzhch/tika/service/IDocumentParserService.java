package cn.lzhch.tika.service;


import cn.lzhch.tika.dto.DocumentInfo;
import org.apache.tika.exception.TikaException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 文档解析服务
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2024/12/4 11:19
 */

public interface IDocumentParserService {

    /**
     * 解析文件
     *
     * @param file 文件
     * @return 文件信息
     * @throws TikaException Tika异常
     * @throws IOException   IO异常
     */
    DocumentInfo parseDocument(MultipartFile file) throws TikaException, IOException;

    /**
     * 提取文本
     *
     * @param inputStream 输入流
     * @return 文本
     * @throws TikaException Tika异常
     */
    String extractText(InputStream inputStream) throws TikaException;

    /**
     * 提取元数据
     *
     * @param inputStream 输入流
     * @return 元数据
     * @throws TikaException Tika异常
     * @throws IOException   IO异常
     */
    Map<String, String> extractMetadata(InputStream inputStream) throws TikaException, IOException;

    /**
     * 检测语言
     *
     * @param content 内容
     * @return 语言
     */
    String detectLanguage(String content);

    /**
     * 检测文件类型
     *
     * @param file 文件
     * @return 文件类型
     * @throws IOException IO异常
     */
    String detectMimeType(MultipartFile file) throws IOException;

    /**
     * 将文档转换为HTML格式
     *
     * @param file 文件
     * @return HTML格式
     * @throws Exception 异常
     */
    String convertToHtml(MultipartFile file) throws Exception;

}
