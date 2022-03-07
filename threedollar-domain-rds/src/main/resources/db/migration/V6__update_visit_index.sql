ALTER TABLE `visit_history`
    drop index `idx_visit_history_1`;

CREATE INDEX `uni_visit_history_1` ON visit_history (`store_id`, `date_of_visit`, `user_id`);
