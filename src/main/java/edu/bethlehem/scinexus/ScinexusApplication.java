package edu.bethlehem.scinexus;

import edu.bethlehem.scinexus.MongoRepository.UserMongoRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//@ConfigurationPropertiesScan
//@EnableConfigurationProperties(RsaKeyConfigProperties.class)
@EnableTransactionManagement
@EnableMongoRepositories(basePackageClasses = MongoRepository.class)

public class ScinexusApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScinexusApplication.class, args);
	}

}
