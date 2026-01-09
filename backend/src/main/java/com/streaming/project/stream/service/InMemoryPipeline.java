package com.streaming.project.stream.service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import com.streaming.project.metrics.service.MetricsCollectorService;
import com.streaming.project.stream.entity.Event;

import jakarta.annotation.PostConstruct;

@Component
public class InMemoryPipeline {
	
	private final BlockingQueue<Event> queue = new LinkedBlockingQueue<Event>(10_000);
	private final ExecutorService consumerPool = Executors.newFixedThreadPool(2);
	
	private final MetricsCollectorService metrics;

	public InMemoryPipeline(MetricsCollectorService metrics) {
		this.metrics = metrics;
	}
	
	public void publish(Event event) {
		queue.offer(event);
		metrics.setQueueSize(queue.size());
	}
	
	@PostConstruct
	public void startConsumers() {
		for(int i=0; i<2; i++) {
			consumerPool.submit(() -> {
				while(!Thread.currentThread().isInterrupted()) {
					try {
						Event e = queue.take();
						long start = System.nanoTime();
						
						// random thread sleep time in order to simulate real world processing time
						Thread.sleep(ThreadLocalRandom.current().nextInt(1, 500));
						
						// simulate occasional error
						boolean isError = e.shouldFail();
						
						long latencyMs =  (System.nanoTime() - start) / 1_000_000;
						
						if(isError) {
							metrics.recordError(latencyMs);
						}else {
							metrics.recordSuccess(latencyMs);
						}
					} catch (InterruptedException ex) {
						Thread.currentThread().interrupt();
					} catch (Exception ex) {
						// treat as error event
						metrics.recordError(0);
					}
				}
			});
		}
	}
}
