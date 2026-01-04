package com.streaming.project.stream.entity;

import java.time.Instant;
import java.util.UUID;

public record Event(
		String id,
		Instant createdAt,
		String payload
) {
	public static Event create(String payload) {
		return new Event(UUID.randomUUID().toString(), Instant.now(), payload);
	}
}