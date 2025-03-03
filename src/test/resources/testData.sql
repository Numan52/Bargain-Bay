
-- Insert User
INSERT INTO users (id, first_name, last_name, password, username, email)
VALUES (gen_random_uuid(), 'Martin', 'Smith', 'S+TjsXFbr/jBb5FmsrAbyQ==#8IP27uLRKfA/Ya60jLP1a1nDXWlF2pgeGDwQTGXboO4=', 'martin11', 'martin@gmail.com');

-- Insert Categories
INSERT INTO category (id, parent_id, name)
VALUES
    ('aefb83ad-4c9f-4c6a-b599-d8f67f4a1676', NULL, 'Electronics'),
    (gen_random_uuid(), 'aefb83ad-4c9f-4c6a-b599-d8f67f4a1676', 'Laptops'),
    (gen_random_uuid(), NULL, 'Books');

-- Insert Ads
INSERT INTO ad (id, title, price, description, condition, created_at, category_id, last_bumped_at, user_id, has_priority)
VALUES
    (gen_random_uuid(), 'Book1', 10, 'book description', 'new', NOW(),
     (SELECT id FROM category WHERE name = 'Books'), NOW(),
     (SELECT id FROM users WHERE username = 'martin11'), false),

    (gen_random_uuid(), 'Gaming laptop 1', 500, 'Gaming laptop description', 'new', NOW(),
     (SELECT id FROM category WHERE name = 'Laptops'), NOW(),
     (SELECT id FROM users WHERE username = 'martin11'), false),

    (gen_random_uuid(), 'Gaming laptop 2', 600, 'Gaming laptop description 2', 'new', NOW(),
     (SELECT id FROM category WHERE name = 'Laptops'), NOW(),
     (SELECT id FROM users WHERE username = 'martin11'), false),

    (gen_random_uuid(), 'Printer', 600, 'Printer description', 'new', NOW(),
     (SELECT id FROM category WHERE name = 'Electronics'), NOW(),
     (SELECT id FROM users WHERE username = 'martin11'), false);
