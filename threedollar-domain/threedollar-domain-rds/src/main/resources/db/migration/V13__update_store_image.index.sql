ALTER TABLE `store_image`
    drop index `idx_store_image_1`;

CREATE INDEX `idx_store_image_1` ON store_image (`store_id`, `status`);
