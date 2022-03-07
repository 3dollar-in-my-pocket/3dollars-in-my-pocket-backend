drop index `uni_visit_history_1` on `visit_history`;
drop index `idx_visit_history_3` on `visit_history`;

create unique index `uni_visit_history_1` on `visit_history` (`store_id`, `user_id`, `date_of_visit`);
CREATE INDEX `idx_visit_history_3` ON `visit_history` (`store_id`, `type`, `date_of_visit`);


drop index idx_popup_1 on popup;
CREATE INDEX `idx_popup_1` ON popup (platform_type, id, start_date_time);


drop index idx_store_1 on store;
drop index idx_store_3 on store;

CREATE INDEX `idx_store_1` ON store (user_id);
CREATE INDEX `idx_store_2` ON store (latitude, longitude);
