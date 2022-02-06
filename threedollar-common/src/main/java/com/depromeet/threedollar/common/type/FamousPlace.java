package com.depromeet.threedollar.common.type;

import lombok.Getter;

@Getter
public enum FamousPlace {

    GANGNAM("강남역", 37.498095, 127.02761),
    PANGYO("판교역", 37.394230, 127.110945),
    HONGDAE("홍대입구역", 37.556290, 126.922736),
    YEOUIDO("여의도", 37.521165, 126.924316),
    HYEHWA("대학로", 37.581306, 127.001829),
    HAEUNDAE("해운대", 35.159222, 129.159235),
    GWANGALLI("광안리", 35.153136, 129.118961),
    JEJU("제주도", 33.493079, 126.529060),
    ;

    private final String description;
    private final double latitude;
    private final double longitude;

    private FamousPlace(String description, double latitude, double longitude) {
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
