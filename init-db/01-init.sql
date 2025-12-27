-- Create extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create additional users if needed
-- CREATE USER app_user WITH PASSWORD 'app_password';
-- GRANT ALL PRIVILEGES ON DATABASE nextpos TO app_user;

-- Create initial tables if they don't exist in your Spring Boot migration
-- (Your Spring Boot will create tables via JPA or Flyway)