package cn.lzhch.tika;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TikaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TikaApplication.class, args);
        System.out.println("================= TikaApplication 启动成功 =====================");
    }

}
