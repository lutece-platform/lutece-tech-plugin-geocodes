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