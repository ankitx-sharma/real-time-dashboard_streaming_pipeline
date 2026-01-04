import { LineChart, Line, XAxis, YAxis, Tooltip, ResponsiveContainer, CartesianGrid } from "recharts";
import type { MetricsSnapshot } from "../../types/metrics";

type Props = { history: MetricsSnapshot[] };

function toTimeLabel(iso: string) {
    const d = new Date(iso);
    return d.toLocaleTimeString([], {hour: "2-digit", minute: "2-digit", second: "2-digit"});
}

export function ThroughputChart({ history }: Props) {
    const data = history.map(m => ({
        t: toTimeLabel(m.timestamp),
        eps: Number.isFinite(m.eventsPerSecond) ? m.eventsPerSecond : 0
    }));

    if (data.length < 2){
        return <div style={{ opacity: 0.7 }}>Not enough data yet ...</div>
    }

    return (
        <ResponsiveContainer width="100%" height={260}>
            <LineChart data={data}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="t" tick={{ fontSize: 12 }} interval="preserveStartEnd" />
                <YAxis tick={{ fontSize: 12 }} />
                <Tooltip />
                <Line type="monotone" dataKey="eps" dot={false} strokeWidth={2} />
            </LineChart>
        </ResponsiveContainer>
    );
}