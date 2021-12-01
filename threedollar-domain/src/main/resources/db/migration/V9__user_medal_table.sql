drop table `user_medal`;


alter table `user`
    drop `medal_type`;


CREATE TABLE `user_medal`
(
    `id`         BIGINT      NOT NULL AUTO_INCREMENT,
    `medal_id`   BIGINT      NOT NULL,
    `user_id`    BIGINT      NOT NULL,
    `status`     VARCHAR(30) NOT NULL,
    `created_at` DATETIME(6) DEFAULT NULL,
    `updated_at` DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uni_user_medal_1` (`medal_id`, `user_id`),
    KEY `idx_user_medal_1` (`medal_id`),
    KEY `idx_user_medal_2` (`user_id`)
) ENGINE = InnoDB;


CREATE TABLE `medal`
(
    `id`         BIGINT        NOT NULL AUTO_INCREMENT,
    `name`       VARCHAR(30)   NOT NULL,
    `icon_url`   VARCHAR(2048) NOT NULL,
    `created_at` DATETIME(6) DEFAULT NULL,
    `updated_at` DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB;


CREATE TABLE `medal_acquisition_condition`
(
    `id`             BIGINT      NOT NULL AUTO_INCREMENT,
    `medal_id`       BIGINT      NOT NULL,
    `condition_type` VARCHAR(30) NOT NULL,
    `count`          INTEGER     NOT NULL DEFAULT 0,
    `created_at`     DATETIME(6)          DEFAULT NULL,
    `updated_at`     DATETIME(6)          DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_medal_acquisition_condition_1` (`medal_id`)
) ENGINE = InnoDB;
