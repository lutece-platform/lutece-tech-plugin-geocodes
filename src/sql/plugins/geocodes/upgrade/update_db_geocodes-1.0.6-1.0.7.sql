DROP TABLE IF EXISTS geocodes_city_changes;
CREATE TABLE geocodes_city_changes (
    id_city_history SERIAL,
    id_city int DEFAULT 0,
    code_country varchar(10) default '' NOT NULL,
    code varchar(10) default '' NOT NULL,
    code_zone varchar(10) default '' NOT NULL,
    value varchar(255) default '' NOT NULL,
    date_validity_start DATE default '1943-01-01',
    date_validity_end DATE default '2999-12-31',
    value_min varchar(255) default '' NOT NULL,
    value_min_complete varchar(255) default '' NOT NULL,
    deprecated SMALLINT DEFAULT 0,
    date_update DATE default '1943-01-01',
    author varchar(255) default '' NOT NULL,
    status varchar(50) DEFAULT '' NOT NULL,
    PRIMARY KEY (id_city_history, id_city)
);

ALTER TABLE geocodes_city_changes
    ADD CONSTRAINT fk_id_city FOREIGN KEY (id_city) REFERENCES geocodes_city (id_city);

DROP TABLE IF EXISTS geocodes_country_changes;
CREATE TABLE geocodes_country_changes (
    id_country_history SERIAL,
    id_country int DEFAULT 0,
    code varchar(10) default '' NOT NULL,
    value varchar(255) default '' NOT NULL,
    is_attached SMALLINT DEFAULT 0,
    date_validity_start DATE default '1900-01-01',
    date_validity_end DATE default '2999-12-31',
    deprecated SMALLINT DEFAULT 0,
    date_update DATE default '1943-01-01',
    author varchar(255) default '' NOT NULL,
    status varchar(50) DEFAULT '' NOT NULL,
    PRIMARY KEY (id_country_history, id_country)
);

ALTER TABLE geocodes_country_changes
    ADD CONSTRAINT fk_id_country FOREIGN KEY (id_country) REFERENCES geocodes_country (id_country);