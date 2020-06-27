package com.Denzo.firl.feed.model;

import com.Denzo.firl.feed.enums.ItemType;

public interface  LazyLoading {
    ItemType getItemType();
    void setItemType(ItemType itemType);
}