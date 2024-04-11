package com.lzhch.practice.springai;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@SpringBootTest
class SpringAiApplicationTests {

    @Resource
    private OllamaChatClient ollamaChatClient;

    @Test
    void contextLoads() {

        /*
         * 这是从 Java 15 开始引入的文本块（Text Blocks）的写法，也被称为多行字符串（Multiline Strings）。
         * 文本块允许你在三个双引号 """ 之间插入多行字符串，这在处理多行文本时非常有用。
         * 如果你的字符串包含多行，那么使用文本块会更加方便，因为你不需要在每一行的结束处添加 \n 来表示换行。
         */
        // 1. 普通提问
        // String message = """
        //         西红柿炒钢丝球这道菜怎么做?
        //         """;
        //
        // System.out.println(ollamaChatClient.call(message));

        //  2. prompt 提问
        // PromptTemplate promptTemplate = new PromptTemplate("""
        //         你是一个五星级米其林大厨，你擅长于做各种菜品，
        //         根据：{message} 场景写出这道菜的做法
        //         """);
        // String message = """
        //         西红柿炒钢丝球这道菜怎么做?
        //         """;
        // Prompt prompt = promptTemplate.create(Map.of("message", message));
        // System.out.println(ollamaChatClient.call(prompt));

        // 3. prompt 提问流式输出
        // 构建一个异步函数，实现手动关闭测试函数
        // CompletableFuture<Void> future = new CompletableFuture<>();
        //
        // PromptTemplate promptTemplate = new PromptTemplate("""
        //         你是一个五星级米其林大厨，你擅长于做各种菜品，
        //         根据：{message} 场景写出这道菜的做法
        //         """);
        // String message = """
        //         西红柿炒钢丝球这道菜怎么做?
        //         """;
        // Prompt prompt = promptTemplate.create(Map.of("message", message));
        //
        // ollamaChatClient.stream(prompt).subscribe(
        //         chatResponse -> System.out.println("response: " + chatResponse),
        //         throwable -> System.err.println("err: " + throwable.getMessage()),
        //         () -> {
        //             System.out.println("complete~!");
        //             // 关闭函数
        //             future.complete(null);
        //         }
        // );
        // try {
        //     future.get();
        // } catch (InterruptedException | ExecutionException e) {
        //     throw new RuntimeException(e);
        // }

        // 4. 普通提问流式输出
        CompletableFuture<Void> future = new CompletableFuture<>();
        String message = """
                西红柿炒钢丝球这道菜怎么做?
                """;
        ollamaChatClient.stream(message).subscribe(
                chatResponse -> System.out.println("response: " + chatResponse),
                throwable -> System.err.println("err: " + throwable.getMessage()),
                () -> {
                    System.out.println("complete~!");
                    // 关闭函数
                    future.complete(null);
                }
        );
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

}
