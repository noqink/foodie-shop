package com.imooc.enums;

public enum AddressIsDefault {

    NO(0,"否"),
    YES(1,"是");

    public final Integer type;

    public final String value;

    AddressIsDefault(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
