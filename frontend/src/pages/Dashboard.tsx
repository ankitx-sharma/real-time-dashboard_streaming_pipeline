import { useEffect, useMemo, useState } from "react";
import { fetchMetrics } from "../api/metricsClient";
import type { MetricsSnapshot } from "../types/metrics";

import { MetricCard } from "../components/MetricCard";
import { ThroughputChart } from "../components/Charts/ThroughputChart";
import { SuccessErrorChart } from "../components/Charts/SuccessErrorChart";

function formatNumber(n: number) {
    return new Intl.NumberFormat().format(n);
}

export function Dashboard() {
    const [ metrics, setMetrics ] = useState<MetricsSnapshot | null>(null);
    const [ error, setError ] = useState<string | null>(null);
    const [ isStale, setIsStale ] = useState(false);

    // Simple “last 60s” history for charts later
    const [ history, setHistory ] = useState<Array<MetricsSnapshot>>([]);

    useEffect(() => {
        let alive = true;
        let timer: number | undefined;

        async function tick() {
            try {
                const m = await fetchMetrics();
                if(!alive) return;

                setMetrics(m);
                setError(null);
                setIsStale(false);

                setHistory(prev => {
                    const next = [...prev, m];
                    // keep last ~60 entries (1 per second)
                    return next.slice(Math.max(0, next.length - 60));
                });
            } catch (e: any){
                if(!alive) return;
                setError(e?.message() ?? "Unknown issue");
                setIsStale(true);
            } finally {
                timer = window.setTimeout(tick, 1000);
            }
        }

        tick();

        return(() => {
            alive = false;
            if(timer) window.clearTimeout(timer);
        });
    }, []);

    const status = useMemo(() => {
        if(isStale) return { text: "DEGRADED", icon: "🔴" }
        return { text: "RUNNING", icon: "🟢" }
    }, [isStale]);

    const successRate = useMemo(() => {
        if(!metrics) return "-";
        const total = metrics.successCount + metrics.errorCount;
        if( total === 0 ) return "-";
        return `${((metrics.successCount / total ) * 100 ).toFixed(1)}%`;
    }, [metrics]);

    return (
        <div style={{ padding: 24, fontFamily: "system-ui, sans-serif", background: "#f6f7fb", minHeight: "100vh" }}>
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "baseline", marginBottom: 16 }}>
                <div>
                    <div style={{ fontSize: 22, fontWeight: 800 }}>Real-Time Streaming Dashboard</div>
                    <div style={{ opacity: 0.7, marginTop: 4 }}>Live metrics (polling every 1 sec)</div>
                </div>
                <div style={{ fontWeight: 700 }}>
                    {status.icon} {status.text}
                </div>
            </div>

            {error && (
                <div style={{ background: "white", border: "1px solid #f1c1c1", padding: 12, borderRadius: 12, marginBottom: 16 }}>
                    <b> API error:</b> {error}
                </div>
            )}

            <div style={{ display: "grid", gridTemplateColumns: "repeat(5, minmax(160px, 1fr))", gap: 12 }}>
                <MetricCard label="Events/sec" value={metrics ? metrics.eventsPerSecond.toFixed(0) : "-"} />
                <MetricCard label="Total processed" value={metrics ? formatNumber(metrics.totalEvents) : "-"} />
                <MetricCard label="Average latency (ms)" value={metrics ? metrics.avgLatencyMs.toFixed(1) : "-"} />
                <MetricCard label="Queue Size" value={metrics ? formatNumber(metrics.queueSize) : "-"} />
                <MetricCard label="Success rate" value={successRate} hint={metrics ? `Errors: ${formatNumber(metrics.errorCount)}` : undefined} /> 
            </div>

            {/* Charts placeholder (to be implemented in Task 4.3) */}
            <div style={{ marginTop: 18, display: "grid", gridTemplateColumns: "1fr 1fr", gap: 12 }}>
                <div style={{ background: "white", borderRadius: 12, padding: 16, border: "1px solid #ddd"}}>
                    <div style={{ fontWeight: 700, marginBottom: 8 }}>Throughput (last 60s)</div>
                    <ThroughputChart history={history} />
                </div>

                <div style={{ background: "white", borderRadius: 12, padding: 16, border: "1px solid #ddd" }}>
                    <div style={{fontWeight: 700, marginBottom: 8}}>Success vs Error</div>
                    <SuccessErrorChart metrics={metrics} />
                </div>
            </div>
        </div>
    )
}