package edu.bethlehem.scinexus;

import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ScinexusApplicationTests {

	@Autowired
	private JwtService jwtService;

	@Test
	void contextLoads() {
		assertNotNull(jwtService);
	}

	@TestConfiguration
	static class TestConfig {

		@Bean
		public DataSource dataSource() {
			// Define the data source configuration for the MySQL database
			DriverManagerDataSource dataSource = new DriverManagerDataSource();
			dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
			dataSource.setUrl("jdbc:mysql://localhost:3306/scinexusdatabase");
			dataSource.setUsername("root");
			dataSource.setPassword("1234");
			return dataSource;
		}
	}
}
