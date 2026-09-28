-- Initial Seed Data for Database Verification Tests

MERGE INTO USERS (username, email, full_name, status, role) KEY(username) VALUES 
('standard_user', 'standard_user@example.com', 'Standard Customer', 'ACTIVE', 'CUSTOMER'),
('problem_user', 'problem_user@example.com', 'Problem Customer', 'ACTIVE', 'CUSTOMER'),
('performance_glitch_user', 'glitch_user@example.com', 'Glitch Customer', 'ACTIVE', 'CUSTOMER');

MERGE INTO PRODUCTS (sku, name, category, price, stock_quantity) KEY(sku) VALUES 
('SKU-BACKPACK', 'Sauce Labs Backpack', 'Apparel', 29.99, 50),
('SKU-BIKELIGHT', 'Sauce Labs Bike Light', 'Accessories', 9.99, 100),
('SKU-BOLTT-SHIRT', 'Sauce Labs Bolt T-Shirt', 'Apparel', 15.99, 75),
('SKU-FLEECE-JACKET', 'Sauce Labs Fleece Jacket', 'Apparel', 49.99, 30),
('SKU-ONESIE', 'Sauce Labs Onesie', 'Apparel', 7.99, 80),
('SKU-RED-TSHIRT', 'Test.allTheThings() T-Shirt (Red)', 'Apparel', 15.99, 45);
