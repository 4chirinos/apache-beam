package com.dataflow.example;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class Application {

	public static void main(String[] args) {
		new SpringApplicationBuilder(Application.class).profiles(args[0]).run(args);
	}
}
