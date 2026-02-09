package me.vladislav.library_service_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LibraryServiceBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryServiceBackendApplication.class, args);
	}

}
