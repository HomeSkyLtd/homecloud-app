package com.homesky.homecloud_lib.model.enums;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.EnumSet;

public class EnumUtil {

    public static <T extends Enum<T> & MultipleValueEnum> EnumSet<T> setFrom(long code, Class<T> type){
        EnumSet<T> enums = EnumSet.noneOf(type);
        long divisor = 1;
        long max = 0;
        for(T enumObj : type.getEnumConstants()){
            if(enumObj.getId() > max)
                max = enumObj.getId();
        }

        while(divisor <= max){
            if((code & divisor) != 0)
                enums.add(getEnumFromCode(divisor, type));
            divisor = divisor << 1;
        }
        return enums;
    }

    public static <T extends Enum<T> & SingleValueEnum> T enumFrom(long code, Class<T> type){
        return getEnumFromCode(code, type);
    }

    private static <T extends Enum<T> & ValueEnum> T getEnumFromCode(long code, Class<T> type) {
        for(T enumObj : type.getEnumConstants()){
            if (enumObj.getId() == code)
                return enumObj;
        }
        throw new RuntimeException("Enum code not found");
    }

    public static <T extends Enum<T>> String getMultiEnumAsString(EnumSet<T> enums){
        ArrayList<String> names = new ArrayList<>();
        for (T enumObj : enums){
            names.add(enumObj.name());
        }
        return TextUtils.join(" | ", names);
    }
}
