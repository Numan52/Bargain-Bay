--parent categories are inserted first
INSERT INTO category (id, name, parent_id) VALUES
    (gen_random_uuid(), 'Electronics', NULL),
    (gen_random_uuid(), 'Vehicles', NULL),
    (gen_random_uuid(), 'Furniture', NULL),
    (gen_random_uuid(), 'Clothing', NULL),
    (gen_random_uuid(), 'Books', NULL);

-- Insert subcategories, referencing parent categories

INSERT INTO category (id, name, parent_id) VALUES
    (gen_random_uuid(), 'Laptops', (SELECT id FROM category WHERE name = 'Electronics')),
    (gen_random_uuid(), 'Smartphones', (SELECT id FROM category WHERE name = 'Electronics')),
    (gen_random_uuid(), 'Cameras', (SELECT id FROM category WHERE name = 'Electronics')),

    (gen_random_uuid(), 'Cars', (SELECT id FROM category WHERE name = 'Vehicles')),
    (gen_random_uuid(), 'Motorcycles', (SELECT id FROM category WHERE name = 'Vehicles')),
    (gen_random_uuid(), 'Bicycles', (SELECT id FROM category WHERE name = 'Vehicles')),

    (gen_random_uuid(), 'Chairs', (SELECT id FROM category WHERE name = 'Furniture')),
    (gen_random_uuid(), 'Tables', (SELECT id FROM category WHERE name = 'Furniture')),
    (gen_random_uuid(), 'Sofas', (SELECT id FROM category WHERE name = 'Furniture')),

    (gen_random_uuid(), 'Men', (SELECT id FROM category WHERE name = 'Clothing')),
    (gen_random_uuid(), 'Women', (SELECT id FROM category WHERE name = 'Clothing'));

-- Clothing subcategories

INSERT INTO category (id, name, parent_id) VALUES
    (gen_random_uuid(), 'Shirts', (SELECT id FROM category WHERE name = 'Men')),
    (gen_random_uuid(), 'Pants', (SELECT id FROM category WHERE name = 'Men')),
    (gen_random_uuid(), 'Shoes', (SELECT id FROM category WHERE name = 'Men')),

    (gen_random_uuid(), 'Dresses', (SELECT id FROM category WHERE name = 'Women')),
    (gen_random_uuid(), 'Pants', (SELECT id FROM category WHERE name = 'Women')),
    (gen_random_uuid(), 'Shoes', (SELECT id FROM category WHERE name = 'Women'));
