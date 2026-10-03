-- password: demo1234
UPDATE users
SET password_hash = 'paste-your-demo-hash-here'
WHERE email = 'demo@example.com';

-- password: admin1234
INSERT INTO users (name, email, password_hash, role) VALUES
    ('Admin', 'admin@example.com',
     'paste-your-admin-hash-here',
     'ADMIN');