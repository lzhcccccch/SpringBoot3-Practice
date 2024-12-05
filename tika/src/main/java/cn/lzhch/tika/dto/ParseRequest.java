package cn.lzhch.tika.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * 解析请求
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2024/12/4 11:17
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParseRequest {

    /**
     * 文件
     */
    private MultipartFile file;

    /**
     * 是否提取元数据
     */
    private boolean extractMetadata;

    /**
     * 是否检测语言
     */
    private boolean detectLanguage;

    /**
     * 是否提取文本
     */
    private boolean extractText;

}
