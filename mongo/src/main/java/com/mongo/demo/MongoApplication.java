package com.mongo.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.mongo.demo.handler.PersonHandler;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableMongoRepositories("com.mongo.demo.repo")
@EnableSwagger2
@EnableWebMvc
public class MongoApplication implements CommandLineRunner {

	@Autowired
	private PersonHandler handler;

	public static void main(String[] args) {
		SpringApplication.run(MongoApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		handler.runApplication();
	}
}
