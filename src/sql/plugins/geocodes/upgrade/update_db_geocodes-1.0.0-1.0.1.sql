alter table geocodes_city add column date_validity_start DATE default '1943-01-01'; 
alter table geocodes_city add column date_validity_end DATE default '2999-12-31';
alter table geocodes_city add column value_min varchar(255) default '' NOT NULL;
alter table geocodes_city add column value_min_complete varchar(255) default '' NOT NULL;
alter table geocodes_city add column date_last_update DATE default '1943-01-01';
