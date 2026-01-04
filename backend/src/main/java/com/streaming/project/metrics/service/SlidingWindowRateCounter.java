package com.streaming.project.metrics.service;

import java.time.Clock;
import java.util.concurrent.atomic.AtomicLongArray;

public final class SlidingWindowRateCounter {
	
	private final int windowSeconds;
	private final AtomicLongArray counts;
	private final AtomicLongArray seconds;
	private final Clock clock;
	
	public SlidingWindowRateCounter(int windowSeconds, Clock clock) {
		if(windowSeconds <= 0) throw new IllegalArgumentException("WindowSeconds must be greater than 0");
		this.windowSeconds = windowSeconds;
		this.counts = new AtomicLongArray(windowSeconds);
		this.seconds = new AtomicLongArray(windowSeconds);
		this.clock = clock;
	}
	
	public void increment() {
		long nowSec = clock.instant().getEpochSecond();
		int idx = (int) (nowSec % windowSeconds);
		
		long bucketSec = seconds.get(idx);
		if(bucketSec != nowSec) {
			// try to claim this bucket for current second
			seconds.set(idx, nowSec);
			counts.set(idx, 0);
		}
		counts.incrementAndGet(idx);
	}
	
	public double ratePerSecond() {
		long nowSec = clock.instant().getEpochSecond();
		long sum = 0;
		
		for(int i=0; i<windowSeconds; i++) {
			long sec = seconds.get(i);
			if(nowSec - sec < windowSeconds) {
				sum += counts.get(i);
			}
		}
		
		return sum / (double) windowSeconds;
	}
}
