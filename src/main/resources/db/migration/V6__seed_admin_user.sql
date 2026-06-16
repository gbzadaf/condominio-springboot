INSERT INTO users (name, email, password, role, deleted, created_at, updated_at)
VALUES (
           'Admin',
           'admin@condoaccess.com',
           '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.',
           'ADMIN',
           FALSE,
           NOW(),
           NOW()
       );