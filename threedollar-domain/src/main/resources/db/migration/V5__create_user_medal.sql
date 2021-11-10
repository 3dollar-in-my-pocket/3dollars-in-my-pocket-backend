CREATE TABLE `user_medal`
(
    `id`         BIGINT      NOT NULL AUTO_INCREMENT,
    `user_id`    BIGINT      NOT NULL,
    `medal_type` VARCHAR(30) NOT NULL,
    `created_at` DATETIME(6) DEFAULT NULL,
    `updated_at` DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uni_user_medal_1` (`user_id`, `medal_type`)
) ENGINE = InnoDB;


ALTER TABLE `user`
    ADD COLUMN `medal_type` VARCHAR(30) DEFAULT NULL;
