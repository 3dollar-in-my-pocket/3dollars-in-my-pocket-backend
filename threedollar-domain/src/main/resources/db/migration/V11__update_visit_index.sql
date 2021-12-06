alter table `visit_history`
    drop index `idx_visit_history_2`;

alter table `visit_history`
    drop index `idx_visit_history_3`;


create index `idx_visit_history_2` on `visit_history` (`user_id`, `type`);

create index `idx_visit_history_3` on `visit_history` (`store_id`, `date_of_visit`, `type`);
