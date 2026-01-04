package com.streaming.project.stream.entity;

import java.time.Instant;
import java.util.UUID;

public record Event(
		String id,
		Instant createdAt,
		String payload,
		boolean shouldFail
) {
	public static Event create(String payload, boolean shouldFail) {
		return new Event(UUID.randomUUID().toString(), Instant.now(), payload, shouldFail);
	}
}