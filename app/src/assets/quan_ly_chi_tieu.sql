CREATE TABLE "User"(
    "id" BIGINT NOT NULL,
    "name" NVARCHAR(50) NOT NULL,
    "age" DATE NOT NULL,
    "email" VARCHAR(100) NOT NULL,
    "username" VARCHAR(50) NOT NULL,
    "password" VARCHAR(50) NOT NULL,
    "create_at" DATE NOT NULL
);
ALTER TABLE
    "User" ADD CONSTRAINT "user_id_primary" PRIMARY KEY("id");
CREATE TABLE "Wallet"(
    "id" BIGINT NOT NULL,
    "user_id" BIGINT NOT NULL,
    "wallet_name" NVARCHAR(50) NOT NULL,
    "balance" BIGINT NOT NULL,
    "currency" VARCHAR(3) NOT NULL
);
ALTER TABLE
    "Wallet" ADD CONSTRAINT "wallet_id_primary" PRIMARY KEY("id");
CREATE INDEX "wallet_user_id_index" ON
    "Wallet"("user_id");
CREATE TABLE "Category"(
    "id" BIGINT NOT NULL,
    "user_id" BIGINT NOT NULL,
    "name" NVARCHAR(50) NOT NULL,
    "type" NVARCHAR(255) CHECK
        ("type" IN(N'income', N'expense')) NOT NULL DEFAULT 'expense',
        "icon" BIGINT NOT NULL
);
ALTER TABLE
    "Category" ADD CONSTRAINT "category_id_primary" PRIMARY KEY("id");
CREATE INDEX "category_user_id_index" ON
    "Category"("user_id");
CREATE TABLE "Transaction"(
    "id" BIGINT NOT NULL,
    "wallet_id" BIGINT NOT NULL,
    "category_id" BIGINT NOT NULL,
    "amount" INT NOT NULL,
    "note" TEXT NOT NULL,
    "date" DATE NOT NULL,
    "type" NVARCHAR(255) CHECK
        ("type" IN(N'income', N'expense')) NOT NULL,
        "created_at" DATE NOT NULL
);
ALTER TABLE
    "Transaction" ADD CONSTRAINT "transaction_id_primary" PRIMARY KEY("id");
CREATE INDEX "transaction_wallet_id_index" ON
    "Transaction"("wallet_id");
CREATE INDEX "transaction_category_id_index" ON
    "Transaction"("category_id");
CREATE TABLE "Budget"(
    "id" BIGINT NOT NULL,
    "user_id" BIGINT NOT NULL,
    "category_id" BIGINT NOT NULL,
    "amount_limit" INT NOT NULL,
    "start_date" DATE NOT NULL,
    "end_date" DATE NOT NULL
);
ALTER TABLE
    "Budget" ADD CONSTRAINT "budget_id_primary" PRIMARY KEY("id");
CREATE INDEX "budget_user_id_index" ON
    "Budget"("user_id");
CREATE INDEX "budget_category_id_index" ON
    "Budget"("category_id");
CREATE TABLE "Attachment"(
    "id" BIGINT NOT NULL,
    "transaction_id" BIGINT NOT NULL,
    "file_path" VARCHAR(255) NOT NULL,
    "create_at" BIGINT NOT NULL
);
ALTER TABLE
    "Attachment" ADD CONSTRAINT "attachment_id_primary" PRIMARY KEY("id");
CREATE INDEX "attachment_transaction_id_index" ON
    "Attachment"("transaction_id");
ALTER TABLE
    "Budget" ADD CONSTRAINT "budget_category_id_foreign" FOREIGN KEY("category_id") REFERENCES "Category"("id");
ALTER TABLE
    "Wallet" ADD CONSTRAINT "wallet_user_id_foreign" FOREIGN KEY("user_id") REFERENCES "User"("id");
ALTER TABLE
    "Attachment" ADD CONSTRAINT "attachment_transaction_id_foreign" FOREIGN KEY("transaction_id") REFERENCES "Transaction"("id");
ALTER TABLE
    "Transaction" ADD CONSTRAINT "transaction_category_id_foreign" FOREIGN KEY("category_id") REFERENCES "Category"("id");
ALTER TABLE
    "Budget" ADD CONSTRAINT "budget_user_id_foreign" FOREIGN KEY("user_id") REFERENCES "User"("id");
ALTER TABLE
    "Transaction" ADD CONSTRAINT "transaction_wallet_id_foreign" FOREIGN KEY("wallet_id") REFERENCES "Wallet"("id");
ALTER TABLE
    "User" ADD CONSTRAINT "user_id_foreign" FOREIGN KEY("id") REFERENCES "Category"("id");