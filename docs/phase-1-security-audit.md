# Phase 1 Security Audit and Completion Report

Date: 2026-08-01  
Scope: Multi-tenant security and authorization only. Phase 2 work was not started.

## 1. Executive summary

Phase 1 is complete and production-ready for its defined security scope. The audit covered all 54 REST controllers (221 mapped endpoint methods), service entry points, repositories, asynchronous import/export consumers, media access, scanner WebSocket traffic, frontend permission metadata, and database migrations.

The security boundary is now fail-closed. Authenticated tenant identity comes from the database-backed principal; tenant request values are ignored, overwritten, or rejected on mismatch. Tenant-owned records and indirect references use company-qualified resolution. Warehouse-qualified workflows pass through the shared warehouse ownership/assignment boundary. No Critical or High severity security findings remain open.

## 2. Modules reviewed

- Identity, users, roles, permissions, company ownership, and company subscriptions
- Registration and the minimal signed onboarding context prerequisite
- Products, categories, brands, units, product stock, prices, and taxes
- Warehouses, warehouse currencies, inventory adjustments, and stock transfers
- POS, barcode scanner HTTP endpoints, and scanner STOMP/WebSocket traffic
- Sales, sale returns, purchases, purchase returns, quotations, and payments
- Customers, suppliers, expenses, promotions, discounts, and loyalty
- Media, import/export jobs, local/cloud storage access, and indirect media references
- Security, branding, SMTP, tax, shipping-provider, payment-gateway, exchange-rate, social-media, online-ordering, and POS settings
- Verification and administrative endpoints
- Reports: controller/service/report-generator source files are empty placeholders and expose no implemented endpoint or data-access surface
- Online Store: settings and existing authenticated preview routes were audited; public commerce functionality remains intentionally deferred to Phase 7

## 3. Repository audit

The initial generic-operation inventory was 236. The final inventory is 63, with every remaining occurrence classified:

| Classification | Count | Repositories and justification |
|---|---:|---|
| Tenant-protected company-root lookup | 21 | `CompanyRepository` lookups receive the authenticated company, a signed onboarding company, or an already validated company ID. `Company` is the tenant root, so a second company scope column does not exist. Authenticated path IDs are rejected on mismatch. |
| Global reference data | 32 | Currency (23), permission (4), and subscription-plan (5) operations. These entities are intentionally shared reference/configuration data; mutations remain permission-protected. |
| Internal/system-only | 10 | Verification lifecycle (5), Kafka import/export job resolution (2), persisted-job/system mail company resolution (2), and post-create company refresh (1). Job-linked media is independently company-qualified. |
| Legacy requiring refactoring | 0 | No reviewed tenant-sensitive legacy generic operation remains. |

Tenant-sensitive repositories are protected by `TenantRepositoryArchitectureTest`, which fails the build if known tenant repositories use generic `findById`, `findAll`, `existsById`, `deleteById`, `getReferenceById`, or `findAllById` operations outside repository declarations.

Final unsecured tenant-sensitive generic-operation count: **0**.

## 4. RBAC audit

- All `/api/**` requests pass through `BusinessApiAuthorizationManager` after a small explicit public allowlist.
- Unknown API paths fail closed. Non-API requests now also deny by default, except documented public resources and the WebSocket handshake.
- `BusinessPermissionRulesTest` discovers every `@RestController` and mapped method and requires a backend permission rule.
- Business-effect routes that use POST for bulk delete, bulk status update, import, or export have action-specific permissions rather than inheriting generic POST/create semantics.
- Existing method-level `@PreAuthorize` checks remain active and add narrower checks where present.
- Scanner STOMP CONNECT frames require a valid JWT and database-backed principal; SEND/SUBSCRIBE require `SALE_POS`; topic subscriptions must match the authenticated user and an assigned warehouse (company owners retain intended tenant-wide access).
- Frontend route permission names are checked against the backend `PermissionType` enum. Warehouse and online-preview mappings were corrected. Frontend checks remain usability controls, not the security boundary.

Public endpoints are intentional and limited to onboarding company creation, signed onboarding company-currency operations, login/registration, verification lifecycle, global currency/subscription-plan reads, Razorpay webhook verification, health, API documentation, and the WebSocket handshake (whose STOMP CONNECT is authenticated separately).

## 5. Endpoint and tenancy audit

- `AuthenticatedUser` carries authoritative user, company, role/permissions, default warehouse, and active warehouse assignments loaded from the database.
- Services derive tenant identity from `UserContext`; client company identifiers are overwritten or compared with the authenticated company and rejected on mismatch.
- The unauthenticated registration sequence is bound to one pending company and email by a signed, short-lived onboarding token.
- Tenant owner roles are created/resolved by `(company_id, role_name)`; registration no longer selects a role globally by name.
- Related products, categories, brands, units, customers, suppliers, sales, purchases, returns, warehouses, media, and settings are resolved inside the authenticated tenant before association or mutation.
- Warehouse reads and mutations use `WarehouseAccessService`; non-owners require an active assignment. Existing-record warehouse checks occur before payment mutation/deletion/read, and sale-linked payments reject mismatched warehouse associations.
- Cache keys that previously risked tenant overlap now include company scope.
- Import/export consumers treat Kafka job IDs as internal identifiers and independently verify that referenced media belongs to the persisted job company.

## 6. Database migrations

- `V1__baseline_schema.sql` supports the declared PostgreSQL 15 production image (a PostgreSQL 17-only dump setting was removed).
- `V2__add_product_search_vector.sql` remains compatible with the baseline.
- `V3__tenant_role_name_uniqueness.sql` fails on duplicate case-insensitive role names inside one tenant, removes the legacy global role-name constraint, and creates `uk_roles_company_name_ci` on `(company_id, lower(name))`.
- Fresh verification: V1 -> V2 -> V3 applied with `ON_ERROR_STOP=1` to a clean PostgreSQL 15 database.
- Upgrade verification: a separate legacy-baseline database applied V1, then V2/V3 successfully.
- Both paths were queried to confirm the tenant index exists and the old global `UNIQUE(name)` constraint is absent. Disposable databases were removed after verification.
- Production compose uses Flyway with `ddl-auto=validate`, `baseline-on-migrate=true`, and baseline version 1 for existing deployments.

## 7. Automated verification

Final clean backend command:

`gradlew.bat clean test --no-daemon --console=plain`

Result: **BUILD SUCCESSFUL**, 27 tests, 0 failures, 0 errors, 0 skipped.

Coverage includes authoritative principal derivation, company ownership, signed onboarding expiry/tampering, tenant role isolation, tenant-qualified product and purchase references, payment indirect references and warehouse integrity, warehouse owner/assignment behavior, fail-closed permission mappings, frontend/backend permission consistency, unsafe repository architecture rules, WebSocket negative authorization, and an H2 persistence integration test proving direct and indirect media references cannot cross companies.

Final frontend command:

`npm.cmd run build`

Result: **production build successful**. The build retains four pre-existing warning groups: two invalid-export warnings and two asset/entrypoint size warnings, plus existing Browserslist/Vue/Node deprecation notices. Phase 1 introduced no new warning category.

No JaCoCo/Istanbul line-coverage threshold is configured, so numerical line coverage is not claimed.

## 8. Files, migrations, and commits

Phase 1 changed 124 tracked files across security configuration/context, controllers, repositories, services, import/export, migrations, tests, compose configuration, onboarding client code, scanner client code, and router permissions. The authoritative file list is the Git diff from the parent of commit `1fb60be` through the Phase 1 completion commit.

Migrations changed:

- `backend/src/main/resources/db/migration/V1__baseline_schema.sql`
- `backend/src/main/resources/db/migration/V2__add_product_search_vector.sql`
- `backend/src/main/resources/db/migration/V3__tenant_role_name_uniqueness.sql`

Phase 1 was delivered as 32 small commits beginning at `1fb60be`, including the closing audit and persistence-integration evidence. Each security module was committed as a separate reviewable slice.

## 9. Performance and compatibility impact

- Tenant-qualified queries generally reduce scanned rows and cache collision risk. Existing company/warehouse indexes are reused; V3 adds one small case-insensitive tenant-role index.
- Warehouse authorization adds bounded database/principal checks at transactional boundaries. Owners use tenant-wide access; assigned users filter to principal warehouse IDs.
- Backward-compatible company headers/paths are accepted only where needed and rejected on mismatch. Signed onboarding replaces raw company identity for unauthenticated continuation.
- Scanner clients must now send the existing JWT in STOMP CONNECT headers. Unauthorized or cross-user subscriptions are intentionally incompatible.
- No business feature was removed or rewritten.

## 10. Accepted technical debt and recommendation

Accepted non-security debt:

- Report controller/service/generator files are placeholders; report functionality must be implemented in its scheduled phase before reports can be considered feature-complete.
- Public online-store business APIs are not implemented; this remains Phase 7 scope. Existing settings and preview routes are secured.
- The frontend has pre-existing invalid-export, bundle-size, Browserslist, Vue deep-selector, and Node deprecation warnings. They do not weaken the Phase 1 backend security boundary but should be resolved in product hardening.
- Gradle reports existing Gradle 9 deprecation usage, and `MailService` uses a deprecated API.
- The current permission vocabulary has no separate payment refund or generic export permission; implemented endpoints use the closest existing least-privilege business permission. Add finer-grained permissions when those business operations are introduced.

There are no accepted Critical or High severity security findings. Phase 1 is production-ready for multi-tenant security and authorization, and the project is ready to begin Phase 2 when explicitly authorized.
