import type { MetricsSnapshot } from "../types/metrics"; 

const BASE_URL = import.meta.env.VITE_BASE_URL ?? "";

export async function fetchMetrics() : Promise<MetricsSnapshot> {
    const res = await fetch(`${BASE_URL}/api/fetch/metrics`);
    
    if(!res.ok){
        throw new Error(`Failed to fetch metrics: ${res.status}`);
    }
    return res.json();
}