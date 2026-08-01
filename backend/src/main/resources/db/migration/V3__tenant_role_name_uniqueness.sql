-- Replace the unsafe global role-name constraint with tenant-specific uniqueness.
-- Fail explicitly if legacy data already contains duplicates within one tenant.
DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM roles
        GROUP BY company_id, lower(name)
        HAVING count(*) > 1
    ) THEN
        RAISE EXCEPTION 'Cannot migrate roles: duplicate role names exist within a company';
    END IF;
END $$;

DO $$
DECLARE
    constraint_name text;
BEGIN
    SELECT c.conname INTO constraint_name
    FROM pg_constraint c
    JOIN pg_attribute a
      ON a.attrelid = c.conrelid
     AND a.attnum = ANY (c.conkey)
    WHERE c.conrelid = 'roles'::regclass
      AND c.contype = 'u'
    GROUP BY c.conname
    HAVING array_agg(a.attname::text ORDER BY a.attname::text) = ARRAY['name']::text[];

    IF constraint_name IS NOT NULL THEN
        EXECUTE format('ALTER TABLE roles DROP CONSTRAINT %I', constraint_name);
    END IF;
END $$;

CREATE UNIQUE INDEX IF NOT EXISTS uk_roles_company_name_ci
    ON roles (company_id, lower(name));
