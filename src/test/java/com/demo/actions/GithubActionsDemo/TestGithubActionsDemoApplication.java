package com.demo.actions.GithubActionsDemo;

import org.springframework.boot.SpringApplication;

public class TestGithubActionsDemoApplication {

	public static void main(String[] args) {
		SpringApplication.from(GithubActionsDemoApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
