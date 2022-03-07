CREATE TABLE `admin`
(
    `id`         BIGINT      NOT NULL AUTO_INCREMENT,
    `email`      VARCHAR(50) NOT NULL,
    `name`       VARCHAR(30) NOT NULL,
    `created_at` DATETIME(6) DEFAULT NULL,
    `updated_at` DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB;
