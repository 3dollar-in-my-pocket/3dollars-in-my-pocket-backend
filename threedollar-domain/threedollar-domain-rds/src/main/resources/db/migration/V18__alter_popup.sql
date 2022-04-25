ALTER TABLE `popup`
    ADD `position_type` VARCHAR(30) NOT NULL DEFAULT 'SPLASH';

ALTER TABLE `popup`
    MODIFY `position_type` VARCHAR(30) NOT NULL;

ALTER TABLE `popup`
    ADD `priority` int NOT NULL DEFAULT 0;

drop index `idx_popup_1` on `popup`;

CREATE INDEX `idx_popup_1` ON popup (`position_type`, `platform_type`, `id`, `start_date_time`);
