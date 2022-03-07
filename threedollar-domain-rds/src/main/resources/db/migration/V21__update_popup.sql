ALTER TABLE `popup`
    ADD COLUMN `title` VARCHAR(50) DEFAULT NULL;

ALTER TABLE `popup`
    ADD COLUMN `sub_title` VARCHAR(100) DEFAULT NULL;

ALTER TABLE `popup`
    ADD COLUMN `bg_color` CHAR(7) DEFAULT NULL;

ALTER TABLE `popup`
    ADD COLUMN `font_color` CHAR(7) DEFAULT NULL;

ALTER TABLE `popup`
    DROP `priority`;
