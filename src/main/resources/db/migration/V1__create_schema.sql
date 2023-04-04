CREATE TABLE customer (
  id BIGINT NOT NULL PRIMARY KEY,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT true,
  created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE account (
  id BIGINT NOT NULL PRIMARY KEY,
  customer_id BIGINT NOT NULL,
  currency VARCHAR(10) NOT NULL,
  balance DECIMAL(10,2) NOT NULL,
  limit DECIMAL(10,2) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT true,
  created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (customer_id) REFERENCES customer(id)
);

CREATE TABLE transaction_type (
  id BIGINT NOT NULL PRIMARY KEY,
  type_name VARCHAR(255) NOT NULL,
  calculation_method CHAR(1) NOT NULL
);

CREATE TABLE transaction (
  id BIGINT NOT NULL PRIMARY KEY,
  account_id BIGINT NOT NULL,
  external_id VARCHAR(255) NOT NULL,
  transaction_type_id BIGINT NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  previous_balance DECIMAL(10,2) NOT NULL,
  new_balance DECIMAL(10,2) NOT NULL,
  booked BOOLEAN NOT NULL DEFAULT true,
  created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (account_id) REFERENCES account(id),
  FOREIGN KEY (transaction_type_id) REFERENCES transaction_type(id)
);

INSERT INTO transaction_type (id, type_name, calculation_method) VALUES
  (1, 'DEPOSIT', '+'),
  (2, 'WITHDRAWAL', '-')
  (3, 'BALANCE', '');
