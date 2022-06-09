ALTER TABLE `advertisement` ADD COLUMN `application_type` VARCHAR(30) DEFAULT 'USER_API' AFTER `id`;

ALTER TABLE `advertisement` MODIFY `application_type` VARCHAR(30) NOT NULL;

ALTER TABLE `advertisement` DROP INDEX `idx_advertisement_1`;

ALTER TABLE `advertisement` ADD INDEX `idx_advertisement_1`(`application_type`, `position_type`, `platform_type`, `id`, `start_date_time`);

ALTER TABLE `advertisement` MODIFY `position_type` VARCHAR(30) NOT NULL AFTER `platform_type`;
ALTER TABLE `advertisement` MODIFY `created_at` DATETIME(6) DEFAULT NULL AFTER `font_color`;
ALTER TABLE `advertisement` MODIFY `updated_at` DATETIME(6) DEFAULT NULL AFTER `created_at`;
