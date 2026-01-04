package com.streaming.project.stream.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Service;

import com.streaming.project.stream.config.LoadConfig;
import com.streaming.project.stream.entity.Event;

@Service
public class LoadGeneratorService {
	private final InMemoryPipeline pipeline;
	private final LoadConfig config;
	
	private ScheduledExecutorService scheduler = 
			Executors.newSingleThreadScheduledExecutor(r -> {
				Thread thread = new Thread(r, "load-generator");
				thread.setDaemon(true);
				return thread;
	});
	
	private final AtomicBoolean running = new AtomicBoolean(false);
	private volatile ScheduledFuture<?> task;
	
	public LoadGeneratorService(InMemoryPipeline pipeline, LoadConfig config) {
		this.pipeline = pipeline;
		this.config = config;
	}
	
	public boolean isRunning() {
		return this.running.get();
	}
	
	public LoadConfig getConfig() {
		return this.config;
	}
	
	public synchronized void start() {
		if(running.get()) return;
		
		running.set(true);
		
		// run every 1 second: publish N events based on eventsPerSecond
		task = scheduler.scheduleAtFixedRate(() -> {
			if(!running.get()) return;
			
			int eps = config.getEventsPerSecond();
			int errorPercent = config.getErrorPercent();
			
			for(int i=0; i<eps; i++) {
				boolean shouldFail = ThreadLocalRandom.current().nextInt(0, 100) < errorPercent;
				Event e = Event.create("demo-payload", shouldFail);
				
				pipeline.publish(e);
			}
			
		}, 0, 1, TimeUnit.SECONDS);
	}
	
	public synchronized void stop() {
		running.set(false);
		if(task != null) {
			task.cancel(false);
			task = null;
		}
	}
	
	public void update(int eventsPerSecond, int errorPercent) {
		config.setErrorPercent(errorPercent);
		config.setEventsPerSecond(eventsPerSecond);
	}
}
