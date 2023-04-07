INSERT INTO customer (id, firstname, lastname, active, created, updated) VALUES
(nextval('customer_id_seq'), 'John', 'Doe', true, NOW(), NOW()),
(nextval('customer_id_seq'), 'Jane', 'Doe', true, NOW(), NOW()),
(nextval('customer_id_seq'), 'Robert', 'Smith', true, NOW(), NOW()),
(nextval('customer_id_seq'), 'Patricia', 'Smith', false, NOW(), NOW());

INSERT INTO account (id, customerid, currency, balance, accountlimit, created, updated) VALUES
(nextval('account_id_seq'), 1, 'USD', 1000.0, 2000.0, NOW(), NOW()),
(nextval('account_id_seq'), 1, 'EUR', 500.0, 1000.0, NOW(), NOW()),
(nextval('account_id_seq'), 2, 'USD', 2000.0, 3000.0, NOW(), NOW()),
(nextval('account_id_seq'), 2, 'EUR', 0.0, 0.0, NOW(), NOW());

INSERT INTO transaction (id, accountid, externalid, transactiontypeid, amount, prevbalance, newbalance, transactiontime) VALUES
(nextval('transaction_id_seq'), 1, '111', 2, 950.0, 1000.0, 50.0, NOW()),
(nextval('transaction_id_seq'), 1, '112', 2, 50.0, 50.0, 0.0, NOW()),
(nextval('transaction_id_seq'), 3, 'ATM42', 2, 3000.0, 2000.0, -1000.0, NOW()),
(nextval('transaction_id_seq'), 2, 'BANK666', 1, 4500.0, 500.0, 5000.0, NOW()),
(nextval('transaction_id_seq'), 2, 'BANK777', 3, 0, 5000.0, 5000.0, NOW());
