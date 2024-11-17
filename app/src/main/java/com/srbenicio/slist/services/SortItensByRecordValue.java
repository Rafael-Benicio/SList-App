package com.srbenicio.slist.services;

import com.srbenicio.slist.ItemList;
import com.srbenicio.slist.interfaces.SortByInterface;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortItensByRecordValue implements SortByInterface {
    @Override
    public void sort(List itemsList, boolean isAscending) {
        if (isAscending) {
            Collections.sort(itemsList, new Comparator<ItemList>() {
                @Override
                public int compare(ItemList o1, ItemList o2) {
                    return Integer.compare(o1.getRecord(), o2.getRecord());
                }
            });
        } else {
            Collections.sort(itemsList, new Comparator<ItemList>() {
                @Override
                public int compare(ItemList o1, ItemList o2) {
                    return Integer.compare(o2.getRecord(), o1.getRecord());
                }
            });
        }
    }
}
