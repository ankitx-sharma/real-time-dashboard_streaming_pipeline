package com.streaming.project.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.streaming.project.metrics.dto.MetricsSnapshot;
import com.streaming.project.metrics.service.MetricsCollectorService;

@RestController
@RequestMapping("/fetch")
public class MetricsController {
	
	@Autowired
	private MetricsCollectorService service;
	
	@GetMapping("/metrics")
	public MetricsSnapshot getMetrics() {
		return service.snapshot();
	}
}
