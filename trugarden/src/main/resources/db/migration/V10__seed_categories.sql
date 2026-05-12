WITH lvl1 AS (
INSERT INTO category (name, category_code, parent_category_id, level)
VALUES
    ('Plantas de Interior',            'PLANTAS-INTERIOR',      null, 1),
    ('Plantas de Exterior',            'PLANTAS-EXTERIOR',      null, 1),
    ('Suculentas y Cactus',            'SUCULENTAS-CACTUS',     null, 1),
    ('Plantas Aromáticas y Medicinales','AROMATICAS-MEDICINALES',null, 1),
    ('Árboles y Arbustos',             'ARBOLES-ARBUSTOS',      null, 1)
    RETURNING id, category_code
), lvl2 AS (
INSERT INTO category (name, category_code, parent_category_id, level)
SELECT sub.name, sub.category_code, lvl1.id, 2
FROM lvl1
    JOIN (VALUES
    ('PLANTAS-INTERIOR',       'Plantas Colgantes',       'INTERIOR-COLGANTES'),
    ('PLANTAS-INTERIOR',       'Plantas de Sombra',       'INTERIOR-SOMBRA'),
    ('PLANTAS-INTERIOR',       'Plantas Purificadoras',   'INTERIOR-PURIFICADORAS'),
    ('PLANTAS-INTERIOR',       'Plantas Tropicales',      'INTERIOR-TROPICALES'),
    ('PLANTAS-EXTERIOR',       'Plantas de Jardín',       'EXTERIOR-JARDIN'),
    ('PLANTAS-EXTERIOR',       'Plantas de Balcón',       'EXTERIOR-BALCON'),
    ('PLANTAS-EXTERIOR',       'Plantas Acuáticas',       'EXTERIOR-ACUATICAS'),
    ('SUCULENTAS-CACTUS',      'Suculentas',              'SUCULENTAS'),
    ('SUCULENTAS-CACTUS',      'Cactus',                  'CACTUS'),
    ('AROMATICAS-MEDICINALES', 'Hierbas Culinarias',      'HIERBAS-CULINARIAS'),
    ('AROMATICAS-MEDICINALES', 'Plantas Medicinales',     'PLANTAS-MEDICINALES'),
    ('ARBOLES-ARBUSTOS',       'Árboles Frutales',        'ARBOLES-FRUTALES'),
    ('ARBOLES-ARBUSTOS',       'Arbustos Ornamentales',   'ARBUSTOS-ORNAMENTALES')
    ) AS sub(parent_code, name, category_code) ON lvl1.category_code = sub.parent_code
    RETURNING id, category_code
), lvl3 AS (
INSERT INTO category (name, category_code, parent_category_id, level)
SELECT sub.name, sub.category_code, lvl2.id, 3
FROM lvl2
    JOIN (VALUES
    ('INTERIOR-COLGANTES',    'Pothos',               'COLGANTES-POTHOS'),
    ('INTERIOR-COLGANTES',    'Helechos Colgantes',   'COLGANTES-HELECHOS'),
    ('INTERIOR-COLGANTES',    'Tradescantia',         'COLGANTES-TRADESCANTIA'),
    ('INTERIOR-SOMBRA',       'Helechos de Sombra',   'SOMBRA-HELECHOS'),
    ('INTERIOR-SOMBRA',       'Calatheas',            'SOMBRA-CALATHEAS'),
    ('INTERIOR-SOMBRA',       'Aglaonemas',           'SOMBRA-AGLAONEMAS'),
    ('INTERIOR-PURIFICADORAS','Espatifilos',          'PURIF-ESPATIFILOS'),
    ('INTERIOR-PURIFICADORAS','Sansevierias',         'PURIF-SANSEVIERIAS'),
    ('INTERIOR-PURIFICADORAS','Dracenas',             'PURIF-DRACENAS'),
    ('INTERIOR-TROPICALES',   'Monsteras',            'TROPICAL-MONSTERAS'),
    ('INTERIOR-TROPICALES',   'Heliconias',           'TROPICAL-HELICONIAS'),
    ('INTERIOR-TROPICALES',   'Bromelias',            'TROPICAL-BROMELIAS'),
    ('EXTERIOR-JARDIN',       'Rosas',                'JARDIN-ROSAS'),
    ('EXTERIOR-JARDIN',       'Hortensias',           'JARDIN-HORTENSIAS'),
    ('EXTERIOR-JARDIN',       'Begonias',             'JARDIN-BEGONIAS'),
    ('EXTERIOR-BALCON',       'Geranios',             'BALCON-GERANIOS'),
    ('EXTERIOR-BALCON',       'Petunias',             'BALCON-PETUNIAS'),
    ('EXTERIOR-BALCON',       'Lavanda',              'BALCON-LAVANDA'),
    ('SUCULENTAS',            'Echeverias',           'SUCU-ECHEVERIAS'),
    ('SUCULENTAS',            'Aloes',                'SUCU-ALOES'),
    ('SUCULENTAS',            'Haworthias',           'SUCU-HAWORTHIAS'),
    ('CACTUS',                'Cactus Columnares',    'CACTUS-COLUMNARES'),
    ('CACTUS',                'Cactus Globosos',      'CACTUS-GLOBOSOS'),
    ('CACTUS',                'Cactus Epífitos',      'CACTUS-EPIFITOS'),
    ('HIERBAS-CULINARIAS',    'Albahaca',             'CULIN-ALBAHACA'),
    ('HIERBAS-CULINARIAS',    'Menta y Hierbabuena',  'CULIN-MENTA'),
    ('HIERBAS-CULINARIAS',    'Romero y Tomillo',     'CULIN-ROMERO-TOMILLO'),
    ('PLANTAS-MEDICINALES',   'Sábila',               'MED-SABILA'),
    ('PLANTAS-MEDICINALES',   'Manzanilla',           'MED-MANZANILLA'),
    ('PLANTAS-MEDICINALES',   'Valeriana',            'MED-VALERIANA'),
    ('ARBOLES-FRUTALES',      'Cítricos',             'FRUTALES-CITRICOS'),
    ('ARBOLES-FRUTALES',      'Aguacate',             'FRUTALES-AGUACATE'),
    ('ARBUSTOS-ORNAMENTALES', 'Buganvillas',          'ARBUS-BUGANVILLAS'),
    ('ARBUSTOS-ORNAMENTALES', 'Azaleas',              'ARBUS-AZALEAS')
    ) AS sub(parent_code, name, category_code) ON lvl2.category_code = sub.parent_code
    RETURNING id
)
SELECT 1;