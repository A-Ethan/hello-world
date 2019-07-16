package com.example.demo;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class DemoApplication {

	@GetMapping("/")
	String home() {
		return "Gary's Hello World Spring is here! 0716 2100";
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}