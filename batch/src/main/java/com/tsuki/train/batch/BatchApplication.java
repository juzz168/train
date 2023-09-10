package com.tsuki.train.batch;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@SpringBootApplication
@MapperScan("com.tsuki.train.*.mapper")
@ComponentScan("com.tsuki")
@Slf4j
public class BatchApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BatchApplication.class);
        Environment env = app.run(args).getEnvironment();
        log.info("启动成功！！");
        log.info("地址：\thttp://127.0.0.1:{}{}/hello", env.getProperty("server.port"), env.getProperty("server.servlet.context-path"));
    }
}
