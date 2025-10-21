INSERT INTO bank_user(login, password, first_name, last_name, role)
VALUES
    ('user1', '$2a$10$eW3mlj5BGcC.p5yqvd3U.OqTAKaf8K7Fn0MoEgNLfk5TPh8uVqgN6', 'Иван', 'Иванов', 'USER'),
    ('user2', '$2a$10$eW3mlj5BGcC.p5yqvd3U.OqTAKaf8K7Fn0MoEgNLfk5TPh8uVqgN6', 'Анна', 'Петрова', 'USER'),
    ('user3', '$2a$10$eW3mlj5BGcC.p5yqvd3U.OqTAKaf8K7Fn0MoEgNLfk5TPh8uVqgN6', 'Алексей', 'Смирнов', 'USER'),
    ('admin', '$2a$10$eW3mlj5BGcC.p5yqvd3U.OqTAKaf8K7Fn0MoEgNLfk5TPh8uVqgN6', 'admin', 'admin', 'ADMIN');

INSERT INTO bankcard(number, user_id, expiration_date, status, balance)
VALUES
    ('4111111111111111', 1, NOW() + INTERVAL '1 year', 'ACTIVE', 10000.00),
    ('4222222222222222', 1, NOW() + INTERVAL '2 years', 'ACTIVE', 5000.00),
    ('5111111111111111', 2, NOW() + INTERVAL '1 year', 'BLOCKED', 15000.00),
    ('5222222222222222', 2, NOW() + INTERVAL '2 years', 'ACTIVE', 20000.00),
    ('6111111111111111', 3, NOW() + INTERVAL '1 year', 'EXPIRED', 3000.00),
    ('6222222222222222', 3, NOW() + INTERVAL '2 years', 'ACTIVE', 7000.00);
