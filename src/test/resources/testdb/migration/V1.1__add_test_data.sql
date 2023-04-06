INSERT INTO customer (id, firstname, lastname, active, created, updated) VALUES
(1, 'John', 'Doe', false, NOW(), NOW()),
(2, 'Jane', 'Doe', true, NOW(), NOW()),
(3, 'Bob', 'Smith', false, NOW(), NOW());

INSERT INTO account (id, customerid, currency, balance, accountlimit, created, updated) VALUES
(1, 1, 'USD', 1000.0, 2000.0, NOW(), NOW()),
(2, 1, 'EUR', 500.0, 1000.0, NOW(), NOW()),
(3, 2, 'USD', 2000.0, 3000.0, NOW(), NOW());

INSERT INTO transaction (id, accountid, externalid, transactiontypeid, amount, prevbalance, newbalance, transactiontime) VALUES
(1, 1, '111', 2, 950.0, 1000.0, 50.0, NOW()),
(2, 1, '112', 2, 50.0, 50.0, 0.0, NOW()),
(3, 3, 'ATM42', 2, 3000.0, 2000.0, -1000.0, NOW()),
(4, 2, 'BANK666', 1, 4500.0, 500.0, 5000.0, NOW()),
(5, 2, 'BANK777', 3, 0, 5000.0, 5000.0, NOW());
