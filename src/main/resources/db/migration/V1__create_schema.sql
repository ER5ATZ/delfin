CREATE TABLE roles (
  id BIGINT PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(255),
  created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE SEQUENCE roles_id_seq;
ALTER TABLE roles ALTER COLUMN id SET DEFAULT nextval('roles_id_seq');

INSERT INTO roles (id, name, description) VALUES
  (nextval('roles_id_seq'), 'ADMIN', 'System administrator'),
  (nextval('roles_id_seq'), 'SUPER', 'Super user'),
  (nextval('roles_id_seq'), 'ANON', 'User w/o credentials'),
  (nextval('roles_id_seq'), 'USER', 'Regular user');

CREATE TABLE userdetails (
  id BIGINT PRIMARY KEY,
  username VARCHAR(50) NOT NULL,
  email VARCHAR(100) NOT NULL,
  password VARCHAR(255) NOT NULL,
  expires TIMESTAMP NOT NULL,
  pwexpires TIMESTAMP NOT NULL,
  failedlogins INTEGER NOT NULL,
  active BOOLEAN NOT NULL,
  created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE userdetails ADD CONSTRAINT uk_userdetails_username UNIQUE (username);
CREATE SEQUENCE userdetails_id_seq;
ALTER TABLE userdetails ALTER COLUMN id SET DEFAULT nextval('userdetails_id_seq');

CREATE TABLE rolemappings (
  id BIGINT NOT NULL,
  userid BIGINT NOT NULL,
  roleid BIGINT NOT NULL,
  created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (userid, roleid),
  FOREIGN KEY (userid) REFERENCES userdetails (id),
  FOREIGN KEY (roleid) REFERENCES roles (id)
);

CREATE SEQUENCE rolemappings_id_seq;
  ALTER TABLE rolemappings ALTER COLUMN id SET DEFAULT nextval('rolemappings_id_seq');

CREATE TABLE customers (
  id BIGINT NOT NULL PRIMARY KEY,
  firstname VARCHAR(255) NOT NULL,
  lastname VARCHAR(255) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT true,
  created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE SEQUENCE customers_id_seq;
ALTER TABLE customers ALTER COLUMN id SET DEFAULT nextval('customers_id_seq');

CREATE TABLE customermappings (
  id BIGINT NOT NULL,
  userid BIGINT NOT NULL,
  customerid BIGINT NOT NULL,
  created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (userid),
  UNIQUE (customerid),
  FOREIGN KEY (userid) REFERENCES userdetails(id),
  FOREIGN KEY (customerid) REFERENCES customers(id)
);

CREATE SEQUENCE customermappings_id_seq;
ALTER TABLE customermappings ALTER COLUMN id SET DEFAULT nextval('customermappings_id_seq');

CREATE TABLE accounts (
  id BIGINT NOT NULL PRIMARY KEY,
  customerid BIGINT NOT NULL,
  accountlimit DECIMAL(10,2) NOT NULL,
  accountnumber VARCHAR(30) NOT NULL,
  currency VARCHAR(10) NOT NULL,
  balance DECIMAL(10,2) NOT NULL,
  pin1 BIGINT,
  pin2 BIGINT,
  active BOOLEAN NOT NULL DEFAULT true,
  created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (customerid) REFERENCES customers(id)
);

CREATE SEQUENCE accounts_id_seq;
ALTER TABLE accounts ALTER COLUMN id SET DEFAULT nextval('accounts_id_seq');
ALTER TABLE accounts ADD CONSTRAINT uk_accounts_accountnumber UNIQUE (accountnumber);

CREATE TABLE transactiontypes (
  id BIGINT NOT NULL PRIMARY KEY,
  typename VARCHAR(255) NOT NULL,
  calculation VARCHAR(10) NOT NULL CHECK (calculation IN ('positive', 'negative', 'neutral')),
  created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE SEQUENCE transactiontypes_id_seq;
ALTER TABLE transactiontypes ALTER COLUMN id SET DEFAULT nextval('transactiontypes_id_seq');
INSERT INTO transactiontypes (id, typename, calculation) VALUES
  (nextval('transactiontypes_id_seq'), 'DEPOSIT', 'positive'),
  (nextval('transactiontypes_id_seq'), 'WITHDRAWAL', 'negative'),
  (nextval('transactiontypes_id_seq'), 'BALANCE', 'neutral');

CREATE TABLE transactions (
  id BIGINT NOT NULL PRIMARY KEY,
  accountid BIGINT NOT NULL,
  externalid VARCHAR(255) NOT NULL,
  transactiontypeid BIGINT NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  prevbalance DECIMAL(10,2) NOT NULL,
  newbalance DECIMAL(10,2) NOT NULL,
  transactiontime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  booked BOOLEAN NOT NULL DEFAULT false,
  created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (accountid) REFERENCES accounts(id),
  FOREIGN KEY (transactiontypeid) REFERENCES transactiontypes(id)
);

CREATE SEQUENCE transactions_id_seq;
ALTER TABLE transactions ALTER COLUMN id SET DEFAULT nextval('transactions_id_seq');
