package com.hotel.diamante;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.hotel.diamante.entity")
public class DiamanteApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiamanteApplication.class, args);
	}

}
