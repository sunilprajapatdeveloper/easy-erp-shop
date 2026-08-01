# Production Readiness Analysis and Implementation Roadmap

Date: 2026-08-01

## Executive decision

The application is **not production-ready**. It has broad functional coverage and several useful foundations (DTOs, service interfaces, transaction annotations, optimistic locking on newer entities, warehouse-level price/stock/tax models, centralized payment records, Docker health checks), but it currently fails the minimum release bar for tenant isolation, authorization, schema governance, workflow atomicity, automated testing, and operational safety.

No application code was changed during this phase. This report is based on the checked-out code, configuration, build output, and static tracing. Existing uncommitted frontend work was preserved.

## Scope and evidence

- 1,518 repository files inspected.
- Backend: 61 controllers, 69 service implementations, 67 repositories, 78 entities, and 164 DTOs.
- Frontend: Vue 3, Vue Router, Pinia, Axios, Bootstrap, and TypeScript.
- Automated tests found: one Spring context test. `MediaServiceTest.java` is under `src/main`, so it is not a normal test source.
- Database migrations found: only `V2__add_product_search_vector.sql`; there is no baseline migration defining the schema.
- Frontend production build: succeeds with broken-import and bundle-size warnings.
- Backend Gradle verification: Java compilation succeeds, but Gradle reports `:test NO-SOURCE`; no tests actually execute. The build also reports Gradle 9 incompatibility warnings and a deprecated API in `MailService`.

## Architecture map

### Backend

The backend is a modular package-by-layer Spring Boot monolith:

- HTTP controllers delegate to service interfaces/implementations.
- JPA repositories access a shared PostgreSQL schema.
- Domain records generally carry `companyId`; some newer records extend `BaseEntity`, while older records duplicate audit and tenant fields.
- `UserContext` resolves the authenticated user from the database for every context lookup and derives company/user/warehouse identifiers.
- Redis is used for cache-related features; Kafka is used for verification email, payment events, and import/export jobs.
- Payment strategies exist for cash, card, cheque, gift card, PayPal, and UPI, while `PaymentServiceImpl` provides a common payment record model.
- Storage abstractions cover local, S3, Azure, and GCS, but several provider operations are explicitly unimplemented.
- AI provider/orchestration code is present but is not on the critical ERP transaction path.

### Frontend

The frontend is a single Vue SPA:

- Routes map pages for onboarding, settings, products, inventory, purchases, sales, POS, returns, people, payments, reports, and store views.
- Pinia stores generally wrap a one-to-one service module, and services wrap the shared Axios instance.
- Authentication, user data, and tenant identification are persisted in browser local storage.
- The Axios client attaches `X-Company-Id` from mutable local storage and uses a hard-coded `http://localhost:9091/api/v1` base URL.
- Many report, quotation, transfer, and shipment routes are commented out even though pages/components exist.

### Core domain relationships

- Company -> users, warehouses, subscriptions, settings, currencies, and tenant-owned master/transaction data.
- Product is company-owned; `ProductPrice`, `ProductStock`, and `ProductTax` bind product configuration to warehouses.
- Sale -> warehouse, customer, currency, sale products, discounts/promotions, payments, and shipment state.
- Purchase -> supplier, warehouse, products, payments, returns, and inventory effects.
- Payment is polymorphic through `PaymentSourceType` plus a reference ID and stores transaction/base amounts and exchange rate metadata.
- Inventory changes occur across stock services, sales, purchases, returns, transfers, and adjustments; there is no single immutable stock ledger visible in the current design.

## Severity-ranked findings

### Critical

#### C1. Tenant isolation is not enforced centrally

Root cause: tenant isolation relies on individual developers choosing tenant-scoped repository methods. Core services frequently call unscoped `findById`, `findAll`, `existsById`, and `deleteById`. Examples include sales resolving warehouse/customer/product/discount by global ID, payments loading sales/payments by global ID, and unscoped CRUD in brands, categories, customers, adjustment types, expenses, roles, media, exchange rates, POS settings, and payment gateway settings.

Impact: an authenticated user can potentially read, modify, associate, or delete another company's records by guessing identifiers. Passing `X-Company-Id` from local storage does not protect this; authoritative tenancy must come from the authenticated principal.

Impacted areas: security context, virtually all services/repositories, controllers accepting company IDs, and integration tests.

Required remediation:

1. Put immutable `userId`, `companyId`, role/permissions, and token version in the authenticated principal/JWT.
2. Introduce a request-scoped tenant context derived only from authentication.
3. Make tenant-scoped repository access the default and ban direct generic CRUD for tenant entities through architecture tests/static rules.
4. Validate every referenced warehouse, product, customer, supplier, price, stock, tax, discount, payment, and user belongs to the current tenant.
5. Add cross-tenant negative tests for every controller family.

#### C2. Authorization is mostly client-side

Root cause: `SecurityConfig` distinguishes public versus authenticated paths but does not enforce business permissions on controllers/services. Vue route metadata checks permissions only in the browser and includes incorrect mappings (for example, user creation/edit routes require `PRODUCT_CREATE`).

Impact: any authenticated caller can bypass the UI and invoke privileged APIs directly.

Required remediation: define a server-side permission model, add method/controller authorization, verify warehouse assignments, test each role/permission boundary, and treat frontend guards only as UX.

#### C3. Onboarding exposes tenant mutation endpoints publicly

Root cause: company creation, company-currency creation/listing, warehouse-currency creation, currencies, subscription plans, user registration, all media endpoints, actuator endpoints, and scanner WebSockets are permitted broadly. Registration trusts a caller-supplied `X-Company-Id` header when creating the admin user. Company creation and user creation are separate requests without a durable onboarding transaction/saga.

Impact: company takeover, unauthorized tenant configuration, orphan companies, partial setup, enumeration, and abuse of operational endpoints.

Required remediation: use a short-lived, single-purpose onboarding token tied to a pending company/email; implement an idempotent onboarding state machine; restrict actuator/media/scanner access; make company/admin/default settings/subscription initialization atomic or compensatable.

#### C4. Database schema has no reproducible migration baseline

Root cause: Hibernate `ddl-auto=update` is used in Docker production configuration, while the migration directory contains only a PostgreSQL search-vector alteration and no V1 schema. The V2 migration uses `CREATE INDEX CONCURRENTLY`, which cannot run inside Flyway's default transaction without special handling.

Impact: environments drift, deployments are non-repeatable, constraints/indexes cannot be audited, rollback is unsafe, and a fresh production database cannot be reliably built from versioned migrations.

Required remediation: generate and review a V1 baseline, add explicit foreign keys/unique/check constraints/indexes, set production Hibernate to `validate`, configure the concurrent migration correctly, and test migrate-from-zero plus upgrade paths.

#### C5. Sale/POS completion is not one authoritative transaction

Root cause: sale creation calculates and deducts stock before saving, but resolves related records without tenant scoping. Payment creation is a separate operation. There is no proven atomic boundary covering sale, stock mutation, payment(s), invoice/receipt, accounting entry, and idempotent retry. Stock adjustments use read-modify-write integers without a demonstrated pessimistic/atomic update path.

Impact: duplicate sales, negative/oversold stock under concurrency, paid-but-missing orders, orders without payment, and inconsistent retries.

Required remediation: implement a server-owned checkout command with idempotency key, tenant/warehouse validation, locked or atomic stock decrement, payment allocation, outbox events, and deterministic receipt/invoice result. Never trust client totals, unit prices, tax, base amount, or exchange rate without server validation.

#### C6. Financial/accounting integrity is incomplete

Root cause: payment records are centralized but update/delete operations are mutable and unscoped; clients can supply transaction amount, base amount, and exchange rate. No complete double-entry ledger/journal architecture was found for sales, purchases, payments, refunds, and currency gains/losses. A Razorpay refund handler contains a TODO to reverse the order/accounting impact.

Impact: financial history can be changed or deleted, base-currency totals can disagree, and reports cannot be considered auditable.

Required remediation: immutable posted transactions, reversal entries instead of deletion, server-selected effective exchange rates, currency precision rules, balanced journals, payment allocation tables, settlement/reconciliation states, and audit trails.

### High

#### H1. Test coverage is effectively absent

One context-load test cannot validate 61 controllers and the principal business workflows. There are no frontend unit/component/E2E tests. Add Testcontainers integration tests, service tests for calculations, concurrency tests, API contract tests, and Playwright/Cypress critical journeys before feature expansion.

#### H2. Registration/setup wizard lacks a durable state machine

The frontend exposes `/setup` publicly and orchestrates multiple APIs. There is no single persisted step/version model proving idempotency, prerequisite enforcement, completion locking, retry behavior, or cleanup. Model onboarding statuses explicitly and expose a server-computed next step.

#### H3. Inventory does not have one auditable source of truth

Stock is directly mutated by multiple workflows. Introduce an inventory movement ledger with reason/reference, immutable deltas, balance projection, reservations, warehouse ownership checks, idempotency, and reconciliation. Use database locking/atomic conditional updates for availability.

#### H4. Purchase/sale/return lifecycles are inconsistent

Entities and services exist, but statuses, payment effects, stock effects, and reversal rules are not governed by a shared transition policy. Define allowed state machines and post inventory/accounting only on explicit transitions (receive, post, cancel, return, refund).

#### H5. Secrets and production defaults are unsafe

Docker Compose supplies default database/Redis/JWT/encryption credentials, enables extensive DEBUG logging, uses unencrypted Kafka/Redis traffic, exposes Ollama, and forces schema update. Remove credential defaults, fail fast on missing secrets, rotate existing secrets, add secret-manager integration, and use environment-specific logging.

#### H6. Public operational surfaces disclose too much

All actuator and media endpoints are permitted. Restrict health to liveness/readiness detail appropriate for unauthenticated callers; protect metrics/env/config; authorize media by tenant/entity and use signed URLs where needed.

#### H7. Error/API contracts are inconsistent

Services throw mixed `IllegalArgumentException`, `EntityNotFoundException`, custom exceptions, and null/unsupported responses. Standardize RFC 7807-style errors, validation field errors, correlation IDs, stable error codes, pagination, and idempotency conflict responses.

#### H8. Cloud/local storage implementations are incomplete

Multiple local/S3/Azure/GCS methods throw `UnsupportedOperationException`. Disable unsupported providers through capability validation or complete and integration-test every advertised operation.

### Medium

- The frontend API URL is hard-coded, so the Docker `VUE_APP_API_URL` value is ineffective at runtime/build time.
- JWT is access-token-only; no refresh-token rotation, revocation/token version, device/session management, or forced logout was found.
- `UserContext` performs repository lookup repeatedly and uses a static injected repository; put identity data in the authenticated principal.
- The frontend stores the bearer token and tenant/user data in local storage, increasing XSS impact. Adopt a deliberate token strategy and a strict CSP.
- Frontend production build warns about two invalid imports: `useCurrencySettingStore` and `ShipmentStatus`.
- The production entrypoint is approximately 2.85 MiB; routes are eagerly bundled and several assets exceed 1 MiB. Add route-level lazy loading, tree shaking, and asset optimization.
- Frontend services/stores duplicate loading/error conventions and frequently return null, weakening error handling and typing.
- Several implemented pages are unreachable because routes are commented out (transfers, quotations, shipments, and reports).
- CORS is code-hard-coded for localhost/ngrok patterns rather than environment-configured trusted origins.
- Kafka is single-broker with replication factor one and no demonstrated dead-letter, replay, schema evolution, or transactional outbox policy.
- Redis disables persistence but uses a named volume; clarify whether it is cache-only and ensure correctness never depends on it.
- Backup script exists, but restore drills, retention, encryption, offsite storage, RPO/RTO, and tenant-aware recovery are not documented or tested.
- No CI workflow was found to enforce build, lint, tests, dependency scanning, migrations, container scanning, or SBOM generation.

### Low

- Naming and package conventions are inconsistent (`sale` versus plural tables, `supplier`, `quotation`, `interf`, misspelled frontend page names).
- Comments and source text contain encoding artifacts.
- Backend build includes Kotlin plugins/dependencies although the application source is Java, increasing build complexity.
- Frontend uses aging Vue CLI/TypeScript tooling and emits deprecated selector/browser database warnings.
- Docker comments and actual Java version are inconsistent; JVM memory options in Compose are not used by the JSON-form entrypoint.

## Workflow assessment

| Workflow | Existing implementation | Readiness | Primary gap |
|---|---|---:|---|
| Company registration | Company, verification, user registration, plans/subscriptions | Critical | Split public calls; caller-selected company ID; no atomic/idempotent orchestration |
| Setup wizard | Vue wizard and configuration APIs | High | Client orchestration without durable server state/prerequisites |
| Products | Company product plus warehouse price/stock/tax | High | Unscoped relationship resolution and inconsistent completeness rules |
| Warehouses | Warehouse entities, currencies, settings, user assignment | Critical | Isolation/authorization not universally enforced |
| POS/sales | POS UI, sale calculation, stock validation, payment APIs | Critical | No atomic checkout; concurrency and trusted-client values |
| Purchases | Purchase, products, returns, payments | High | Receipt/payment/stock/accounting transition policy not unified |
| Returns/refunds | Sale/purchase returns and gateway handlers | Critical | Reversal/accounting behavior incomplete |
| Payments | Common payment entity/service plus strategies | Critical | Mutable/unscoped records and no authoritative allocation/ledger |
| Multi-currency | company/warehouse currencies and exchange rates | Critical | Client-supplied values; no complete posting/revaluation rules |
| Employees/RBAC | users, roles, permissions, warehouse assignments | Critical | Permission enforcement is primarily frontend-only |
| Reports/accounting | report pages/services and report utility | Critical | Reports cannot be trusted before ledger and transaction integrity |
| Online store | storefront views/routes/settings | High | Mostly UI scaffolding; authenticated shopping routes; no hardened order flow |
| Import/export | batch/Kafka jobs and format utilities | High | incomplete methods, tenant/replay/security tests absent |
| Storage/media | abstraction and four providers | High | public media surface and unimplemented provider operations |
| AI | provider/orchestrator/tools | Medium | unfinished streaming and governance; defer until core controls are stable |

## Prioritized implementation roadmap

### Phase 0 — Release containment and reproducible baseline

Goal: prevent unsafe deployment and make findings measurable.

- Freeze production release and document current data backups.
- Add CI for backend compile/test, frontend type/lint/build, secret/dependency scanning, and container build.
- Create schema baseline and change production Hibernate to `validate`.
- Remove unsafe credential defaults/debug logging/public actuator exposure.
- Add an API error contract and correlation IDs.

Validation: fresh environment starts from migrations; no secrets in image/repository; builds are warning-gated; health endpoints expose minimum data.

### Phase 1 — Identity, tenancy, and authorization

Goal: make cross-company access structurally impossible.

- Implement authenticated tenant principal/context.
- Convert every tenant repository/service operation to tenant-scoped access.
- Add server-side RBAC and warehouse assignment enforcement.
- Lock down onboarding/media/WebSocket/actuator endpoints.
- Add exhaustive cross-tenant and permission integration tests.

Migration: indexes beginning with `company_id`; tenant-inclusive unique constraints; remediation script for null/mismatched tenant IDs.

Exit gate: automated tests prove tenant A cannot reference any tenant B identifier across every controller family.

### Phase 2 — Registration and setup wizard

Goal: an idempotent company bootstrap.

- Pending-registration aggregate with verified email, selected plan/trial, payment state, and expiring onboarding token.
- Transactional bootstrap for company, admin, role, base currency, default settings, subscription, and default warehouse, or a persisted saga with compensation.
- Persist wizard steps/version and compute readiness server-side.
- Add retry/resume/expiry/duplicate-email/payment-failure journeys.

Exit gate: repeated requests yield one consistent company; failures leave no usable partial tenant.

### Phase 3 — Product-to-warehouse and inventory ledger

Goal: authoritative availability and auditable stock.

- Define “sellable” as active product plus active warehouse mapping with price, stock policy, tax, SKU/barcode, and availability.
- Add immutable inventory movements/reservations and atomic balance updates.
- Rework purchases, transfers, adjustments, sales, and returns to post movements idempotently.
- Add concurrency and reconciliation tests.

Exit gate: parallel checkout cannot oversell and every balance reconciles to movements.

### Phase 4 — POS checkout and centralized payments

Goal: complete a sale reliably in one command.

- Server-side pricing/tax/discount/promotion engine with explicit rounding and currency policy.
- Atomic/idempotent checkout orchestrator for sale, allocations, inventory, invoice/receipt, accounting outbox.
- Central payment lifecycle supporting cash/card/UPI/bank/wallet/gift/store credit and split tenders.
- Gateway webhooks verified, idempotent, replay-safe, and tenant-bound.

Exit gate: cash, split, credit, failed gateway, retry, refund, and offline/reconnect scenarios pass integration/E2E tests.

### Phase 5 — Purchases, returns, accounting, and multi-currency

Goal: auditable financial operations.

- Explicit purchase/order/receipt/invoice/payment state machines.
- Immutable double-entry journal and subledger allocations.
- Credit notes, refunds, reversals, exchange gains/losses, and reconciliation.
- Currency precision/effective-rate rules and base/transaction reporting.

Exit gate: every posted transaction balances; reversals preserve history; reports reconcile to ledgers.

### Phase 6 — Reporting, online store, and secondary modules

Goal: safely expose derived capabilities.

- Build reports from governed transaction/ledger sources.
- Connect storefront catalog to warehouse sellability and reuse checkout/payment services.
- Complete or disable unfinished storage/import/export/AI capabilities.
- Restore routes only when their backend workflow passes acceptance tests.

### Phase 7 — Production operations

Goal: deploy and operate predictably.

- HA topology, TLS, secret manager, Kafka/Redis hardening, object storage, rate limits/WAF.
- Structured logs, metrics, traces, alerting, SLOs, dashboards, and incident runbooks.
- Encrypted backups, point-in-time recovery, restore drills, retention, and disaster recovery.
- Load, soak, chaos, security, accessibility, and browser/device testing.

## Standard deliverable template for each implementation phase

Every module change should include:

1. Root cause and invariant being restored.
2. Exact backend/frontend/database/API files impacted.
3. Backward-compatible API and migration plan.
4. Unit, integration, contract, concurrency, security, and E2E tests as applicable.
5. Data validation/reconciliation checklist.
6. Observability and rollback plan.
7. Risks, trade-offs, and explicit exit criteria.

## Immediate next implementation slice

Start with Phase 1, not POS UI fixes. The first bounded slice should be **tenant-safe product/warehouse/POS read paths**:

- authenticated tenant principal;
- scoped product, warehouse, price, stock, and tax queries;
- server-side warehouse assignment checks;
- endpoint permission checks for product/POS access;
- cross-tenant integration tests;
- no checkout behavior changes yet.

This creates the security foundation required to diagnose and repair POS checkout without preserving unsafe data access patterns.
