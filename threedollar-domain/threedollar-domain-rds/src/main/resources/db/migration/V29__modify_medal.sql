ALTER TABLE `medal` MODIFY `disable_icon_url` VARCHAR(2048) NOT NULL AFTER `activation_icon_url`;

ALTER TABLE `medal` MODIFY `introduction` VARCHAR(200) DEFAULT NULL AFTER `disable_icon_url`;
