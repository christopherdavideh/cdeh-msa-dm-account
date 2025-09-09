-- Create account table
CREATE TABLE account (
    account_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_number VARCHAR(10) NOT NULL,
    type_account VARCHAR(100) NOT NULL,
    initial_balance DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    account_status BOOLEAN DEFAULT TRUE,
    customer_id UUID NOT NULL
);

-- Create index
CREATE INDEX idx_account_customer_id ON account(customer_id);
CREATE INDEX idx_account_number ON account(account_number);


