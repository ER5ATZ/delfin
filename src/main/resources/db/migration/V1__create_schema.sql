CREATE TABLE customer (
  id BIGINT NOT NULL PRIMARY KEY,
  firstname VARCHAR(255) NOT NULL,
  lastname VARCHAR(255) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT true,
  created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE SEQUENCE customer_id_seq;
ALTER TABLE customer ALTER COLUMN id SET DEFAULT nextval('customer_id_seq');

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

CREATE SEQUENCE account_id_seq;
ALTER TABLE account ALTER COLUMN id SET DEFAULT nextval('account_id_seq');

CREATE TABLE transactiontype (
  id BIGINT NOT NULL PRIMARY KEY,
  typename VARCHAR(255) NOT NULL,
  calculation CHAR(8) NOT NULL
);

CREATE SEQUENCE transactiontype_id_seq;
INSERT INTO transactiontype (id, typename, calculation) VALUES
  (1, 'DEPOSIT', 'positive'),
  (2, 'WITHDRAWAL', 'negative'),
  (3, 'BALANCE', 'neutral');
--ALTER TABLE transactiontype ALTER COLUMN id SET DEFAULT nextval('transactiontype_id_seq');

CREATE TABLE transaction (
  id BIGINT NOT NULL PRIMARY KEY,
  accountid BIGINT NOT NULL,
  externalid VARCHAR(255) NOT NULL,
  transactiontypeid BIGINT NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  prevbalance DECIMAL(10,2) NOT NULL,
  newbalance DECIMAL(10,2) NOT NULL,
  transactiontime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  booked BOOLEAN NOT NULL DEFAULT false,
  FOREIGN KEY (accountid) REFERENCES account(id),
  FOREIGN KEY (transactiontypeid) REFERENCES transactiontype(id)
);

CREATE SEQUENCE transaction_id_seq;
ALTER TABLE transaction ALTER COLUMN id SET DEFAULT nextval('transaction_id_seq');
