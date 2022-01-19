CREATE TABLE `store_promotion`
(
    `id`           BIGINT        NOT NULL AUTO_INCREMENT,
    `created_at`   DATETIME(6)   DEFAULT NULL,
    `updated_at`   DATETIME(6)   DEFAULT NULL,
    `title`        VARCHAR(30)   NOT NULL,
    `introduction` VARCHAR(2048) NOT NULL,
    `image_url`    VARCHAR(2048) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB;


ALTER TABLE `store`
    ADD `store_promotion_id` BIGINT DEFAULT NULL;

CREATE INDEX `idx_store_3` ON `store` (`store_promotion_id`);
