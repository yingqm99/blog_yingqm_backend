package com.yingqm.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.yingqm.blog.db.mappers")
@ComponentScan(basePackages = "com.yingqm")
public class BlogApplication {

	public static void main(String[] args) {
		System.setProperty("aws.accessKeyId", "AKIAXF7UUV4KCNAZY4MK");
		System.setProperty("aws.secretKey", "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");
		SpringApplication.run(BlogApplication.class, args);
	}

}
