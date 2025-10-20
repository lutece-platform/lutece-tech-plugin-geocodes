
--
-- Structure for table geocodes_country
--

DROP TABLE IF EXISTS geocodes_country;
CREATE TABLE geocodes_country (
id_country int AUTO_INCREMENT,
code varchar(10) default '' NOT NULL,
value varchar(255) default '' NOT NULL,
is_attached SMALLINT DEFAULT 0,
date_validity_start DATE default '1900-01-01',
date_validity_end DATE default '2999-12-31',
deprecated SMALLINT DEFAULT 0,
value_min_complete varchar(255) default '',
PRIMARY KEY (id_country)
);

CREATE INDEX IDX_COUNTRY_CODE on geocodes_country ( code ASC );
CREATE INDEX IDX_COUNTRY_NAME on geocodes_country ( value ASC ); 


--
-- Structure for table geocodes_city
--

DROP TABLE IF EXISTS geocodes_city;
CREATE TABLE geocodes_city (
id_city int AUTO_INCREMENT,
code_country varchar(10) default '' NOT NULL,
code varchar(10) default '' NOT NULL,
code_zone varchar(10) default '' NOT NULL,
value varchar(255) default '' NOT NULL,
date_validity_start DATE default '1943-01-01',
date_validity_end DATE default '2999-12-31',
value_min varchar(255) default '' NOT NULL,
value_min_complete varchar(255) default '' NOT NULL,
date_last_update DATE default '1943-01-01',
deprecated SMALLINT DEFAULT 0,
PRIMARY KEY (id_city)
);

CREATE INDEX IDX_CITY_CODE on geocodes_city (code ASC) ;
CREATE INDEX IDX_CITY_NAME on geocodes_city (value ASC) ;
CREATE INDEX IDX_COUNTRY_CODE_CITIES on geocodes_city (code_country ASC);
CREATE INDEX IDX_VALUE_MIN_CITY on geocodes_city ( value_min ASC);
CREATE INDEX IDX_VALUE_MIN_COMPLETE_CITY on geocodes_city ( value_min_complete ASC);


DROP TABLE IF EXISTS geocodes_city_changes;
CREATE TABLE geocodes_city_changes (
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
	   PRIMARY KEY (code, date_validity_start)
);

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
