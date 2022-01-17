CREATE TABLE `display_menu_category`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `category_type` VARCHAR(30)  NOT NULL,
    `description`   VARCHAR(300) NOT NULL,
    `display_order` INTEGER      NOT NULL,
    `icon_url`      VARCHAR(300) NOT NULL,
    `is_new`        TINYINT(1)   NOT NULL,
    `name`          VARCHAR(30)  NOT NULL,
    `status`        VARCHAR(30)  NOT NULL,
    `created_at`    DATETIME(6) DEFAULT NULL,
    `updated_at`    DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB;
