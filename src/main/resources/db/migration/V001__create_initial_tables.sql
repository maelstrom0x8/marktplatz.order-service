
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TYPE order_status AS ENUM ('PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED');

CREATE FUNCTION status_cast(varchar) RETURNS order_status AS $$
    SELECT CASE $1
        WHEN 'PENDING' THEN 'PENDING'::order_status
        WHEN 'PROCESSING' THEN 'PROCESSING'::order_status
        WHEN 'SHIPPED' THEN 'SHIPPED'::order_status
        WHEN 'DELIVERED' THEN 'DELIVERED'::order_status
        WHEN 'CANCELLED' THEN 'CANCELLED'::order_status

    END;
$$ LANGUAGE SQL;

CREATE CAST (VARCHAR AS order_status) WITH FUNCTION status_cast(VARCHAR) AS ASSIGNMENT;

CREATE TABLE orders (
    order_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    status order_status,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified TIMESTAMP NOT NULL
);

CREATE SEQUENCE order_item_seq;

CREATE TABLE order_items (
    order_item_id BIGINT DEFAULT nextval('order_item_seq') PRIMARY KEY,
    order_id UUID,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);

