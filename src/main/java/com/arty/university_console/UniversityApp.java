package com.arty.university_console;

import com.arty.university_console.console.ConsoleUI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class UniversityApp {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(UniversityApp.class, args);
		ConsoleUI consoleUI = context.getBean(ConsoleUI.class);
		consoleUI.run();
	}

}
