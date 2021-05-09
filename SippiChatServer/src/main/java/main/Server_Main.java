package main;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import database.DBConnection;

@SpringBootApplication
@Configuration
public class Server_Main {

	private static final int DEFAULT_PORT = 13334;
	
	public static void main(String[] args) {
		
		SpringApplication app = new SpringApplication(Server_Main.class);
		
		app.setDefaultProperties(Collections.singletonMap("server.port", DEFAULT_PORT));
		app.run(args);
		
	}

}
