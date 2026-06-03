<div align="center">

# ⚡ PowerPulse

### Energy Monitoring System for Nigeria

*Because Nigerians spend $14 billion yearly on private power — blindly.*

[![Java](https://img.shields.io/badge/Java-17+-orange?style=flat-square&logo=java)](https://www.java.com)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-brightgreen?style=flat-square&logo=spring)](https://spring.io/projects/spring-boot)
[![Next.js](https://img.shields.io/badge/Next.js-14+-black?style=flat-square&logo=next.js)](https://nextjs.org)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-blue?style=flat-square&logo=postgresql)](https://www.postgresql.org)
[![Status](https://img.shields.io/badge/Status-In%20Development-yellow?style=flat-square)]()

</div>

---

## 🇳🇬 The Problem

The average Nigerian home or business runs on **three power sources simultaneously** — NEPA (grid), a Generator, and an Inverter. Most people have no idea:

- Which hours the generator actually ran
- What their total energy cost was last week
- When the fuel will run out at current consumption rate
- Whether NEPA supply has been improving or getting worse

This isn't a small problem. Nigeria spends **$14 billion per year** on private power generation. That money is spent completely blind.

**PowerPulse fixes that.**

---

## 💡 What It Does

| Feature | Description |
|---|---|
| **Usage Logging** | Log kWh consumed per source with timestamps |
| **Daily Summary** | Total kWh and cost breakdown per source per day |
| **Cost Analytics** | Generator (fuel × pump price), NEPA (tariff), Inverter (battery amortisation) |
| **Fuel Prediction** | At current usage rate, generator fuel lasts X more hours |
| **Smart Alerts** | Generator running > 6hrs, inverter battery < 20%, NEPA down > 24hrs |
| **Source Comparison** | Which source is cheapest per kWh this week? |

---

## 🏗️ Architecture

```
┌─────────────────────────────────────┐
│         Next.js Frontend            │
│  Dashboard · Charts · Alerts        │
└──────────────┬──────────────────────┘
               │ REST API
┌──────────────▼──────────────────────┐
│       Spring Boot Backend           │
│                                     │
│  Controller → Service → Repository  │
│         ↓           ↓               │
│    AlertService   RuleEngine        │
└──────────────┬──────────────────────┘
               │ JPA
┌──────────────▼──────────────────────┐
│         PostgreSQL                  │
│  usage_log · alerts · cost_config   │
└─────────────────────────────────────┘
```

---

## 🧩 Domain Model

The core design uses a clean interface hierarchy:

```java
// Contract — what every source must do
public interface PowerSource {
    String getSourceName();
    double getCurrentOutput();  // kWh
    boolean isAvailable();
}

// Shared behaviour — written once, inherited by all
public abstract class AbstractPowerSource implements PowerSource {
    public void logUsage(double kwh) { ... }
    public void logCost(double kwh, double rate) { ... }
}

// Capability — only billable sources implement this
public interface Billable {
    double calculateCost(double kwh);
}
```

```
PowerSource (interface)
    └── AbstractPowerSource (abstract class)
            ├── Generator     → implements Billable (fuel cost)
            ├── NepaPower     → NOT Billable (prepaid meter)
            └── Inverter      → implements Billable (battery cost)
```

**Key design decision:** `Generator` and `Inverter` accept their cost rates at construction time — not hardcoded. When Nigeria's fuel price changes (and it will), you update one config value.

```java
new Generator(fuelLevel: 10.0, fuelRatePerKwh: 1350.0)  // ₦1,350/litre
new Inverter(batteryPercent: 85.0, batteryCostPerKwh: 150.0)
```

---

## 📁 Project Structure

```
src/main/java/com/powerpulse/
├── controller/
│   └── PowerUsageController.java    # HTTP layer only
├── service/
│   ├── PowerUsageService.java       # Business logic
│   └── PowerMonitorService.java     # Source monitoring
├── repository/
│   └── PowerUsageRepository.java    # Data access
├── domain/
│   ├── PowerSource.java             # Core interface
│   ├── AbstractPowerSource.java     # Shared behaviour
│   ├── Billable.java                # Cost capability
│   ├── Generator.java
│   ├── NepaPower.java
│   └── Inverter.java
├── model/
│   ├── PowerUsage.java              # JPA entity
│   ├── PowerUsageRequest.java       # Inbound DTO
│   └── PowerUsageResponse.java      # Outbound DTO
└── exception/
    ├── GlobalExceptionHandler.java
    └── PowerSourceNotFoundException.java
```

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- PostgreSQL 15+
- Node.js 18+ (frontend)

### Backend Setup

```bash
# Clone the repo
git clone https://github.com/samzion/powerpulse.git
cd powerpulse

# Create the database
psql -U postgres -c "CREATE DATABASE powerpulse;"

# Configure your database credentials
cp src/main/resources/application.example.properties \
   src/main/resources/application.properties
# Edit application.properties with your DB credentials

# Run the application
mvn spring-boot:run
```

### API is running at `http://localhost:8080`

---

## 📡 API Reference

### Log Power Usage

```http
POST /api/usage
Content-Type: application/json

{
  "sourceType": "GENERATOR",
  "kwh": 2.5,
  "timestamp": "2025-05-12T14:00:00",
  "costNaira": 3375.00
}
```

**Response — 201 Created:**
```json
{
  "id": 1,
  "sourceType": "GENERATOR",
  "kwh": 2.5,
  "timestamp": "2025-05-12T14:00:00",
  "costNaira": 3375.00
}
```

### Get Usage History

```http
GET /api/usage
```

**Response — 200 OK:**
```json
[
  {
    "id": 1,
    "sourceType": "GENERATOR",
    "kwh": 2.5,
    "timestamp": "2025-05-12T14:00:00",
    "costNaira": 3375.00
  }
]
```

### Valid Source Types

| Value | Description |
|---|---|
| `GENERATOR` | Petrol/diesel generator |
| `NEPA` | Grid supply (DISCO) |
| `INVERTER` | Battery inverter |

---

## 🔧 Tech Stack

| Layer | Technology | Why |
|---|---|---|
| Backend | Java 17 + Spring Boot 4 | Production-grade, industry standard |
| Frontend | Next.js 14 | Fast, React-based, great DX |
| Database | PostgreSQL | Reliable, great for time-series data |
| ORM | Spring Data JPA + Hibernate | Clean repository pattern |
| Build | Maven | Standard Java build tool |

---

## 🗺️ Roadmap

- [x] Core domain model — PowerSource interface hierarchy
- [x] AbstractPowerSource — shared logging and cost methods
- [x] Billable interface — dynamic cost rates per source
- [x] PowerMonitorService — polymorphic source monitoring
- [ ] REST API — log and retrieve usage
- [ ] Daily usage summary endpoint
- [ ] Cost analytics by source
- [ ] Fuel prediction engine
- [ ] Alert rule engine
- [ ] JWT authentication
- [ ] Swagger documentation
- [ ] Frontend dashboard
- [ ] Deployment

---

## 🧠 OOP Concepts Applied

This project was built to demonstrate clean object-oriented design:

| Concept | Where Applied |
|---|---|
| **Interface** | `PowerSource` — contract for all energy sources |
| **Abstract Class** | `AbstractPowerSource` — shared `logUsage()` and `logCost()` without duplication |
| **Polymorphism** | `PowerMonitorService` calls the same methods on all 3 sources without knowing their type |
| **Multiple Interface Implementation** | `Generator` extends `AbstractPowerSource` AND implements `Billable` |
| **Dependency Injection** | Cost rates injected at construction — not hardcoded |
| **Strategy Pattern** | Coming in Week 4 — swappable prediction algorithms |
| **Observer Pattern** | Coming in Week 4 — decoupled alert system |

---

## 📊 Why This Project Matters

> *"Nigeria loses $14 billion per year to private power generation costs.  
> Most of that money is spent without data. PowerPulse is built for the  
> millions of Nigerians who deserve to understand where their money goes."*

This is not a tutorial project. It solves a real problem felt by real people every single day.

---

## 👨‍💻 Author

**Samson Kayode** — Software Engineer  
[LinkedIn](https://linkedin.com/in/kayodesamson) · [GitHub](https://github.com/samzion) · kayodesamson4@gmail.com

---

## 📄 Licence

MIT — free to use, modify, and distribute.
