package com.electronicssales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@ComponentScan(basePackages = {"com.electronicssales"} )
public class ElectronicsSalesApplication {

	@Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		SpringApplication.run(ElectronicsSalesApplication.class, args);
	}

}
