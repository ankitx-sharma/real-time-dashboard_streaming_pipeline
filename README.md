# Real-Time Streaming Pipeline + Live Dashboard

## Goal
Build a real-time event streaming pipeline and a **live dashboard** that visualizes the system’s behavior in real time: throughput, queue pressure, latency, and errors.

This project is designed to be:
- **Demo-friendly** (visual proof the system is working)
- **Extensible** (can be reused as a module in larger systems)

---

## MVP (What “Done” Means)
The MVP is complete when the dashboard shows live updates for:

- **Events/sec** (throughput)
- **Total events processed**
- **Average processing latency (ms)**
- **Queue size / backlog**
- **Error count + success vs error**

And the system includes:
- A **load generator** to produce events (configurable rate)
- A **metrics endpoint** (polling-based first, WebSocket optional later)
- A dashboard UI that refreshes metrics every ~1 second

---

## In Scope (Phase 1)
### Backend
- Metrics collection (thread-safe counters + latency tracking)
- Sliding window calculations (e.g., last 10s / last 60s)
- REST API: `GET /api/metrics`
- Load generator (simulate traffic + optional error injection)

### Frontend (Dashboard)
- Simple dashboard with:
  - Metric cards
  - Throughput time-series chart (last 60s)
  - Success vs error chart

---

## Out of Scope (Phase 1)
To keep this project focused and finishable, the MVP will NOT include:
- Authentication / authorization
- Persistent storage for metrics (DB)
- Distributed cluster mode / multi-node replication
- Exactly-once semantics
- Full UI responsiveness / mobile-first design
- Complex alerting pipelines (email/slack)

---

## Success Criteria
- When load increases, the dashboard clearly shows:
  - Events/sec rising
  - Queue size changing (if consumer can’t keep up)
  - Latency reacting under pressure
  - Errors increasing when error injection is enabled
- A reviewer can run the project locally and see results within 2–3 minutes.
