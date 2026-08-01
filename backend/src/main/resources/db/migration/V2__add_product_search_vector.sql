-- flyway:executeInTransaction=false
-- Add tsvector column
ALTER TABLE products ADD COLUMN IF NOT EXISTS search_vector tsvector
GENERATED ALWAYS AS (
  setweight(to_tsvector('simple', coalesce(name, '')), 'A') ||
  setweight(to_tsvector('simple', coalesce(code, '')), 'B') ||
  setweight(to_tsvector('simple', coalesce(sku, '')), 'B')
) STORED;

-- Create indexes
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_products_search ON products USING GIN (search_vector);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_products_company_search ON products (company_id, search_vector);
