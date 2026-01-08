type Status = "healthy" | "warning" | "critical";

export function StatusPill({status}: {status: Status}) {
    const label = 
        status === "healthy" ? "HEALTHY" : status === "warning" ? "WARNING" : "CRITICAL";

    const style: React.CSSProperties = {
        display: "inline-flex",
        alignItems: "center",
        gap: 8,
        padding: "6px 10px",
        borderRadius: 999,
        fontWeight: 700,
        fontSize: 12,
        letterSpacing: 0.6,
        border: "1px solid",
    };

    const color =
        status === "healthy"
        ? { bg: "rgba(46, 204, 113, 0.12)", fg: "#1e8f4d", border: "rgba(46, 204, 113, 0.35)" }
        : status === "warning"
        ? { bg: "rgba(241, 196, 15, 0.14)", fg: "#9a7b00", border: "rgba(241, 196, 15, 0.45)" }
        : { bg: "rgba(231, 76, 60, 0.12)", fg: "#b23b30", border: "rgba(231, 76, 60, 0.40)" };

    return (
        <span style={{ ...style, background: color.bg, color: color.fg, borderColor: color.border }} >
            <span 
                style={{ 
                    width: 8, 
                    height: 8, 
                    borderRadius: 999, 
                    background: color.fg, 
                    display: "inline-block" 
                }} />
            {label}
        </span>
    );   
}