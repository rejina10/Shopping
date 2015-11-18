package com.android.shopping.util;

import com.android.shopping.ItemModel;

import java.util.Comparator;



/**
 * Created by user on 11/13/2015.
 */
public class SortItemByCategory implements Comparator<ItemModel> {
    @Override
    public int compare(ItemModel itemModel, ItemModel t1) {
        return itemModel.getCategory().compareToIgnoreCase(t1.getCategory());
    }
}
