package com.srbenicio.slist.services;

import com.srbenicio.slist.ItemList;
import com.srbenicio.slist.interfaces.SortByInterface;

import java.util.Comparator;
import java.util.List;

public class SortItemsByName implements SortByInterface<ItemList> {
    @Override
    public void sort(List<ItemList> itemsList, boolean isAscending) {
        Comparator<ItemList> comparator = Comparator.comparing(ItemList::getName);

        if (!isAscending) {
            comparator = comparator.reversed();
        }

        itemsList.sort(comparator);
    }
}
