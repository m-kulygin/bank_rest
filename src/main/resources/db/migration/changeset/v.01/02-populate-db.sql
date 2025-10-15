INSERT INTO bank_user(login, password, first_name, last_name, role)
VALUES
    ('user1', '$2a$10$b5yQ0CswuUdqoYwykq.XNO/GvBBUI2.Uw7dLWhdBNbsOExmlNhXAO', 'Иван', 'Иванов', 'ADMIN'),
    ('user2', '$2a$10$cYI1A46t6EWtWiBzUEEBae2fCGIZRUkRh.kotHcGPbVkyalOVON.', 'Анна', 'Петрова', 'CLIENT'),
    ('user3', '$2a$10$fGbdpP3ewLukIPgFQR9NNuLtFa1/hBuMGcW/vNoiZOCeUKoSnIUR6', 'Алексей', 'Смирнов', 'CLIENT');

INSERT INTO bankcard(number, user_id, expiration_date, status, balance)
VALUES
    ('4111111111111111', 1, NOW() + INTERVAL '1 year', 'ACTIVE', 10000.00),
    ('4222222222222222', 1, NOW() + INTERVAL '2 years', 'ACTIVE', 5000.00),
    ('5111111111111111', 2, NOW() + INTERVAL '1 year', 'BLOCKED', 15000.00),
    ('5222222222222222', 2, NOW() + INTERVAL '2 years', 'ACTIVE', 20000.00),
    ('6111111111111111', 3, NOW() + INTERVAL '1 year', 'EXPIRED', 3000.00),
    ('6222222222222222', 3, NOW() + INTERVAL '2 years', 'ACTIVE', 7000.00);