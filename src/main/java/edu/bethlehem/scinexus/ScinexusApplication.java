package edu.bethlehem.scinexus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class ScinexusApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScinexusApplication.class, args);
	}

}
