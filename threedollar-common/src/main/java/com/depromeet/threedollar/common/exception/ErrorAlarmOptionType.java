package com.depromeet.threedollar.common.exception;

public enum ErrorAlarmOptionType {

    ON,
    OFF;

    public boolean isSetAlarm() {
        return this.equals(ErrorAlarmOptionType.ON);
    }

}
