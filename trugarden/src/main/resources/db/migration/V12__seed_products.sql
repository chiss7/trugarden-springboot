WITH inserted_products AS (
INSERT INTO product (
    name, slug, description,
    original_price, unit_price, discount_percentage,
    stock, lead_time_min_days, lead_time_max_days,
    product_type, has_iva, iva_percentage,
    num_ratings, category_id, created_date, created_by
)
SELECT
    p.name, p.slug, p.description,
    p.original_price, p.unit_price, p.discount_percentage,
    p.stock, p.lead_time_min_days, p.lead_time_max_days,
    p.product_type, p.has_iva, p.iva_percentage,
    p.num_ratings, c.id, now(), 1
FROM (VALUES
          (
              'Monstera Deliciosa',
              'monstera-deliciosa',
              'Icónica planta tropical de interior con sus características hojas perforadas. Perfecta para espacios luminosos.',
              35.00, 35.00, 0.0,
              0.0, 7, 14,
              'MADE_TO_ORDER', false, 0,
              0, 'TROPICAL-MONSTERAS'
          ),
          (
              'Echeveria Elegans',
              'echeveria-elegans',
              'Suculenta roseta de color azul-verdoso, ideal para escritorios y jardines de roca. Muy fácil de cuidar.',
              12.00, 12.00, 0.0,
              0.0, 5, 10,
              'MADE_TO_ORDER', false, 0,
              0, 'SUCU-ECHEVERIAS'
          ),
          (
              'Albahaca Genovesa',
              'albahaca-genovesa',
              'Hierba culinaria aromática de hojas grandes y sabor intenso. Perfecta para cocina y huertos urbanos.',
              8.00, 8.00, 0.0,
              0.0, 3, 7,
              'MADE_TO_ORDER', false, 0,
              0, 'CULIN-ALBAHACA'
          )
     ) AS p(name, slug, description,
            original_price, unit_price, discount_percentage,
            stock, lead_time_min_days, lead_time_max_days,
            product_type, has_iva, iva_percentage,
            num_ratings, category_code)
         JOIN category c ON c.category_code = p.category_code
    RETURNING id, slug
)
INSERT INTO product_entity_images (product_entity_id, images)
SELECT p.id, img.url
FROM inserted_products p
         JOIN (VALUES
                   ('monstera-deliciosa',  'https://pub-0ba768f93334489a80d7fdc0ac6ed523.r2.dev/products/770dc980-41fa-4ab8-816d-3874a576440f.jpeg'),
                   ('echeveria-elegans',   'https://pub-0ba768f93334489a80d7fdc0ac6ed523.r2.dev/products/dd3bbe4d-2e2b-4791-bb6a-0f00f7fb5de7.png'),
                   ('albahaca-genovesa',   'https://pub-0ba768f93334489a80d7fdc0ac6ed523.r2.dev/products/f2a3095d-25ce-4e0d-a892-a4ffcfefbda5.png')
) AS img(slug, url) ON p.slug = img.slug;