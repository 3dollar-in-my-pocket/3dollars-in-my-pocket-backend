package com.depromeet.threedollar.common.exception.type;

public enum ErrorAlarmOptionType {

    ON,
    OFF;

    public boolean isSetAlarm() {
        return this.equals(ErrorAlarmOptionType.ON);
    }

}
