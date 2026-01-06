package org.project.loadrunner;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class LoadRunnerApplication {
	
	public static void main(String[] args) {
		String baseUrl = env("BASE_URL", "http://localhost:8080");
		
		int eventPerSecond = Integer.parseInt(env("EPS", "200"));
		int errorPercent = Integer.parseInt(env("ERROR_PERC", "2"));
		
		HttpClient client = HttpClient.newBuilder()
									.connectTimeout(Duration.ofSeconds(2))
									.build();
		
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
		Runnable tick = (() -> {
			for(int i=0; i<eventPerSecond; i++) {
				boolean shouldFail = ThreadLocalRandom.current().nextInt(0, 100) < errorPercent;
				
				String json = """
						{"payload": "demo-payload", "shouldFail": %s}
						""".formatted(shouldFail);
				
				HttpRequest req = HttpRequest.newBuilder()
								.uri(URI.create( baseUrl+"/event/trigger"))
								.timeout(Duration.ofSeconds(2))
								.header("Content-Type", "application/json")
								.POST(HttpRequest.BodyPublishers.ofString(json))
								.build();
				
				client.sendAsync(req, HttpResponse.BodyHandlers.discarding());
			}
		});
		
		executorService.scheduleAtFixedRate(tick, 0, 1, TimeUnit.SECONDS);
		
		System.out.printf("Load runner started -> %s | EPS=%d | ERROR_PERCENT=%d%n",
                baseUrl, eventPerSecond, errorPercent);
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			executorService.shutdownNow();
			System.out.println("Load runner stopped.");
		}));
	}
	
	private static String env(String key, String defaultValue) {
		String value = System.getenv(key);
		return (value == null || value.isBlank()) ? defaultValue : value;
	}
}
