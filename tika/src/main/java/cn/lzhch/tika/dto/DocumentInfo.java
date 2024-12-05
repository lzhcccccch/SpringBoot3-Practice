package cn.lzhch.tika.dto;


import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 文档信息
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2024/12/4 11:21
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentInfo {

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件类型
     */
    private String mimeType;

    /**
     * 文件内容
     */
    private String content;

    /**
     * 元数据
     */
    private Map<String, String> metadata;

    /**
     * 文件大小
     */
    private long fileSize;

    /**
     * 语言
     */
    private String language;

    /**
     * 解析时间
     */
    private LocalDateTime parseTime;

}
