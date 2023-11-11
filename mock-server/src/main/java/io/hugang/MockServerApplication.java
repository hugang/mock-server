package io.hugang;

import org.smartboot.http.server.HttpBootstrap;
import org.smartboot.http.server.handler.HttpStaticResourceHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cn.hutool.core.io.FileUtil;

@SpringBootApplication
public class MockServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MockServerApplication.class, args);

        runFileServer();
    }

    public static void runFileServer() {
        String rootPath = FileUtil.file("static").getAbsolutePath();
        HttpBootstrap bootstrap = new HttpBootstrap();
        //配置HTTP消息处理管道
        bootstrap.httpHandler(new HttpStaticResourceHandler(rootPath));
        bootstrap.setPort(8088);
        bootstrap.configuration().bannerEnabled(false);
        //设定服务器配置并启动
        bootstrap.start();
    }
}
