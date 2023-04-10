INSERT INTO customers (id, firstname, lastname, active, created, updated) VALUES
(nextval('customers_id_seq'), 'John', 'Doe', true, NOW(), NOW()),
(nextval('customers_id_seq'), 'Jane', 'Doe', true, NOW(), NOW()),
(nextval('customers_id_seq'), 'Robert', 'Smith', true, NOW(), NOW()),
(nextval('customers_id_seq'), 'Patricia', 'Smith', false, NOW(), NOW());

INSERT INTO accounts (id, customerid, accountnumber, currency, balance, accountlimit, created, updated) VALUES
(nextval('accounts_id_seq'), 1, 'USD001', 'USD', 1000.0, 2000.0, NOW(), NOW()),
(nextval('accounts_id_seq'), 1, 'EUR001', 'EUR', 500.0, 1000.0, NOW(), NOW()),
(nextval('accounts_id_seq'), 2, 'USD002', 'USD', 2000.0, 3000.0, NOW(), NOW()),
(nextval('accounts_id_seq'), 2, 'EUR002', 'EUR', 0.0, 0.0, NOW(), NOW());

INSERT INTO transactions (id, accountid, externalid, transactiontypeid, amount, prevbalance, newbalance, transactiontime) VALUES
(nextval('transactions_id_seq'), 1, '111', 2, 950.0, 1000.0, 50.0, NOW()),
(nextval('transactions_id_seq'), 1, '112', 2, 50.0, 50.0, 0.0, NOW()),
(nextval('transactions_id_seq'), 3, 'ATM42', 2, 3000.0, 2000.0, -1000.0, NOW()),
(nextval('transactions_id_seq'), 2, 'BANK666', 1, 4500.0, 500.0, 5000.0, NOW()),
(nextval('transactions_id_seq'), 2, 'BANK777', 3, 0, 5000.0, 5000.0, NOW());
