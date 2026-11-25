INSERT INTO tax_rules (country_code, country_name, currency, standard_rate, reduced_rate, super_reduced_rate, tax_id_regex, tax_id_example)
VALUES
    ('ES', 'Spain', 'EUR', 21.00, 10.00, 4.00, '^ES[A-Z0-9][0-9]{7}[A-Z0-9]$', 'ESB12345678'),
    ('FR', 'France', 'EUR', 20.00, 5.50, 2.10, '^FR[A-Z0-9]{2}[0-9]{9}$', 'FR12345678901'),
    ('DE', 'Germany', 'EUR', 19.00, 7.00, 0.00, '^DE[0-9]{9}$', 'DE123456789'),
    ('PT', 'Portugal', 'EUR', 23.00, 13.00, 6.00, '^PT[0-9]{9}$', 'PT123456789')
    ON CONFLICT (country_code) DO NOTHING;