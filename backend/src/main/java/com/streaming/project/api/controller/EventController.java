package com.streaming.project.api.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.streaming.project.stream.entity.Event;
import com.streaming.project.stream.service.InMemoryPipeline;

@RestController
@RequestMapping("/event/trigger")
public class EventController {
	private final InMemoryPipeline pipeline;
	
	public EventController(InMemoryPipeline pipeline) {
		this.pipeline = pipeline;
	}
	
	public record PublishRequest(String payload, boolean shouldFail) {}
	
	@PostMapping
	public Map<String, Object> publish (@RequestBody PublishRequest req) {
		pipeline.publish(Event.create(
				req.payload() == null ? "payload" : req.payload(), 
				req.shouldFail()
		));
		return Map.of("status", "accepted");
	}
}
