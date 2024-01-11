
--
-- Structure for table geocodes_country
--

DROP TABLE IF EXISTS geocodes_country;
CREATE TABLE geocodes_country (
id_country int AUTO_INCREMENT,
code varchar(10) default '' NOT NULL,
value varchar(255) default '' NOT NULL,
is_attached SMALLINT DEFAULT 0,
PRIMARY KEY (id_country)
);

ALTER TABLE geocodes_country 
ADD INDEX IDX_COUNTRY_CODE (code ASC) ,
ADD INDEX IDX_COUNTRY_NAME (value ASC)  ;

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
PRIMARY KEY (id_city)
);

ALTER TABLE geocodes_city 
ADD INDEX IDX_CITY_CODE (code ASC) ,
ADD INDEX IDX_CITY_NAME (value ASC) ,
ADD INDEX IDX_COUNTRY_CODE_CITIES (code_country ASC) ;
