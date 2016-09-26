package com.homesky.homecloud_lib.model.enums;

public enum CommandCategoryEnum implements SingleValueEnum {
    TOGGLE(1), TEMPERATURE(2), FAN(3), LIGHT_SWITCH(4), AC_MODE(5), LIGHT_INTENSITY(6), LIGHT_COLOR(7);

    private long id;

    CommandCategoryEnum(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }
}
