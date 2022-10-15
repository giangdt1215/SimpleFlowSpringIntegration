package com.dtg.SimpleFlowSpringIntegration;

import com.dtg.SimpleFlowSpringIntegration.integration.FileWriterGateway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SimpleFlowSpringIntegrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleFlowSpringIntegrationApplication.class, args);
	}

	@Bean
	public CommandLineRunner writeData(FileWriterGateway gateway){
		return args -> {
			String myName = "Dang Truong Giang";
			gateway.writeToFile("giangdt.txt", myName);
		};
	}
}
