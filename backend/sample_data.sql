-- sample_data.sql
-- Creates two customers, one account each, initial deposits, and an example online transfer.

-- 1) Add first customer (Alice)
INSERT INTO customer
  (name, phoneNumber, email, address, pin, aadharNumber, dob)
VALUES
  ('Alice Kumar', '+919900112233', 'alice@example.com', '12 Park Street, Pune', '1234', '111122223333', '1990-05-20');
SET @cust1 = LAST_INSERT_ID();

-- 2) Add second customer (Bob)
INSERT INTO customer
  (name, phoneNumber, email, address, pin, aadharNumber, dob)
VALUES
  ('Bob Sharma', '+919988776655', 'bob@example.com', '34 MG Road, Mumbai', '4321', '444455556666', '1986-09-12');
SET @cust2 = LAST_INSERT_ID();

-- 3) Create an account for Alice
INSERT INTO account
  (customerId, balance, accountType, accountName, accountNumber, phoneNumberLinked)
VALUES
  (@cust1, 5000.00, 'Savings', 'Alice Savings', 'ACC1000001', '+919900112233');
SET @acc1 = LAST_INSERT_ID();

-- 4) Create an account for Bob
INSERT INTO account
  (customerId, balance, accountType, accountName, accountNumber, phoneNumberLinked)
VALUES
  (@cust2, 3000.00, 'Savings', 'Bob Savings', 'ACC1000002', '+919988776655');
SET @acc2 = LAST_INSERT_ID();

-- 5) Add initial deposit transaction records (optional records showing initial funding)
INSERT INTO bank_transaction
  (accountId, transactionAmount, transactionMode, receiverDetails, senderDetails, description)
VALUES
  (@acc1, 5000.00, 'Cash', NULL, NULL, 'Initial deposit'),
  (@acc2, 3000.00, 'Cash', NULL, NULL, 'Initial deposit');

-- 6) Perform an example online transfer: Alice -> Bob of 200.00
--    This updates balances atomically and inserts transaction records for both accounts.
START TRANSACTION;

  -- subtract from Alice
  UPDATE account
    SET balance = balance - 200.00
    WHERE accountId = @acc1;

  -- add to Bob
  UPDATE account
    SET balance = balance + 200.00
    WHERE accountId = @acc2;

  -- record debit transaction for Alice
  INSERT INTO bank_transaction
    (accountId, transactionAmount, transactionMode, receiverDetails, senderDetails, description)
  VALUES
    (@acc1, 200.00, 'Online Transfer', CONCAT('To: ', 'ACC1000002', ' (+919988776655)'), CONCAT('From: ', 'ACC1000001', ' (+919900112233)'), 'Transfer to Bob');

  -- record credit transaction for Bob
  INSERT INTO bank_transaction
    (accountId, transactionAmount, transactionMode, receiverDetails, senderDetails, description)
  VALUES
    (@acc2, 200.00, 'Online Transfer', CONCAT('To: ', 'ACC1000001', ' (+919900112233)'), CONCAT('From: ', 'ACC1000002', ' (+919988776655)'), 'Received from Alice');

COMMIT;

-- 7) Quick verification queries (run these after executing the script)
SELECT customerId, name, phoneNumber, aadharNumber, dob FROM customer;
SELECT accountId, customerId, accountNumber, accountName, balance, accountType, phoneNumberLinked FROM account;
SELECT transactionId, accountId, transactionAmount, transactionMode, transactionTime, description FROM bank_transaction ORDER BY transactionTime ASC;