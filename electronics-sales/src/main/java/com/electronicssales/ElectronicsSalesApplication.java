package com.electronicssales;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.electronicssales"} )
public class ElectronicsSalesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElectronicsSalesApplication.class, args);
	}

}
