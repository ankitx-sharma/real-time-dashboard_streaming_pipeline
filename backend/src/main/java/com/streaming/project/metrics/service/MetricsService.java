package com.streaming.project.metrics.service;

import java.time.Clock;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.LongAdder;

import org.springframework.stereotype.Service;

import com.streaming.project.metrics.dto.MetricsSnapshot;

@Service
public class MetricsService {
	private final LongAdder totalEvents = new LongAdder();
	private final LongAdder successEvents = new LongAdder();
	private final LongAdder errorEvents = new LongAdder();
	private final Clock clock;
	
	public MetricsService(Clock clock) {
		this.clock = clock;
	}
	
	public MetricsSnapshot snapshot() {
		totalEvents.add(random(5, 15));
		successEvents.add(random(4, 14));
		
		if(randomInt(0,9) == 0) errorEvents.increment();
			
		return new MetricsSnapshot(
				randomDouble(80, 150),  	 // events / sec
				totalEvents.sum(), 
				randomDouble(5, 25), // latency ms
				randomInt(0, 100),		 // queue size 
				successEvents.sum(), 
				errorEvents.sum(), 
				Instant.now(clock)
		);
	}
	
	private long random(int min, int max) {
		return ThreadLocalRandom.current().nextLong(min, max+1);
	}
	
	private int randomInt(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max+1);
	}
	
	private double randomDouble(double min, double max) {
		return ThreadLocalRandom.current().nextDouble(min, max);
	}
}
