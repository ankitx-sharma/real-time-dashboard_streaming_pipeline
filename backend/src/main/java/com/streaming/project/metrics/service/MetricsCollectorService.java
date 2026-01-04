package com.streaming.project.metrics.service;

import java.time.Clock;
import java.time.Instant;
import java.util.concurrent.atomic.LongAdder;

import org.springframework.stereotype.Service;

import com.streaming.project.metrics.dto.MetricsSnapshot;

@Service
public class MetricsCollectorService {
	private final LongAdder totalEvents = new LongAdder();
	private final LongAdder successEvents = new LongAdder();
	private final LongAdder errorEvents = new LongAdder();
	
	private final LongAdder latencySumMs = new LongAdder();
	private final LongAdder latencySamples = new LongAdder();
	
	private volatile int queueSize = 0;
	
	private final SlidingWindowRateCounter rateCounter;
	private final Clock clock;
	
	public MetricsCollectorService(Clock clock) {
		this.clock = clock;
		this.rateCounter = new SlidingWindowRateCounter(10, clock);
	}
	
	public void recordSuccess(long latencyMs) {
		totalEvents.increment();
		successEvents.increment();
		rateCounter.increment();
		latencySumMs.add(latencyMs);
		latencySamples.increment();
	}
	
	public void recordError(long latencyMs) {
		totalEvents.increment();
		errorEvents.increment();
		rateCounter.increment();
		latencySumMs.add(latencyMs);
		latencySamples.increment();
	}
	
	public void setQueueSize(int queueSize) {
		this.queueSize = Math.max(queueSize, 0);
	}
	
	public MetricsSnapshot snapshot() {
		long samples = latencySamples.sum();
		double avgLatencyMs = samples == 0 ? 0.0 : latencySumMs.sum() / (double) samples;
		
		return new MetricsSnapshot(
					rateCounter.ratePerSecond(), 
					totalEvents.sum(), 
					avgLatencyMs, 
					queueSize, 
					successEvents.sum(), 
					errorEvents.sum(), 
					Instant.now(clock)
		);
	}
}
