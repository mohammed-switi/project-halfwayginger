package edu.bethlehem.scinexus.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("edu.bethlehem.scinexus.JPARepository")
public class JPAConfiguration {
}