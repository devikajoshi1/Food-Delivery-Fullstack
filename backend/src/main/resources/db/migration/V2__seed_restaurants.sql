INSERT INTO restaurants (id, name, cuisine, address) VALUES
                                                         (1, 'Punjab Grill',   'North Indian', 'Koregaon Park, Pune'),
                                                         (2, 'Pizza Co.',      'Italian',      'Baner, Pune'),
                                                         (3, 'Dosa Junction',  'South Indian', 'Kothrud, Pune');

INSERT INTO menu_items (restaurant_id, name, description, price) VALUES
                                                                     (1, 'Paneer Tikka',   'Char-grilled cottage cheese', 250.00),
                                                                     (1, 'Dal Makhani',    'Slow-cooked black lentils',   220.00),
                                                                     (1, 'Butter Naan',    NULL,                           45.00),
                                                                     (2, 'Margherita',     'Tomato, mozzarella, basil',   299.00),
                                                                     (2, 'Farmhouse',      'Onion, capsicum, mushroom',   349.00),
                                                                     (3, 'Masala Dosa',    'With sambar and chutney',     120.00),
                                                                     (3, 'Idli (2 pcs)',   NULL,                           60.00),
                                                                     (3, 'Filter Coffee',  NULL,                           40.00);
