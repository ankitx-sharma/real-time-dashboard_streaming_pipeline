package com.streaming.project.metrics.dto;

import java.time.Instant;

public record MetricsSnapshot(
		double eventsPerSecond,
		long totalEvents,
		double avgLatencyMs,
		int queueSize,
		long successCount,
		long errorCount,
		Instant timestamp
) {}