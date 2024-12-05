package cn.lzhch.tika.config;


import org.apache.tika.Tika;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.DefaultParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.ParserDecorator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

/**
 * Tika 配置类
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2024/12/3 17:42
 */

@Configuration
public class TikaConfig {

    @Bean
    public Tika tika() {
        return new Tika();
    }

    /**
     * 自定义 Parser
     *
     * @return Parser
     */
    @Bean
    public Parser parser() {
        DefaultParser parser = new DefaultParser();
        Set<MediaType> excludeTypes = new HashSet<>();
        // 排除某些媒体类型的解析
        excludeTypes.add(MediaType.application("x-tika-ooxml"));

        return new ParserDecorator(parser) {
            @Override
            public Set<MediaType> getSupportedTypes(ParseContext context) {
                Set<MediaType> types = super.getSupportedTypes(context);
                types.removeAll(excludeTypes);
                return types;
            }
        };
    }

}
