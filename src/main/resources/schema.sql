-- ************************************** customers

CREATE TABLE IF NOT EXISTS customers
(
 customer_id         uuid NOT NULL,
 customer_name       varchar(50) NOT NULL,
 customer_last_name  varchar(100) NOT NULL,
 customer_phone      varchar(50) NOT NULL,
 customer_email      varchar(50) NOT NULL,
 customer_created_at timestamp with time zone NOT NULL,
 CONSTRAINT PK_customers PRIMARY KEY ( customer_id ),
 CONSTRAINT uk_customer_email UNIQUE ( customer_email )
);