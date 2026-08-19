-- Create schema
-- Clear any existing data (useful when running locally with persistent DB)
DROP TABLE IF EXISTS ledger_entries CASCADE;
DROP TABLE IF EXISTS accounts CASCADE;
DROP TABLE IF EXISTS customers CASCADE;

CREATE TABLE customers (
  id UUID PRIMARY KEY,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT true,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE accounts (
  id UUID PRIMARY KEY,
  customer_id UUID NOT NULL REFERENCES customers(id),
  iban VARCHAR(34) NOT NULL UNIQUE,
  currency VARCHAR(3) NOT NULL,
  overdraft_limit_amount DECIMAL(19,2) NOT NULL DEFAULT 0,
  overdraft_limit_currency VARCHAR(3) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT true,
  version BIGINT NOT NULL DEFAULT 0,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ledger_entries (
  id UUID PRIMARY KEY,
  account_id UUID NOT NULL REFERENCES accounts(id),
  entry_type VARCHAR(10) NOT NULL,
  amount DECIMAL(19,2) NOT NULL,
  currency VARCHAR(3) NOT NULL,
  description VARCHAR(500),
  idempotency_key VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE ledger_entries ADD CONSTRAINT uq_ledger_account_idempotency UNIQUE (account_id, idempotency_key);
CREATE INDEX idx_ledger_account_created ON ledger_entries(account_id, created_at DESC);

-- Insert seed data
-- Customer 1
INSERT INTO customers (id, first_name, last_name, active) VALUES 
  ('550e8400-e29b-41d4-a716-446655440001', 'Anna', 'Schmidt', true);

-- Customer 2
INSERT INTO customers (id, first_name, last_name, active) VALUES 
  ('550e8400-e29b-41d4-a716-446655440002', 'Bob', 'Mueller', true);

-- Account 1 for Customer 1
INSERT INTO accounts (id, customer_id, iban, currency, overdraft_limit_amount, overdraft_limit_currency, active) VALUES 
  ('550e8400-e29b-41d4-a716-446655440101', '550e8400-e29b-41d4-a716-446655440001', 'DE89370400440532013000', 'EUR', 5000.00, 'EUR', true);

-- Account 2 for Customer 1
INSERT INTO accounts (id, customer_id, iban, currency, overdraft_limit_amount, overdraft_limit_currency, active) VALUES 
  ('550e8400-e29b-41d4-a716-446655440102', '550e8400-e29b-41d4-a716-446655440001', 'DE27100777770209299700', 'USD', 3000.00, 'USD', true);

-- Account 3 for Customer 2
INSERT INTO accounts (id, customer_id, iban, currency, overdraft_limit_amount, overdraft_limit_currency, active) VALUES 
  ('550e8400-e29b-41d4-a716-446655440103', '550e8400-e29b-41d4-a716-446655440002', 'GB29NWBK60161331926819', 'GBP', 2000.00, 'GBP', true);

-- Ledger entries for Account 1
INSERT INTO ledger_entries (id, account_id, entry_type, amount, currency, description, idempotency_key) VALUES 
  ('550e8400-e29b-41d4-a716-446655440201', '550e8400-e29b-41d4-a716-446655440101', 'CREDIT', 1000.00, 'EUR', 'Initial deposit', 'init-1');

INSERT INTO ledger_entries (id, account_id, entry_type, amount, currency, description, idempotency_key) VALUES 
  ('550e8400-e29b-41d4-a716-446655440202', '550e8400-e29b-41d4-a716-446655440101', 'CREDIT', 500.00, 'EUR', 'Transfer in', 'transfer-1');

INSERT INTO ledger_entries (id, account_id, entry_type, amount, currency, description, idempotency_key) VALUES 
  ('550e8400-e29b-41d4-a716-446655440203', '550e8400-e29b-41d4-a716-446655440101', 'DEBIT', 200.00, 'EUR', 'Withdrawal', 'debit-1');

-- Ledger entries for Account 2
INSERT INTO ledger_entries (id, account_id, entry_type, amount, currency, description, idempotency_key) VALUES 
  ('550e8400-e29b-41d4-a716-446655440204', '550e8400-e29b-41d4-a716-446655440102', 'CREDIT', 2000.00, 'USD', 'Salary', 'salary-1');

INSERT INTO ledger_entries (id, account_id, entry_type, amount, currency, description, idempotency_key) VALUES 
  ('550e8400-e29b-41d4-a716-446655440205', '550e8400-e29b-41d4-a716-446655440102', 'DEBIT', 300.00, 'USD', 'Expense', 'expense-1');
