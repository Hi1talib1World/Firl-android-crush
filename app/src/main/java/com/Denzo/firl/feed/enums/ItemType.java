package com.Denzo.firl.feed.enums;

public enum ItemType {LOAD(10), ITEM(11);
    private final int typeCode;

    ItemType(int typeCode) {
        this.typeCode = typeCode;
    }

    public int getTypeCode() {
        return this.typeCode;
    }
}