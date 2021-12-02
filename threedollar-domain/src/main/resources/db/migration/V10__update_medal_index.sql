alter table `medal_acquisition_condition`
    drop index `idx_medal_acquisition_condition_1`;

create unique index `uni_medal_acquisition_condition_1` on `medal_acquisition_condition` (`medal_id`, `condition_type`)
