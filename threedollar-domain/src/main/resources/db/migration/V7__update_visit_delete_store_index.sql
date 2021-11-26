ALTER TABLE `visit_history`
    drop index `uni_visit_history_1`;

CREATE UNIQUE INDEX `uni_visit_history_1` ON `visit_history` (`store_id`, `date_of_visit`, `user_id`);

CREATE UNIQUE INDEX `uni_store_delete_request_1` ON `store_delete_request` (`store_id`, `user_id`);
