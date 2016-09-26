package com.homesky.homecloud_lib.model.enums;

public enum DataCategoryEnum implements SingleValueEnum {
    TEMPERATURE(1), LUMINANCE(2), PRESENCE(3), HUMIDITY(4),
    PRESSURE(5), WIND_SPEED(6), SMOKE(7);

    private long id;

    DataCategoryEnum(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }
}
