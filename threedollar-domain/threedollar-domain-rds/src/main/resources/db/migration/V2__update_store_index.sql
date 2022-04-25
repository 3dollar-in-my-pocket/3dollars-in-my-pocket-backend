ALTER TABLE `store`
    DROP INDEX `idx_store_1`;

ALTER TABLE `store`
    DROP INDEX `idx_store_2`;

ALTER TABLE `store`
    ADD INDEX `idx_store_1` (`user_id`, `status`);
