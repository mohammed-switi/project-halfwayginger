package edu.bethlehem.scinexus.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "edu.bethlehem.scinexus.MongoRepository")
public class MongoConfiguration {
}