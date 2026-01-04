package com.streaming.project.stream.config;

import java.util.concurrent.atomic.AtomicInteger;

public class LoadConfig {
	
	private final AtomicInteger eventsPerSecond = new AtomicInteger(50);
	private final AtomicInteger errorPercent = new AtomicInteger(5); // 0..100
	
	public int getEventsPerSecond() {
		return Math.max(0, eventsPerSecond.get());
	}
	
	public int getErrorPercent() {
		int perc = errorPercent.get();
		if(perc < 0) return 0;
		if(perc > 100) return 100;
		return perc;
	}
	
	public void setEventsPerSecond(int eps) {
		eventsPerSecond.set(Math.max(0, eps));
	}
	
	public void setErrorPercent(int perc) {
		errorPercent.set(Math.max(0, Math.min(perc, 100)));
	}
}
