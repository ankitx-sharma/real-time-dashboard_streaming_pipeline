export type MetricsSnapshot = {
    eventsPerSecond: number;
    totalEvents: number;
    avgLatencyMs: number;
    queueSize: number;
    successCount: number;
    errorCount: number;
    timestamp: string;
}