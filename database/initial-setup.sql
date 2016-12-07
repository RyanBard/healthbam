CREATE USER healthbam_sql PASSWORD 'changeit';
CREATE DATABASE healthbam_db WITH OWNER healthbam_sql;

\connect healthbam_db

\i database/initial-schema.sql
\i database/initial-data.sql

GRANT SELECT ON ALL SEQUENCES IN SCHEMA public TO healthbam_sql;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO healthbam_sql;
