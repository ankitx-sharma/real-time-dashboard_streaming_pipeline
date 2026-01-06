import { PieChart, Pie, Tooltip, ResponsiveContainer, Cell } from "recharts";
import type { MetricsSnapshot } from "../../types/metrics";

type Props = {metrics: MetricsSnapshot | null}

export function SuccessErrorChart({metrics} : Props) {
    const success = metrics?.successCount ?? 0;
    const error = metrics?.errorCount ?? 0;

    const data = [
        {"name" : "Success", value: success},
        {"name" : "Error", value: error}
    ];

    if(!metrics || success + error === 0) {
        return <div style={{ opacity: 0.7 }}> No events yet ...</div>;
    }

    const colors = ["#2ecc71", "#e74c3c"];

    return (
        <ResponsiveContainer width="100%" height={260}>
            <PieChart>
                <Tooltip />
                <Pie data={data} dataKey="value" nameKey="name" outerRadius={90} label>
                    {data.map((_, idx) => (
                        <Cell key={idx} fill={colors[idx % colors.length]} />
                    ))}
                </Pie>
            </PieChart>
        </ResponsiveContainer>
    );
}