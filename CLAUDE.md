# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
./gradlew build
./gradlew clean build

# Run application
./gradlew :module-api:bootRun

# Run all tests
./gradlew test

# Run tests for a specific module
./gradlew :module-core:test
./gradlew :module-api:test

# Run a single test class
./gradlew :module-core:test --tests "io.github.dongjulim.domain.order.usecase.service.SaveOrderServiceTest"
```

## Architecture

This is a **multi-module Gradle project** (Java 17, Spring Boot 2.7.3) structured as follows:

| Module | Role |
|--------|------|
| `module-api` | REST controllers, scheduler, application entry point |
| `module-core` | Domain entities, repositories, use case services (business logic) |
| `module-auth` | JWT filter chain, Spring Security configuration |
| `module-common` | Shared config (Async, Password encoding), RFC 9457 `ProblemDetail` |

All packages are under `io.github.dongjulim`.

### Layer conventions

1. **Controller** (`module-api/api/controller/<domain>/`) — one class per use case (e.g., `SaveOrderController`, `FindOrderDetailController`). Delegates to a `UseCase` interface.
2. **UseCase / Service** (`module-core/domain/<domain>/usecase/`) — interface + `*Service` implementation. Business logic lives here.
3. **Entity** (`module-core/domain/<domain>/entity/`) — rich domain objects with state-transition methods (e.g., `order.cancel()`, `order.complete()`). All entities extend `BaseEntity` (audit fields: `createAt`, `createBy`, `updateAt`, `updateBy`).
4. **Repository** (`module-core/domain/<domain>/repository/`) — Spring Data JPA repositories.
5. **Component** (`module-core/domain/<domain>/component/`) — helpers for shared cross-domain logic (e.g., `OrderCreationHelper` consolidates coupon and point application).
6. **Loader** (`module-core/domain/<domain>/component/*Loader.java`) — fetches the currently authenticated user or other commonly needed aggregates.

### Security

- **Stateless JWT**. Two custom filters in `module-auth`:
  - `JwtTokenIssueFilter` — issues tokens on `POST /api/v2/login`
  - `JwtTokenAuthenticationFilter` — validates tokens on all other protected endpoints
- Roles: `USER`, `ADMIN`, `MASTER`. `ADMIN`/`MASTER` are required for admin operations (deliveries, notices, notifications, product categories, stock, coupons).
- CORS is configured for `http://localhost:5173`.

### Databases

- **Production**: PostgreSQL
- **Tests**: H2 in-memory (`jdbc:h2:~/nore-lento;AUTO_SERVER=TRUE`), configured in `application-test.yml`
- DDL: `hibernate.ddl-auto=update` (no migration tool — schema is managed by Hibernate)
- Soft delete: entities use a `deleteCheck` boolean instead of hard deletes.

### Domains

The system models an e-commerce platform. Key domains and their notable design decisions:

- **Order**: `OrderItem` stores a price snapshot at order time. Shared creation logic (coupon + point application) lives in `OrderCreationHelper`.
- **Payment**: status/method enum (CARD, BANK_TRANSFER, KAKAO_PAY) — no external gateway is integrated yet.
- **Notification**: supports unread filtering; async delivery via `AsyncConfig`.
- **UserPoint / PointHistory**: point ledger with full history.
- **Stock**: separate `Stock` entity (1:1 with Product) for inventory management.

### Error handling

API errors follow **RFC 9457** using the custom `ProblemDetail` class in `module-common`.

## Key files

- `module-api/src/main/resources/application.yml` — server port (8080), encoding
- `module-api/src/main/resources/application-test.yml` — JWT secret, H2 datasource, file upload dir
- `module-auth/src/main/java/.../security/config/SecurityConfig.java` — full security/filter configuration
- `module-core/src/main/java/.../domain/common/entity/BaseEntity.java` — base class all entities extend
- `module-core/src/main/java/.../domain/order/component/OrderCreationHelper.java` — cross-domain order logic
- `ERD.md` — full database schema with table names and relationships
