package com.lq.bite;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.lq.bite.dao") 
//定时器注解
@EnableScheduling
@EnableConfigurationProperties
public class BiteApplication {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(BiteApplication.class, args);
	}
}
