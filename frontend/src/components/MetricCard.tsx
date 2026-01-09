import { useFlashOnStatusChange } from "../hooks/useFlashOnStatusChange";

type Props = {
    label: string;
    value: string;
    hint?: string;
};

export function MetricCard({ label, value, hint }: Props) {
    const flash = useFlashOnStatusChange(value);

    return (
        <div style={{
            border: "1px solid #e6e6e6",
            borderRadius: 14,
            padding: 14,
            background: flash ? "rgba(52, 152, 219, 0.10)" : "white",
            boxShadow: "0 2px 8px rgba(0,0,0,0.05)",
            transition: "background 200ms ease, transform 200ms ease",
        }}>
            <div style={{ fontSize: 12, opacity: 0.7, fontWeight: 600 }}>{label}</div>
            <div style={{ fontSize: 26, fontWeight: 800, marginTop: 6, lineHeight: 1.1 }}>
                {value}
            </div>
            {hint && <div style={{ fontSize: 12, opacity: 0.6, marginTop: 6 }}>{hint}</div>}
        </div>
    );
}