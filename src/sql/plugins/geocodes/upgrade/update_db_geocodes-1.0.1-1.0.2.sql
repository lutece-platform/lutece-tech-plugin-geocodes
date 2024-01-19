alter table geocodes_country add column is_attached SMALLINT DEFAULT 0;

update geocodes_country set is_attached = 1 where code = '99501' and value != 'AUSTRALIE';
update geocodes_country set is_attached = 1 where code = '99132' and value != 'ROYAUME-UNI';
update geocodes_country set is_attached = 1 where code = '99502' and value != 'NOUVELLE-ZELANDE';
update geocodes_country set is_attached = 1 where code = '99103' and value != 'NORVEGE';
update geocodes_country set is_attached = 1 where code = '99101' and value != 'DANEMARK';
update geocodes_country set is_attached = 1 where code = '99404' and value != 'ETATS-UNIS';
update geocodes_country set is_attached = 1 where code = '99223' and value != 'INDE';
update geocodes_country set is_attached = 1 where code = '99135' and value != 'PAYS-BAS';
update geocodes_country set is_attached = 1 where code = '99425' and value != 'TERRITOIRES DU ROYAUME-UNI AUX ANTILLES';
update geocodes_country set is_attached = 1 where code = '99505' and value != 'TERR. DES ETATS-UNIS D''AMERIQUE EN OCEANIE';
update geocodes_country set is_attached = 1 where code = '99313' and value != 'PROVINCES ESPAGNOLES D''AFRIQUE';
update geocodes_country set is_attached = 1 where code = '99432' and value != 'TERR. DES ETATS-UNIS D''AMERIQUE EN AMERIQUE';
update geocodes_country set is_attached = 1 where code = '99427' and value != 'TERR. DU ROYAUME-UNI DANS L''ATLANTIQUE SUD';
update geocodes_country set is_attached = 1 where code = '99308' and value != 'OCEAN INDIEN (TERRITOIRE BRITANNIQUE DE L'')';

alter table geocodes_country add column date_validity_start DATE default '1900-01-01'; 
alter table geocodes_country add column date_validity_end DATE default '2999-12-31';

INSERT INTO geocodes_country (code, value, date_validity_start, date_validity_end) VALUES 
( '91352','ALGERIE FRANCAISE (DEPT ALGER)', '1900-01-01', '1962-07-04' ),
( '92352','ALGERIE FRANCAISE (DEPT ORAN)', '1900-01-01', '1962-07-04' ),
( '93352','ALGERIE FRANCAISE (DEPT CONSTANTINE)', '1900-01-01', '1962-07-04' ),
( '94352','ALGERIE FRANCAISE (DEPT TERRITOIRES DU SUD)', '1900-01-01', '1962-07-04' ),
( '99143','CHYPRE (EMPIRE ROYAUME-UNI)', '1900-01-01', '1960-08-15' ), 
( '99146','ARMENIE (URSS)', '1900-01-01', '1991-09-20' );

update geocodes_country set date_validity_start = '1962-07-05' where code = '99352';
update geocodes_country set date_validity_start = '1960-08-16' where code = '99254';
update geocodes_country set date_validity_start = '1991-09-21' where code = '99252';
