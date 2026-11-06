# EventEase — Event Management System
## Architecture & Requirements Document (Phase 1)

### 1. Project Overview

**EventEase** is a beginner-friendly full-stack web application for a college training project. It allows administrators to manage events and users to browse events and register for them.

**Target audience:** College students presenting a training project. The code is kept simple, understandable, maintainable, and easy to demonstrate. No overengineering.

---

### 2. Technology Stack

| Layer | Technology | Version | Purpose |
|-------|-----------|---------|---------|
| Backend language | Java | 21 LTS | Core application logic |
| Backend framework | Spring Boot | 3.x | REST API, security, data access |
| Data access | Spring Data JPA + Hibernate | — | Database entities & repositories |
| Database | MySQL | 8.x | Relational data store |
| Security | Spring Security + JWT | — | Authentication & authorization |
| Password hashing | BCrypt | — | Secure password storage |
| Build tool | Maven | — | Dependency management & builds |
| Frontend framework | Angular | 21 (standalone components) | SPA UI |
| Frontend language | TypeScript | — | Type-safe frontend code |
| Styling | HTML + CSS (SCSS optional) | — | Responsive UI |

---

### 3. Layered Backend Architecture

The backend follows a clean layered architecture. Each layer has a single responsibility:

```
com.eventease.backend
├── controller      # REST controllers - handle HTTP requests/responses only
├── service         # Service classes - business logic lives here
├── repository      # Spring Data JPA repositories - database queries
├── entity          # JPA entities - database table mappings
├── dto             # Data Transfer Objects - request/response payloads
├── config          # Security, CORS, JWT configuration
└── exception       # Centralized exception handling & custom exceptions
```

**Rules (beginner-friendly):**
* Controllers call services. Controllers do NOT contain business logic.
* Services call repositories. Services contain business logic.
* Repositories only perform database operations via Spring Data JPA.
* DTOs carry data between client and server. Entities are never exposed directly.
* Passwords are hashed before saving and never returned in API responses.

---

### 4. Database Schema (JPA Entities)

Four tables with proper primary keys, foreign keys, unique constraints, and indexes.

#### `users`
| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PK, auto-increment |
| username | VARCHAR(50) | NOT NULL, UNIQUE, indexed |
| email | VARCHAR(100) | NOT NULL, UNIQUE, indexed |
| password | VARCHAR(255) | NOT NULL (hashed) |
| role | VARCHAR(20) | NOT NULL (ADMIN or USER) |
| created_at | TIMESTAMP | NOT NULL |

#### `categories`
| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PK, auto-increment |
| name | VARCHAR(50) | NOT NULL, UNIQUE |

#### `events`
| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PK, auto-increment |
| title | VARCHAR(100) | NOT NULL |
| description | TEXT | — |
| category_id | BIGINT | FK → categories.id |
| event_date | DATE | NOT NULL |
| event_time | TIME | NOT NULL |
| venue | VARCHAR(100) | NOT NULL |
| capacity | INT | NOT NULL |
| organizer_name | VARCHAR(100) | NOT NULL |
| status | VARCHAR(20) | NOT NULL (PUBLISHED, CANCELLED, COMPLETED) |
| created_at | TIMESTAMP | NOT NULL |

*Index on category_id for fast filtering. Index on status/event_date for dashboard queries.*

#### `registrations`
| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PK, auto-increment |
| user_id | BIGINT | FK → users.id |
| event_id | BIGINT | FK → events.id |
| registration_date | TIMESTAMP | NOT NULL |
| status | VARCHAR(20) | NOT NULL (ACTIVE, CANCELLED) |

*Unique constraint on (user_id, event_id) where status = 'ACTIVE' to prevent duplicate active registrations. Indexes on user_id and event_id.*

---

### 5. Relationships

* One category → many events (`@ManyToOne` on Event, `@OneToMany` on Category)
* One event → many registrations (`@OneToMany` on Event, `@ManyToOne` on Registration)
* One user → many registrations (`@OneToMany` on User, `@ManyToOne` on Registration)
* Each registration belongs to one user and one event.

---

### 6. API Endpoints

#### Authentication
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/auth/register` | Register a new user | Public |
| POST | `/api/auth/login` | Login, returns JWT | Public |

#### Events
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/events` | List published events (with search/filter) | Public |
| GET | `/api/events/{id}` | Get event details | Public |
| GET | `/api/admin/events` | List all events | ADMIN |
| POST | `/api/admin/events` | Create event | ADMIN |
| PUT | `/api/admin/events/{id}` | Update event | ADMIN |
| DELETE | `/api/admin/events/{id}` | Delete event (if no active registrations) | ADMIN |
| GET | `/api/admin/events/search` | Search events by title | ADMIN |

#### Registrations
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/registrations` | Register for an event | USER (authenticated) |
| GET | `/api/registrations/my` | View own registrations | USER (authenticated) |
| DELETE | `/api/registrations/{id}` | Cancel own registration | USER (authenticated, owner only) |

#### Dashboards
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/admin/dashboard` | Total events & registration counts | ADMIN |
| GET | `/api/user/dashboard` | User's upcoming & history registrations | USER (authenticated) |

---

### 7. Security Design

* **Authentication:** Stateless JWT. Login returns a token; client sends it in `Authorization: Bearer <token>` header.
* **Authorization:** Role-based access control (`@PreAuthorize("hasRole('ADMIN')")` etc.).
* **Passwords:** BCrypt hashing. Password hash is excluded from all DTO responses.
* **Data privacy:** Users can only view/cancel their own registrations. Service layer enforces ownership checks.
* **CORS:** Configured to allow the Angular development server (`http://localhost:4200`).

---

### 8. Frontend Architecture (Angular Standalone Components)

```
src/app
├── core/
│   ├── services/       # auth.service, event.service, registration.service
│   ├── guards/         # auth.guard, admin.guard (route protection)
│   ├── interceptors/   # jwt.interceptor (adds Bearer token)
│   └── models/         # TypeScript interfaces
├── shared/
│   └── components/     # navbar, loading indicator, alert messages
└── features/
    ├── home/
    ├── login/
    ├── register/
    ├── event-listing/
    ├── event-details/
    ├── admin-dashboard/
    ├── event-management/
    ├── add-event/
    ├── edit-event/
    ├── user-dashboard/
    └── my-registrations/
```

**Features:**
* Angular Router with lazy/standalone routes
* Route guards for protected pages (login required, admin-only)
* Reactive Forms with validation
* HttpClient for API calls
* JWT HTTP interceptor
* Reusable navigation bar showing login/logout and role-based links
* Loading indicators and error/success toast messages
* Responsive CSS with a consistent color palette

---

### 9. Configuration & Security Best Practices

* MySQL connection configured via **environment variables** or an **untracked local config file**.
* `application-example.properties` provided as a safe template (no real credentials).
* `application.properties` is gitignored / not committed with real credentials.
* No secrets hardcoded in source code.

---

### 10. Quality Constraints (from requirements)

* ✅ No microservices
* ✅ No online payment integration
* ✅ No AI features
* ✅ No QR code ticketing
* ✅ No TODO comments on core functionality
* ✅ No hardcoded fake dashboard statistics
* ✅ Beginner-friendly code, minimal abstractions
* ✅ Backend tests for important business logic
* ✅ Backend and frontend builds verified

---

### 11. Development Phases

| Phase | Focus | Status |
|-------|-------|--------|
| Phase 1 | Project structure and architecture document | Complete |
| Phase 2 | Spring Boot backend: entities, repositories, services, validation, exception handling, REST APIs | Complete |
| Phase 3 | Authentication and authorization (Spring Security + JWT) | Complete |
| Phase 4 | Angular frontend and API integration | Complete |
| Phase 5 | Registration, cancellation, capacity validation | Complete |
| Phase 6 | Builds, tests, and documentation | Complete |

---

### 12. Prerequisites

* Java 21 JDK
* Apache Maven 3.9+
* Node.js 18+ and npm
* MySQL Server 8.x

---

### 13. Project Status

All planned phases are complete. The backend test suite passes (21 tests) and the
frontend production build succeeds. See `README.md` for setup and run instructions.