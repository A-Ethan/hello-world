package com.example.demo;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.*;

import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@RestController
@Component
public class DemoApplication {
	int serverPort = 0;

	public void onApplicationEvent(WebServerInitializedEvent event) {
		this.serverPort = event.getWebServer().getPort();
	}

	public int getPort() {
		return this.serverPort;
	}

	@GetMapping("/hello")
	String hello(HttpServletRequest request, HttpServletResponse response) {
		String address = "";
		try {
			address =  InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return "<PRE>Hello World DevOps Demo\n\r Version 0928 0745 Server works at " + address + ":" +serverPort + "\n\r  RemoteAddress: " + request.getRemoteAddr()+ "</PRE>\n\r";

	}
	@GetMapping("/")
	String home(HttpServletRequest request, HttpServletResponse response) {
		String address = "";
		try {
			address =  InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return "Gary's Hello World Spring is here! 0716 2100 Server works at " + address + ":" +serverPort + "\n  " + request.getRemoteAddr();

	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}