package com.electronicssales;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.electronicssales.models.ProductNameAndIdOnly;
import com.electronicssales.repositories.ProductRepository;
import com.electronicssales.repositories.impl.DefaultMyCustomizeRepositoryImpl;
import com.electronicssales.services.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@ComponentScan(basePackages = {"com.electronicssales"} )
@EnableJpaRepositories(
	repositoryBaseClass = DefaultMyCustomizeRepositoryImpl.class
)
public class ElectronicsSalesApplication {

	@Lazy
	@Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
	}

	@Lazy
	@Bean
	public ObjectMapper getObjectMapper() {
		return new ObjectMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(ElectronicsSalesApplication.class, args);
	}

	@Autowired
	private CategoryService categoryService;

	@Bean
    public void test() {
		categoryService.fetchCategoriesGroupProducts().forEach(System.out::println);
    }

}
