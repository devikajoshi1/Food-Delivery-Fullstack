-- password: demo1234
UPDATE users
SET password_hash =
    '$2a$10$HYdYHDAStYw5gEjlXjXBzeRLRxs8h60xkdLR9pTR8olLbdVkCZwq2'
WHERE email = 'demo@example.com';

-- password: admin1234
UPDATE users
SET password_hash =
    '$2a$10$Xr0IKPYMlhZIfUs3bhZC/OovSFuJpdRoyqLHUgTo7gqPdv1IKprci'
WHERE email = 'admin@example.com';
