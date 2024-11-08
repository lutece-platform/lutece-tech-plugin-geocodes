ALTER TABLE geocodes_city 
ADD INDEX IDX_VALUE_MIN_CITY ( value_min ASC),
ADD INDEX IDX_VALUE_MIN_COMPLETE_CITY ( value_min_complete ASC);

update geocodes_country set date_validity_end = '1949-05-23' where code = 99109;
update geocodes_country set date_validity_end = '1945-11-29' where code = 99121;
update geocodes_country set date_validity_start = '1975-01-01' where code = 99235;

alter table geocodes_country add column value_min_complete varchar(255) default '';

INSERT INTO geocodes_country (code, value, date_validity_start, date_validity_end, value_min_complete) VALUES 
( '99141','ALLEMAGNE DE L''EST', '1949-05-23', '1990-10-03','' ),
( '99142','ALLEMAGNE DE L''OUEST', '1949-05-23', '1990-10-03','' ),
( '99109','ALLEMAGNE', '1990-10-03', '2999-12-31','' ),
( '99121','YOUGOSLAVIE', '1945-11-29', '1992-04-27','République fédérative socialiste de Yougoslavie' ),
( '99121','YOUGOSLAVIE', '1992-04-27', '2003-02-04','République fédérative de Yougoslavie' ), 
( '99121','SERBIE-ET-MONTENEGRO', '2003-02-04', '2006-06-08','' ),
( '99121','SERBIE', '2006-06-08', '2999-12-31','' );