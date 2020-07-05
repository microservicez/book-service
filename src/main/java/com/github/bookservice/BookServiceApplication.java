package com.github.bookservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@SpringBootApplication
@EnableSwagger2
public class BookServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(BookServiceApplication.class, args);
	}

	@Bean
	public Docket apiDocumentation() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.github.bookservice"))
				.build()
				.apiInfo(getAPIInfo());
	}

	private ApiInfo getAPIInfo() {
		return new ApiInfo("Book Service API Doc",
				"Rest end points to contact Book service",
				"1.0",
				"",
				null,
				"",
				"",
				Collections.emptyList());
	}
}
