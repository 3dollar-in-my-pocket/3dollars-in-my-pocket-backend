create table `visit_history`
(
    `id`            BIGINT      NOT NULL AUTO_INCREMENT,
    `store_id`      BIGINT      NOT NULL,
    `user_id`       BIGINT      NOT NULL,
    `type`          VARCHAR(30) NOT NULL,
    `date_of_visit` DATE        NOT NULL,
    `created_at`    DATETIME(6) DEFAULT NULL,
    `updated_at`    DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_visit_history_1` (`store_id`, `date_of_visit`, `user_id`),
    KEY `idx_visit_history_2` (`user_id`),
    KEY `idx_visit_history_3` (`store_id`, `type`)
) ENGINE = InnoDB;
