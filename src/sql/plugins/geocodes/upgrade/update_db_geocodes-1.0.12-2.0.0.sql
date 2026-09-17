-- liquibase formatted sql
-- changeset geocodes:update_db_geocodes-1.0.12-2.0.0.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA=database() AND TABLE_NAME='geocodes_country_changes' AND COLUMN_NAME='value_min_complete';
ALTER TABLE geocodes_country_changes ADD COLUMN value_min_complete varchar(255) default '' AFTER value;
