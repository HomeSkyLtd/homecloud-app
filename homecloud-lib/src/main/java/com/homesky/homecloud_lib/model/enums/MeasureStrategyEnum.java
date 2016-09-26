package com.homesky.homecloud_lib.model.enums;

public enum MeasureStrategyEnum implements SingleValueEnum {
    EVENT(1), PERIODIC(2);

    private long id;

    MeasureStrategyEnum(long id){
        this.id = id;
    }

    @Override
    public long getId() {
        return this.id;
    }
}
