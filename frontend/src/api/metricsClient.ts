import type { MetricsSnapshot } from "../types/metrics"; 

const BASE_URL = import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080";

export async function fetchMetrics() : Promise<MetricsSnapshot> {
    const res = await fetch(`${BASE_URL}/api/metrics`);
    
    if(!res.ok){
        throw new Error(`Failed to fetch metrics: ${res.status}`);
    }
    return res.json();
}