package com.streaming.project.stream.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadConfigBean {

	@Bean
	public LoadConfig loadConfig() {
		return new LoadConfig();
	}
}
