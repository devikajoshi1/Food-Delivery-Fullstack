-- Move the first 3 restaurants to Bangalore
UPDATE restaurants SET address = 'Indiranagar, Bangalore' WHERE id = 1;
UPDATE restaurants SET address = 'Koramangala, Bangalore' WHERE id = 2;
UPDATE restaurants SET address = 'Jayanagar, Bangalore'   WHERE id = 3;

-- 5 new restaurants
INSERT INTO restaurants (id, name, cuisine, address) VALUES
                         (4, 'Biryani House',  'Hyderabadi', 'HSR Layout, Bangalore'),
                        (5, 'Dragon Wok',     'Chinese',    'Whitefield, Bangalore'),
                         (6, 'Burger Barn',    'American',   'MG Road, Bangalore'),
                                                         (7, 'Udupi Kitchen',  'South Indian', 'Malleshwaram, Bangalore'),
                                                         (8, 'Sweet Tooth',    'Desserts',   'BTM Layout, Bangalore');

-- More dishes for the old restaurants, and a menu for each new one
INSERT INTO menu_items (restaurant_id, name, description, price) VALUES
                                                                     (1, 'Chole Bhature',    'Spicy chickpeas with fried bread', 180.00),
                                                                     (1, 'Lassi',            'Sweet yoghurt drink',               80.00),
                                                                     (2, 'Pasta Arrabiata',  'Penne in spicy tomato sauce',      279.00),
                                                                     (2, 'Garlic Bread',     NULL,                               129.00),
                                                                     (3, 'Rava Idli',        'With coconut chutney',              80.00),
                                                                     (3, 'Mysore Masala Dosa', 'With red chutney inside',        140.00),
                                                                     (4, 'Chicken Biryani',  'Dum-cooked with basmati rice',     320.00),
                                                                     (4, 'Veg Biryani',      'Mixed vegetables and rice',        250.00),
                                                                     (4, 'Mirchi ka Salan',  'Chilli and peanut curry',          120.00),
                                                                     (5, 'Veg Hakka Noodles', NULL,                              199.00),
                                                                     (5, 'Chilli Paneer',    'Paneer in spicy sauce',            229.00),
                                                                     (5, 'Fried Rice',       NULL,                               189.00),
                                                                     (6, 'Classic Burger',   'Veg patty, cheese, lettuce',       179.00),
                                                                     (6, 'Chicken Burger',   'Crispy chicken, mayo',             219.00),
                                                                     (6, 'French Fries',     NULL,                                99.00),
                                                                     (7, 'Bisi Bele Bath',   'Rice, lentils and spices',         110.00),
                                                                     (7, 'Vada (2 pcs)',     'With sambar',                       60.00),
                                                                     (7, 'Kesari Bath',      'Sweet semolina',                    50.00),
                                                                     (8, 'Gulab Jamun (2 pcs)', NULL,                             70.00),
                                                                     (8, 'Chocolate Brownie', 'With chocolate sauce',            120.00),
                                                                     (8, 'Mango Kulfi',      NULL,                                90.00);
