CREATE TABLE `store_promotion`
(
    `id`                       BIGINT       NOT NULL AUTO_INCREMENT,
    `created_at`               DATETIME(6) DEFAULT NULL,
    `updated_at`               DATETIME(6) DEFAULT NULL,
    `introduction`             VARCHAR(300) NOT NULL,
    `icon_url`                 VARCHAR(300) NOT NULL,
    `is_display_on_marker`     TINYINT(1)   NOT NULL,
    `is_display_on_the_detail` TINYINT(1)   NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB;


ALTER TABLE `store`
    ADD `store_promotion_id` BIGINT DEFAULT NULL;

CREATE
    INDEX `idx_store_3` ON `store` (`store_promotion_id`);
