<div align="center">

# ⚡ PowerPulse

### Nigeria's Energy Intelligence Platform for SMEs

**Track. Analyse. Reduce. Every kilowatt. Every Naira.**

[![Java](https://img.shields.io/badge/Java-17+-orange?style=flat-square&logo=openjdk)](https://openjdk.org)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-brightgreen?style=flat-square&logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-blue?style=flat-square&logo=postgresql)](https://www.postgresql.org)
[![Liquibase](https://img.shields.io/badge/Liquibase-5.x-red?style=flat-square)](https://liquibase.org)
[![Next.js](https://img.shields.io/badge/Next.js-14+-black?style=flat-square&logo=next.js)](https://nextjs.org)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)](LICENSE)
[![Status](https://img.shields.io/badge/Status-Active%20Development-brightgreen?style=flat-square)]()

<br/>

*The Energy Commission of Nigeria estimates Nigerians spend $22 billion annually fuelling generators.*
*Nigeria has 22 million generators — 8x the capacity of the national grid.*
*Yet most SMEs have no data on what that power actually costs them per day.*

</div>

---

## 🇳🇬 The Problem

Every Nigerian SME runs on three power sources simultaneously:

```
NEPA (Grid)   →  4–8 hours/day on Lagos Mainland
Generator     →  12–16 hours/day, ₦1,350/litre petrol
Inverter      →  Bridges the gaps, battery drains quietly
```

At the end of the month, a bakery owner in Mushin knows three things:

- The NEPA bill arrived
- The generator drank fuel
- Money disappeared

They **cannot** tell you:
- Which hours the generator actually ran
- What their total energy cost was per day in Naira
- When the fuel will run out at current consumption rate
- Whether their generator dependency is getting worse

**The numbers are staggering:**

| Stat | Figure | Source |
|---|---|---|
| Annual spend on generator fuel | **$22 billion** | Energy Commission of Nigeria |
| Full generator economy (hardware + fuel + maintenance) | **$14 billion** | Central Bank of Nigeria |
| Average SME monthly fuel spend | **₦20,000 – ₦40,000** | SEforALL, 2024 |
| Total generators in Nigeria | **22 million** | Christensen Institute |
| Combined generator capacity | **42 GW** | Christensen Institute |
| National grid capacity | **~5.4 GW** | NERC |
| Grid collapses in H1 2024 alone | **12 times** | Energy Transition Africa, 2026 |
| Annual economic losses from power unreliability | **$25 billion** | World Bank |

**PowerPulse fixes this — one business at a time.**

---

## 💡 What PowerPulse Does

PowerPulse is not a calculator. It is a **simulation-based energy cost intelligence engine** that:

- Models real Nigerian SME energy behaviour across 7 business archetypes
- Tracks usage across Generator, NEPA, and Inverter with timestamps
- Converts every hour of usage into exact Naira cost
- Generates plain-English recommendations a non-technical business owner can act on

```
"Your generator cost ₦47,200 this week — 74% of your total energy spend.
 NEPA was available for 6 hours daily but your generator ran for 18 hours.
 You are spending ₦8,400/day that could be avoided."
```

That is what PowerPulse produces. Not raw data. Insight.

---

## 🏗️ System Architecture

```
┌──────────────────────────────────────────────────────┐
│                  Next.js Frontend                    │
│     Dashboard · Weekly Reports · Alerts · Trends     │
└────────────────────────┬─────────────────────────────┘
                         │  REST API v1 (JSON)
┌────────────────────────▼─────────────────────────────┐
│              Spring Boot 4 Backend                   │
│                                                      │
│  BusinessProfileController  EnergyUsageController    │
│         ↓                          ↓                 │
│  BusinessProfileService    EnergyUsageService        │
│         ↓                          ↓                 │
│  BusinessProfileRepository  EnergyUsageRepository    │
│                    ↓                                 │
│             GlobalExceptionHandler                   │
└────────────────────────┬─────────────────────────────┘
                         │  JPA / Hibernate / Liquibase
┌────────────────────────▼─────────────────────────────┐
│                  PostgreSQL 15                       │
│     business_profiles · energy_usage_records         │
└──────────────────────────────────────────────────────┘
```

---

## 🧩 Domain Model

### Core OOP Hierarchy

```
PowerSource (interface)
  └── AbstractPowerSource (abstract class)
            ├── Generator     → extends Abstract + implements Billable
            ├── NepaPower     → extends Abstract (NOT Billable — prepaid)
            └── Inverter      → extends Abstract + implements Billable

Billable (interface)
  └── calculateCost(double kwh) → double
  └── getCostSummary(double kwh) → String  (default method)
```

**Why this design:**

`PowerMonitorService` accepts `List<? extends PowerSource>` and calls the same methods on all three sources without ever checking the type. Adding a fourth source — solar panels, gas turbine — requires zero changes to any service.

### Entity Hierarchy

```
BaseEntity (@MappedSuperclass)
  ├── id              → UUID (auto-generated)
  ├── createdAt       → set by Spring Data Auditing (@CreatedDate)
  ├── updatedAt       → set by Spring Data Auditing (@LastModifiedDate)
  ├── isDeleted       → soft delete flag (default: false)
  └── deletedAt       → timestamp of soft deletion
        ↓ extends
BusinessProfile              EnergyUsageRecord
├── businessName             ├── businessProfile (FK)
├── businessType (enum)      ├── sourceType (enum)
└── location (enum)          ├── hoursUsed (BigDecimal)
                             ├── estimatedKwh (BigDecimal)
                             ├── costNaira (BigDecimal)
                             └── timestamp
```

> **Why `BigDecimal` for monetary fields?**
> `double` has floating-point precision issues. `₦47,250.00` must never
> become `₦47,249.9999`. Money is always `BigDecimal`.

### Business Archetypes

| Archetype | Generator % | NEPA % | Inverter % | Nigerian Reality |
|---|---|---|---|---|
| BAKERY | 70% | 20% | 10% | Ovens + refrigeration = continuous power demand |
| COLD_STORAGE | 80% | 10% | 10% | Generator must never stop |
| HOTEL | 40% | 40% | 20% | Balanced — NEPA used when available |
| WORKSHOP | 70% | 20% | 10% | Industrial equipment needs reliable power |
| CAFE | 30% | 50% | 20% | NEPA-sensitive — wifi and devices fail on outage |
| OFFICE | 20% | 50% | 30% | Inverter preference — quiet, lower cost |
| RETAIL | 40% | 40% | 20% | Mixed load — lighting and POS systems |

### Lagos Zone Profiles

```
Zone              NEPA/day     Generator/day    Blackout Risk
──────────────────────────────────────────────────────────────
MUSHIN            2–6 hrs      14–18 hrs        40%
LAGOS_MAINLAND    4–8 hrs      12–16 hrs        30%
IKEJA_GRA         6–10 hrs     10–14 hrs        20%
LEKKI             8–12 hrs     8–12 hrs         10%
LAGOS_ISLAND      8–14 hrs     6–10 hrs         15%
```

---

## 📡 API Reference

All endpoints versioned under `/api/v1/`

### Business Profiles

```http
POST /api/v1/businesses
Content-Type: application/json

{
  "businessName": "Mama Ngozi Bakery",
  "businessType": "BAKERY",
  "location": "LAGOS_MAINLAND"
}
```

**Response — 201 Created:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "businessName": "Mama Ngozi Bakery",
  "businessType": "BAKERY",
  "location": "LAGOS_MAINLAND",
  "createdAt": "2025-06-11T09:42:44"
}
```

```http
GET /api/v1/businesses                              → all businesses
GET /api/v1/businesses?type=BAKERY                  → filter by type
GET /api/v1/businesses?location=LAGOS_MAINLAND      → filter by zone
GET /api/v1/businesses?type=BAKERY&location=MUSHIN  → combined filter
GET /api/v1/businesses?name=ngozi                   → search by name
GET /api/v1/businesses/{id}                         → get one
DELETE /api/v1/businesses/{id}                      → soft delete
```

> One endpoint handles all filtering via a single JPQL query with nullable
> parameters — no if/else chains in the service layer.

### Energy Usage

```http
POST /api/v1/usage
Content-Type: application/json

{
  "businessId": "550e8400-e29b-41d4-a716-446655440000",
  "sourceType": "GENERATOR",
  "hoursUsed": 14.5,
  "estimatedKwh": 36.25,
  "costNaira": 49037.50,
  "timestamp": "2025-06-11T08:00:00"
}
```

```http
GET /api/v1/usage                           → all records
GET /api/v1/usage/business/{businessId}     → records for one business
```

### Valid Enums

| Field | Valid Values |
|---|---|
| `businessType` | `BAKERY`, `HOTEL`, `CAFE`, `OFFICE`, `COLD_STORAGE`, `WORKSHOP`, `RETAIL` |
| `location` | `LAGOS_MAINLAND`, `LAGOS_ISLAND`, `IKEJA_GRA`, `LEKKI`, `MUSHIN` |
| `sourceType` | `GENERATOR`, `NEPA`, `INVERTER` |

---

## 🔧 Tech Stack

| Layer | Technology | Decision Rationale |
|---|---|---|
| Language | Java 17 | Current LTS — records, pattern matching |
| Framework | Spring Boot 4 | Production standard for Java backends |
| Database | PostgreSQL 15 | Reliable, excellent for time-series analytics |
| ORM | Spring Data JPA + Hibernate | Clean repository pattern, zero SQL boilerplate |
| Migrations | Liquibase 5 | Version-controlled schema — no `ddl-auto=update` in production |
| Validation | Jakarta Bean Validation | DTO-level input validation, not entity-level |
| Build | Maven | Standard Java build tool |
| Frontend | Next.js 14 | React-based, SSR for dashboards |

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- Docker (for PostgreSQL)

### Setup

```bash
# Clone
git clone https://github.com/samzion/powerpulse-be.git
cd powerpulse-be

# Start PostgreSQL via Docker
docker run --name powerpulse-db \
  -e POSTGRES_DB=powerpulse \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  -d postgres:15

# Configure
cp src/main/resources/application.example.properties \
   src/main/resources/application.properties

# Run — Liquibase creates all tables automatically
mvn spring-boot:run
```

Server starts at `http://localhost:8080`

Liquibase runs migrations on startup:
```
001-create-business-profiles.xml    → business_profiles table + indexes
002-create-energy-usage-records.xml → energy_usage_records table + indexes
```

---

## 📁 Project Structure

```
src/main/java/com/powerpulse/
│
├── config/
│   └── GlobalExceptionHandler.java      # Consistent error responses
│
├── controllers/
│   ├── BusinessProfileController.java   # /api/v1/businesses
│   └── EnergyUsageController.java       # /api/v1/usage
│
├── services/
│   ├── BusinessProfileService.java
│   ├── EnergyUsageService.java
│   ├── WeeklyReportService.java          # coming Week 3
│   └── SeedDataService.java             # coming Week 5
│
├── repositories/
│   ├── BusinessProfileRepository.java   # JPQL flexible filter query
│   └── EnergyUsageRepository.java       # Aggregate cost queries
│
├── models/
│   ├── domain/                          # Pure Java OOP — no Spring
│   │   ├── PowerSource.java             (interface)
│   │   ├── AbstractPowerSource.java     (abstract class)
│   │   ├── Billable.java                (interface)
│   │   ├── Generator.java
│   │   ├── NepaPower.java
│   │   └── Inverter.java
│   │
│   ├── entities/                        # JPA entities
│   │   ├── BaseEntity.java              (@MappedSuperclass — UUID, audit, soft delete)
│   │   ├── BusinessProfile.java
│   │   └── EnergyUsageRecord.java
│   │
│   ├── enums/
│   │   ├── BusinessType.java
│   │   ├── LagosZone.java
│   │   └── SourceType.java
│   │
│   ├── requests/                        # Inbound DTOs (Java records + validation)
│   │   ├── BusinessProfileRequest.java
│   │   └── EnergyUsageRequest.java
│   │
│   └── responses/                       # Outbound DTOs (Java records)
│       ├── BusinessProfileResponse.java
│       └── EnergyUsageResponse.java
│
└── PowerPulseApplication.java           # @EnableJpaAuditing
```

---

## 🗺️ Roadmap

**Foundation (Week 1)**
- [x] PowerSource interface hierarchy — polymorphic design
- [x] AbstractPowerSource — shared behaviour without duplication
- [x] Billable interface — Generator and Inverter implement, NEPA does not
- [x] PowerMonitorService — `List<? extends PowerSource>` pattern
- [x] Dynamic cost rates — constructor-injected, never hardcoded

**Backend API (Week 2)**
- [x] BaseEntity — UUID primary keys, Spring Data Auditing, soft delete
- [x] BusinessProfile entity + CRUD endpoints
- [x] EnergyUsageRecord entity + logging endpoints
- [x] Flexible JPQL filter query — type + location + name in one query
- [x] Liquibase migrations — version-controlled schema from day one
- [x] GlobalExceptionHandler — consistent error response shape
- [x] Bean Validation on all request DTOs
- [x] API versioning — `/api/v1/`

**Data Intelligence (Week 3)**
- [ ] WeeklyEnergyReport — computed on demand, not stored
- [ ] CostCalculator strategy — per source, swappable algorithm
- [ ] Cost aggregation by source for any date range

**Intelligence Layer (Week 4)**
- [ ] BusinessArchetype enum with usage weights
- [ ] InsightRule interface + 4 rule implementations
- [ ] RuleEngine — evaluates all rules per report
- [ ] FuelPredictor — hours remaining at current consumption

**Realism Layer (Week 5)**
- [ ] POST /api/seed/generate-demo-data
- [ ] 30 synthetic Lagos SMEs across all archetypes and zones
- [ ] 30 days of realistic usage per business
- [ ] Zone-based outage probability and NEPA availability variability

**Production Quality (Week 6)**
- [ ] Structured logging — @Slf4j on all services
- [ ] Pagination on all list endpoints
- [ ] DB query performance review
- [ ] Edge case tests and coverage report

**Auth + Docs (Week 7)**
- [ ] JWT authentication
- [ ] OWNER / ADMIN role-based access
- [ ] Swagger / OpenAPI documentation

**Deployment (Week 8)**
- [ ] Railway / Render deployment
- [ ] Environment-specific config (dev / prod profiles)
- [ ] Live demo with seed data

---

## 🧠 Engineering Decisions

| Decision | What | Why |
|---|---|---|
| `BigDecimal` for money | All cost fields | `double` loses precision — ₦47,250 must never become ₦47,249.99 |
| UUID primary keys | All entities | Sequential Long IDs are enumerable — UUID is safe |
| Soft delete | `isDeleted` + `deletedAt` | Deleted usage records would corrupt historical cost reports |
| Liquibase over `ddl-auto=update` | Schema migrations | Version-controlled, team-safe, production-grade |
| Java records for DTOs | All requests/responses | Immutable, zero boilerplate, clean API boundary |
| JPQL nullable params | Filter query | One query handles all combinations — no if/else routing in service |
| `List<? extends PowerSource>` | Service generics | Accepts Generator, NepaPower, Inverter or any future subtype |
| No ML / no external APIs | Insight rules | Deterministic, testable, deployable without dependencies |
| API versioning from day one | `/api/v1/` | Breaking changes in future don't break existing clients |

---

## 📊 Nigerian Energy Constants

All values from public Nigerian sources:

| Constant | Value | Source |
|---|---|---|
| Annual generator fuel spend | $22 billion | Energy Commission of Nigeria |
| Full generator economy | $14 billion | Central Bank of Nigeria |
| Petrol pump price | ₦1,350/litre | NNPC, June 2025 |
| Generator fuel consumption | ~0.3 litres/kWh | 3kVA generator standard |
| Generator rated output | 2.5 kWh | Typical residential/SME |
| NEPA tariff — R2 Band A | ₦66/kWh | NERC, 2025 |
| NEPA commercial — C1 Band A | ₦119/kWh | NERC, 2025 |
| Inverter cost (amortised) | ₦150/kWh | 200Ah battery / 500 cycles |
| Inverter rated output | 0.8 kWh | 1kVA home inverter |
| Total generators in Nigeria | 22 million | Christensen Institute |
| Combined generator capacity | 42 GW | Christensen Institute |
| National grid capacity | ~5.4 GW | NERC |
| Grid collapses in H1 2024 | 12 times | Energy Transition Africa |

---

## 👨‍💻 Author

**Samson Kayode**
Backend Software Engineer · Lagos, Nigeria
[github.com/samzion](https://github.com/samzion) · [linkedin.com/in/kayodesamson](https://linkedin.com/in/kayodesamson) · kayodesamson4@gmail.com

> *"Nigeria has 22 million generators with 42 GW of combined capacity —
> eight times the national grid. Every single one of them runs blind.
> PowerPulse is built to change that."*

---

## 📄 Licence

MIT — free to use, modify, and distribute.

---

<div align="center">

**⚡ PowerPulse — Built for Nigeria. Designed to last.**

</div>
