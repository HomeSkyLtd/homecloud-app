package com.homesky.homecloud_lib.model.enums;

/**
 * Created by fabio on 26/09/2016.
 */

public enum TypeEnum implements SingleValueEnum {
    INT(1), BOOL(2), REAL(3), STRING(4);

    private long id;

    TypeEnum(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }
}
