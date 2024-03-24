alter table order_event add column comment varchar(255);
alter table order_event drop column type;
alter table order_event add column type varchar(255) check (type in ('ORDER_STATUS_CHANGED', 'PAYMENT_STATUS_CHANGED', 'PRODUCT_PRICE_CHANGED', 'PRODUCTS_ADDED', 'PRODUCTS_RETURNED'));