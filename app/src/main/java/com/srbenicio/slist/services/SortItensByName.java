package com.srbenicio.slist.services;

import com.srbenicio.slist.ItemList;
import com.srbenicio.slist.interfaces.SortByInterface;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortItensByName implements SortByInterface {
    @Override
    public void sort(List itemsList, boolean isAscending) {
        if (isAscending) {
            Collections.sort(itemsList, new Comparator<ItemList>() {
                @Override
                public int compare(ItemList o1, ItemList o2) {
                    return o1.getName().compareToIgnoreCase(o2.getName());
                }
            });
        } else {
            Collections.sort(itemsList, new Comparator<ItemList>() {
                @Override
                public int compare(ItemList o1, ItemList o2) {
                    return o2.getName().compareToIgnoreCase(o1.getName());
                }
            });
        }
    }
}
