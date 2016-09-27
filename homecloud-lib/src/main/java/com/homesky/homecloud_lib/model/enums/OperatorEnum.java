package com.homesky.homecloud_lib.model.enums;

public enum OperatorEnum implements SingleValueEnum {
    LT(1, "<"), LE(2, "<="), EQ(3, "=="), NE(4, "!="), GT(5, ">"), GE(6, ">=");

    private long id;
    private String representation;

    public static OperatorEnum fromRepresentation(String representation){
        for(OperatorEnum en : OperatorEnum.values()){
            if(en.representation.equals(representation))
                return en;
        }
        return null;
    }

    OperatorEnum(long id, String representation) {
        this.id = id;
        this.representation = representation;
    }

    @Override
    public long getId(){
        return id;
    }

    public String getRepresentation() {
        return representation;
    }
}
