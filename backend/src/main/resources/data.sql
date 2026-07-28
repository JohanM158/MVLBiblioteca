-- ============================================
-- Datos de Prueba - Sistema de Biblioteca
-- Compatible con MySQL
-- ============================================

-- LIBROS
INSERT IGNORE INTO books (id, title, author, isbn, genre, year, description, available, created_at, updated_at) VALUES
(1, 'Cien años de soledad', 'Gabriel García Márquez', '978-0307474728', 'Realismo Mágico', 1967, 'La obra maestra del Nobel colombiano que narra la historia de la familia Buendía en Macondo.', true, NOW(), NOW()),
(2, 'Don Quijote de la Mancha', 'Miguel de Cervantes', '978-8420412146', 'Novela', 1605, 'La primera novela moderna, las aventuras del ingenioso hidalgo.', true, NOW(), NOW()),
(3, 'El Principito', 'Antoine de Saint-Exupéry', '978-0156012195', 'Fantasía', 1943, 'Un clásico de la literatura que habla sobre la amistad y el amor.', false, NOW(), NOW()),
(4, '1984', 'George Orwell', '978-0451524935', 'Ciencia Ficción', 1949, 'Una distopía sobre el totalitarismo y la vigilancia masiva.', true, NOW(), NOW()),
(5, 'Rayuela', 'Julio Cortázar', '978-8437604572', 'Novela', 1963, 'Una novela que se puede leer de múltiples formas, revolucionaria en su estructura.', true, NOW(), NOW()),
(6, 'La Casa de los Espíritus', 'Isabel Allende', '978-0553383805', 'Realismo Mágico', 1982, 'Saga familiar que abarca cuatro generaciones en Chile.', true, NOW(), NOW()),
(7, 'Pedro Páramo', 'Juan Rulfo', '978-8437604183', 'Realismo Mágico', 1955, 'Un joven viaja a Comala en busca de su padre y descubre un pueblo de fantasmas.', false, NOW(), NOW()),
(8, 'El Aleph', 'Jorge Luis Borges', '978-0142437889', 'Cuento', 1949, 'Colección de cuentos del maestro argentino de la literatura fantástica.', true, NOW(), NOW()),
(9, 'Ficciones', 'Jorge Luis Borges', '978-0802130303', 'Cuento', 1944, 'Cuentos que exploran laberintos, infinitos y realidades alternas.', true, NOW(), NOW()),
(10, 'La Sombra del Viento', 'Carlos Ruiz Zafón', '978-0143034902', 'Misterio', 2001, 'Un joven descubre un libro maldito en el Cementerio de los Libros Olvidados.', true, NOW(), NOW()),
(11, 'Crónica de una Muerte Anunciada', 'Gabriel García Márquez', '978-1400034956', 'Novela', 1981, 'Todo un pueblo sabía que iban a matar a Santiago Nasar.', true, NOW(), NOW()),
(12, 'El Túnel', 'Ernesto Sabato', '978-8432248221', 'Novela', 1948, 'La obsesión de un pintor por una mujer que contempla su cuadro.', true, NOW(), NOW()),
(13, 'Fahrenheit 451', 'Ray Bradbury', '978-1451673319', 'Ciencia Ficción', 1953, 'Un futuro donde los bomberos queman libros en lugar de apagar incendios.', true, NOW(), NOW()),
(14, 'El Amor en los Tiempos del Cólera', 'Gabriel García Márquez', '978-0307389732', 'Novela', 1985, 'Una historia de amor que espera más de cincuenta años para realizarse.', true, NOW(), NOW()),
(15, 'Cuentos de Eva Luna', 'Isabel Allende', '978-0060951313', 'Cuento', 1989, 'Veintitrés relatos que celebran la pasión y la aventura.', true, NOW(), NOW());

-- USUARIOS
INSERT IGNORE INTO users (id, first_name, last_name, email, phone, membership_id, registration_date, active) VALUES
(1, 'María', 'González', 'maria.gonzalez@email.com', '+57 300 1234567', 'MEM-001', '2024-01-15', true),
(2, 'Carlos', 'Rodríguez', 'carlos.rodriguez@email.com', '+57 301 2345678', 'MEM-002', '2024-02-20', true),
(3, 'Ana', 'Martínez', 'ana.martinez@email.com', '+57 302 3456789', 'MEM-003', '2024-03-10', true),
(4, 'Luis', 'Hernández', 'luis.hernandez@email.com', '+57 303 4567890', 'MEM-004', '2024-04-05', true),
(5, 'Sofía', 'López', 'sofia.lopez@email.com', '+57 304 5678901', 'MEM-005', '2024-05-12', true),
(6, 'Diego', 'Torres', 'diego.torres@email.com', '+57 305 6789012', 'MEM-006', '2024-06-18', false),
(7, 'Valentina', 'Ramírez', 'valentina.ramirez@email.com', '+57 306 7890123', 'MEM-007', '2024-07-22', true),
(8, 'Andrés', 'Vargas', 'andres.vargas@email.com', '+57 307 8901234', 'MEM-008', '2024-08-30', true);

-- PRÉSTAMOS
INSERT IGNORE INTO loans (id, book_id, user_id, loan_date, due_date, return_date, status, notes) VALUES
(1, 1, 1, '2025-01-10', '2025-01-24', '2025-01-22', 'RETURNED', 'Devuelto a tiempo'),
(2, 2, 2, '2025-01-15', '2025-01-29', '2025-01-28', 'RETURNED', NULL),
(3, 4, 1, '2025-02-01', '2025-02-15', '2025-02-14', 'RETURNED', 'Excelente lectura'),
(4, 5, 3, '2025-02-10', '2025-02-24', '2025-02-23', 'RETURNED', NULL),
(5, 1, 3, '2025-03-01', '2025-03-15', '2025-03-14', 'RETURNED', NULL),
(6, 8, 4, '2025-03-05', '2025-03-19', '2025-03-18', 'RETURNED', NULL),
(7, 9, 5, '2025-03-15', '2025-03-29', '2025-03-28', 'RETURNED', NULL),
(8, 10, 1, '2025-04-01', '2025-04-15', '2025-04-14', 'RETURNED', 'Fascinante misterio'),
(9, 2, 4, '2025-04-10', '2025-04-24', '2025-04-22', 'RETURNED', NULL),
(10, 11, 5, '2025-04-20', '2025-05-04', '2025-05-03', 'RETURNED', NULL),
(11, 1, 2, '2025-05-01', '2025-05-15', '2025-05-14', 'RETURNED', NULL),
(12, 13, 7, '2025-05-10', '2025-05-24', '2025-05-23', 'RETURNED', NULL),
(13, 4, 3, '2025-06-01', '2025-06-15', '2025-06-14', 'RETURNED', NULL),
(14, 12, 1, '2025-06-10', '2025-06-24', '2025-06-23', 'RETURNED', NULL),
(15, 14, 8, '2025-06-15', '2025-06-29', '2025-06-28', 'RETURNED', NULL),
(16, 1, 5, '2025-07-01', '2025-07-15', '2025-07-14', 'RETURNED', 'Cuarta vez que lo leo'),
(17, 3, 2, '2025-07-15', '2025-07-29', NULL, 'ACTIVE', 'Primer préstamo de este libro'),
(18, 7, 4, '2025-07-18', '2025-08-01', NULL, 'ACTIVE', NULL),
(19, 3, 1, '2025-06-01', '2025-06-15', NULL, 'ACTIVE', 'Pendiente de devolución');
