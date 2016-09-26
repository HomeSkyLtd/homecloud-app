package com.homesky.homecloud_lib.model.enums;

public enum NodeClassEnum implements MultipleValueEnum {
    SENSOR(1), ACTUATOR(2);

    private long id;

    NodeClassEnum(long id) {
        this.id = id;
    }

    @Override
    public long getId(){
        return id;
    }

}


