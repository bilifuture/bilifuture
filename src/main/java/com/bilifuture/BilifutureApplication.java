package com.bilifuture;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.bilifuture.dao")
@ServletComponentScan
//@ImportResource("classpath:dubbo-*.xml")
public class BilifutureApplication {

	public static void main(String[] args) {
		SpringApplication.run(BilifutureApplication.class, args);
	}

}
