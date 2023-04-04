CREATE TABLE customer (
  id BIGINT NOT NULL PRIMARY KEY,
  firstname VARCHAR(255) NOT NULL,
  lastname VARCHAR(255) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT true,
  created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE account (
  id BIGINT NOT NULL PRIMARY KEY,
  customerid BIGINT NOT NULL,
  currency VARCHAR(10) NOT NULL,
  balance DECIMAL(10,2) NOT NULL,
  accountlimit DECIMAL(10,2) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT true,
  created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (customerid) REFERENCES customer(id)
);

CREATE TABLE transactiontype (
  id BIGINT NOT NULL PRIMARY KEY,
  typename VARCHAR(255) NOT NULL,
  calculationmethod CHAR(1) NOT NULL
);

CREATE TABLE transaction (
  id BIGINT NOT NULL PRIMARY KEY,
  accountid BIGINT NOT NULL,
  externalid VARCHAR(255) NOT NULL,
  transactiontypeid BIGINT NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  prevbalance DECIMAL(10,2) NOT NULL,
  newbalance DECIMAL(10,2) NOT NULL,
  transactiontime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  booked BOOLEAN NOT NULL DEFAULT true,
  FOREIGN KEY (accountid) REFERENCES account(id),
  FOREIGN KEY (transactiontypeid) REFERENCES transactiontype(id)
);

INSERT INTO transactiontype (id, typename, calculationmethod) VALUES
  (1, 'DEPOSIT', '+'),
  (2, 'WITHDRAWAL', '-'),
  (3, 'BALANCE', '');

INSERT INTO customer (id, firstname, lastname, active, created, updated) VALUES
  (1, 'John', 'Doe', false, NOW(), NOW()),
  (2, 'Jane', 'Doe', true, NOW(), NOW()),
  (3, 'Bob', 'Smith', false, NOW(), NOW());
