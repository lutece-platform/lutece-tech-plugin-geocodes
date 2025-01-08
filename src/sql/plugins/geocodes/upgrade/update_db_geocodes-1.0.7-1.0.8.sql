ALTER TABLE geocodes_country
    ADD COLUMN value_min_complete VARCHAR(255);

WITH capitalized AS (SELECT CONCAT(
                                    UPPER(SUBSTRING(value, 1, 1)),
                                    LOWER(SUBSTRING(value, 2, LENGTH(value)))
                            ) AS capitalized_name, id_country
                     FROM geocodes_country AS geo)
UPDATE geocodes_country AS country SET value_min_complete = capitalized.capitalized_name
FROM  capitalized
WHERE capitalized.id_country = country.id_country;

ALTER TABLE geocodes_country_changes
    ADD COLUMN value_min_complete VARCHAR(255);

WITH capitalized AS (SELECT CONCAT(
                                    UPPER(SUBSTRING(value, 1, 1)),
                                    LOWER(SUBSTRING(value, 2, LENGTH(value)))
                            ) AS capitalized_name, id_country
                     FROM geocodes_country_changes AS geo)
UPDATE geocodes_country_changes AS country SET value_min_complete = capitalized.capitalized_name
FROM  capitalized
WHERE capitalized.id_country = country.id_country;