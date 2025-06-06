package com.company.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProductApplication {

	public static void main(String[] args) {
                System.setProperty("server.port", "8083");
		SpringApplication.run(ProductApplication.class, args);
	}

}
