package com.streaming.project.api.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.streaming.project.stream.service.LoadGeneratorService;

@RestController
@RequestMapping("/api/load")
public class LoadController {
	
	private final LoadGeneratorService generatorService;
	
	public LoadController(LoadGeneratorService generatorService) {
		this.generatorService = generatorService;
	}
	
	@PostMapping("/start")
	public Map<String, Object> start() {
		generatorService.start();
		return status();
	}
	
	@PostMapping("/stop")
	public Map<String, Object> stop() {
		generatorService.stop();
		return status();
	}
	
	@PostMapping("/config")
	public Map<String, Object> config(
			@RequestParam int eventsPerSecond, 
			@RequestParam int errorPercent) {
		generatorService.update(eventsPerSecond, errorPercent);
		return status();
	}
	
	@GetMapping("/status")
	public Map<String, Object> status() {
		return Map.of(
				"running", generatorService.isRunning(), 
				"eventsPerSecond", generatorService.getConfig().getEventsPerSecond(), 
				"errorPercent", generatorService.getConfig().getErrorPercent()
		);
	}
}
