CREATE TABLE `popup`
(
    `id`              BIGINT        not null auto_increment,
    `platform_type`   VARCHAR(30)   NOT NULL,
    `image_url`       VARCHAR(2048) NOT NULL,
    `link_url`        VARCHAR(2048) DEFAULT NULL,
    `start_date_time` DATETIME(6)   NOT NULL,
    `end_date_time`   DATETIME(6)   NOT NULL,
    `created_at`      DATETIME(6)   DEFAULT NULL,
    `updated_at`      DATETIME(6)   DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_popup_1` (`platform_type`, `start_date_time`, `end_date_time`)
) ENGINE = InnoDB;
