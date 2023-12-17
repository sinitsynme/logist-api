alter table stored_product drop constraint stored_product_pkey;
alter table stored_product add primary key(product_id, warehouse_id);