type Props = {
    label: string;
    value: string;
    hint?: string;
};

export function MetricCard({ label, value, hint }: Props) {
    return (
        <div style={{
            border: "1px solid #ddd",
            borderRadius: 12,
            padding: 16,
            background: "white",
            boxShadow: "0 2px 8px rgba(0,0,0,0.05)"
        }}>
            <div style={{ fontSize: 12, opacity: 0.7 }}>{label}</div>
            <div style={{ fontSize: 28, fontWeight: 700, marginTop: 6 }}>{value}</div>
            {hint && <div style={{ fontSize: 12, opacity: 0.6, marginTop: 6 }}>{hint}</div>}
        </div>
    );
}