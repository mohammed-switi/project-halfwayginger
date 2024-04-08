package edu.bethlehem.scinexus;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ScinexusApplicationTests {

	@Autowired
	private JwtService jwtService;

	@Test
	void contextLoads() {

		assertNotNull(jwtService);
	}

}
