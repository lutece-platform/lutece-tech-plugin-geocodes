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