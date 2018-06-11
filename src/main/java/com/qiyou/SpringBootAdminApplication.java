package com.qiyou;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringBootAdminApplication implements CommandLineRunner {

	public static void main(String[] args) {
	    System.out.println("buile auto test");
		SpringApplication.run(SpringBootAdminApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {}

}
