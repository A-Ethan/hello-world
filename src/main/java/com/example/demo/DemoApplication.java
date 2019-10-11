package com.example.demo;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.*;

import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.management.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
	static HelloworldMBean jmx_bean  = new Helloworld();
	public static void main(String[] args) {

		SpringApplication.run(DemoApplication.class, args);
		List< MBeanServer > servers = MBeanServerFactory.findMBeanServer(null);
		System.out.println("Found MBean Servers : "+ servers);



		for (MBeanServer server:servers){
			try {
				server.registerMBean( jmx_bean , new ObjectName("hello_bean:name=helloworld" ));
			} catch (InstanceAlreadyExistsException e) {
				e.printStackTrace();
			} catch (MBeanRegistrationException e) {
				e.printStackTrace();
			} catch (NotCompliantMBeanException e) {
				e.printStackTrace();
			} catch (MalformedObjectNameException e) {
				e.printStackTrace();
			}
		}

		Timer t = new Timer();

		t.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				jmx_bean.setAppCounter( jmx_bean.getAppCounter()+1);
				jmx_bean.setRequestCounter( jmx_bean.getRequestCounter()+10);
				jmx_bean.setSuccessCounter( jmx_bean.getAppCounter() + jmx_bean.getRequestCounter());
			}
		},1000, 2000);
	}
}