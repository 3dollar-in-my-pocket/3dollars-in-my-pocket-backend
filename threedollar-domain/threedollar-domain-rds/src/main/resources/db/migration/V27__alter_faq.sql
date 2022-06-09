ALTER TABLE `faq` ADD COLUMN `application_type` VARCHAR(30) DEFAULT 'USER_API' AFTER `id`;

ALTER TABLE `faq` MODIFY `application_type` VARCHAR(30) NOT NULL;

ALTER TABLE `faq` DROP INDEX `idx_faq_1`;

ALTER TABLE `faq` ADD INDEX `idx_faq_1`(`application_type`, `category`);
