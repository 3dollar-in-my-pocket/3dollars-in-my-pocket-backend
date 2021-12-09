ALTER TABLE `medal`
    CHANGE `icon_url` `activation_icon_url` VARCHAR(2048) NOT NULL;

ALTER TABLE `medal`
    ADD COLUMN `disable_icon_url` VARCHAR(2048) NOT NULL;
