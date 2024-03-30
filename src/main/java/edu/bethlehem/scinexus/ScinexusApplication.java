package edu.bethlehem.scinexus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//@ConfigurationPropertiesScan
//@EnableConfigurationProperties(RsaKeyConfigProperties.class)
@EnableTransactionManagement
public class ScinexusApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScinexusApplication.class, args);
	}

}
