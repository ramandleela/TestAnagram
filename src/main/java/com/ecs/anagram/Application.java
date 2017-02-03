package com.ecs.anagram;

// Spring
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


// This Service
import com.ecs.anagram.component.AnagramComponent;

// @EnableDiscoveryClient - to be used with Config Server
@SpringBootApplication

public class Application implements CommandLineRunner{
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	
	@Autowired
	private AnagramComponent anagramComponent;
		
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		logger.info("Application.run() ...Start");
		anagramComponent.initialize();	
		logger.info("Application.run() ...End");		
	}	
}