package com.orderGestion.orderGestion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.orderGestion.orderGestion.infrastructure.out.persistence.MONGO")
@EnableJpaRepositories(basePackages = "com.orderGestion.orderGestion.infrastructure.out.persistence.JPA")
public class OrderGestionApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderGestionApplication.class, args);
	}

}
