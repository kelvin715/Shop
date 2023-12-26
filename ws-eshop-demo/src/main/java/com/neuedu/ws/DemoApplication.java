package com.neuedu.ws;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.PropertySource;

import com.neuedu.ws.service.IUserService;
import com.neuedu.ws.service.impl.UserServiceImpl;



@SpringBootApplication
// 扫描数据访问层接口的包名。
@MapperScan("com.neuedu.ws.dao") 
@PropertySource(value = { "classpath:ws.properties" ,"classpath:zfbinfo.properties"}, ignoreResourceNotFound = true)
public class DemoApplication {
	private final static Logger logger = LoggerFactory.getLogger(DemoApplication.class);
	
	public static void main(String[] args) {
		logger.info("logback 成功了");
		
		SpringApplication.run(DemoApplication.class, args);
		
	}
}
