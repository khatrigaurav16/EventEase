# EventEase — Event Management System

A full-stack web application for managing college events. Administrators create and
manage events; users browse events and register for them.

Built with **Spring Boot + MySQL** on the backend and **Angular** on the frontend.

---

## Tech Stack

| Layer | Technology |
|-------|------------|
| Language | Java 21, TypeScript |
| Backend | Spring Boot 3.3, Spring Data JPA, Hibernate, Spring Security, JWT |
| Database | MySQL 8.x (H2 in-memory database for tests) |
| Frontend | Angular 21 (standalone components), Reactive Forms, HttpClient |
| Build | Maven (backend), npm / Angular CLI (frontend) |

---

## Project Structure

```
EventEase/
├── backend/                      # Spring Boot Maven application
│   ├── src/main/java/com/eventease/backend/
│   │   ├── controller/           # REST controllers
│   │   ├── service/              # Business logic
│   │   ├── repository/           # Spring Data JPA repositories
│   │   ├── entity/               # JPA entities (User, Category, Event, Registration)
│   │   ├── dto/                  # Data Transfer Objects
│   │   ├── config/               # Security, JWT, CORS, data initializer
│   │   └── exception/            # Centralized exception handling
│   ├── src/main/resources/
│   │   ├── application-example.properties   # Safe config template (copy this)
│   │   └── application.properties           # Local config (gitignored)
│   ├── src/test/                 # Unit and integration tests
│   └── pom.xml
├── frontend/                     # Angular application
│   ├── src/app/
│   │   ├── core/                 # Services, guards, interceptors, models
│   │   ├── shared/               # Reusable components (loading, alert)
│   │   └── features/             # Page components
│   ├── angular.json
│   ├── package.json
│   └── tsconfig*.json
├── ARCHITECTURE.md               # Architecture and design decisions
├── LICENSE                       # MIT License
├── .gitignore
└── README.md
```

---

## Prerequisites

| Tool | Version |
|------|---------|
| Java JDK | 21 |
| Apache Maven | 3.9+ |
| Node.js | 18+ |
| npm | 9+ |
| MySQL Server | 8.x |

Make sure `JAVA_HOME` points to your JDK 21 installation before building.

---

## Getting Started

### 1. Set up the database

Start MySQL, then create the database (the connection string can also create it
automatically on first run):

```sql
CREATE DATABASE eventease;
```

### 2. Configure the backend

Copy the example configuration and fill in your own values:

```bash
# Windows
copy backend\src\main\resources\application-example.properties backend\src\main\resources\application.properties

# macOS / Linux
cp backend/src/main/resources/application-example.properties backend/src/main/resources/application.properties
```

Edit `application.properties`:

- `spring.datasource.username` — your MySQL username
- `spring.datasource.password` — your MySQL password
- `jwt.secret` — a strong random string (at least 32 characters)

Values can also be supplied through environment variables `DB_USERNAME`,
`DB_PASSWORD`, and `JWT_SECRET`.

> `application.properties` is gitignored. Never commit real credentials.

### 3. Run the backend

```bash
cd backend
mvn spring-boot:run
```

The API is available at `http://localhost:8080`.

### 4. Run the frontend

```bash
cd frontend
npm install
npm start
```

The app is available at `http://localhost:4200`.

### 5. Log in

A default admin account is seeded on first startup:

| Username | Password | Role |
|----------|----------|------|
| `admin` | `password` | ADMIN |

Change this password before using the app outside a local demo. Additional
users can be created from the `/register` page.

---

## Running Tests

Backend tests use an embedded H2 database and do not require MySQL:

```bash
cd backend
mvn test
```

The suite contains **21 tests** covering security, users, events, registrations,
and admin statistics.

Frontend production build:

```bash
cd frontend
npm run build
```

---

## API Endpoints

| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| POST | `/api/auth/register` | Public | Register a new user |
| POST | `/api/auth/login` | Public | Login and receive a JWT |
| GET | `/api/events` | Public | List published events |
| GET | `/api/events/{id}` | Public | Event details |
| GET | `/api/events/search?title=...` | Public | Search events |
| GET | `/api/events/category/{id}` | Public | Filter by category |
| GET | `/api/categories` | Public | List categories |
| GET | `/api/admin/events` | ADMIN | List all events |
| POST | `/api/admin/events` | ADMIN | Create an event |
| PUT | `/api/admin/events/{id}` | ADMIN | Update an event |
| DELETE | `/api/admin/events/{id}` | ADMIN | Delete an event |
| GET | `/api/admin/dashboard` | ADMIN | Admin statistics |
| POST | `/api/registrations` | USER | Register for an event |
| GET | `/api/registrations/my` | USER | View own registrations |
| DELETE | `/api/registrations/{id}` | USER | Cancel own registration |
| GET | `/api/user/dashboard` | USER | User dashboard data |

---

## Database Schema

Four tables managed by JPA/Hibernate:

- **users** — id, username (unique), email (unique), password (hashed), role, created_at
- **categories** — id, name (unique)
- **events** — id, title, description, category_id, event_date, event_time, venue, capacity, organizer_name, status, created_at
- **registrations** — id, user_id, event_id, registration_date, status

A unique constraint on `(user_id, event_id)` where `status = 'ACTIVE'` prevents
duplicate active registrations.

---

## Features

- User registration and login with BCrypt password hashing
- Stateless JWT authentication
- Role-based access control (ADMIN / USER)
- Admin event management: create, view, update, delete, search
- Browse, search, and filter events by category
- Event registration with capacity validation
- Duplicate, past-event, cancelled-event, and full-event protection
- Users can cancel their own registrations
- Admin dashboard with real statistics
- User dashboard with upcoming and past registrations
- Centralized exception handling with correct HTTP status codes
- CORS configured for the Angular development server
- Password hashes never exposed in API responses
- Route guards and a JWT HTTP interceptor on the frontend
- Responsive UI with loading indicators and success/error messages

---

## Security Notes

- Real credentials are never committed. Use `application-example.properties` as
  the template and keep `application.properties` local.
- Passwords are hashed with BCrypt.
- JWTs are stateless and validated on every protected request.
- Ownership checks ensure users can only view or cancel their own registrations.

---

## License

This project is licensed under the [MIT License](LICENSE).
