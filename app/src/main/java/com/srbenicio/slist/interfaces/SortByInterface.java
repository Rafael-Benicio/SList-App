package com.srbenicio.slist.interfaces;


import java.util.List;

public interface SortByInterface<T> {
    public void sort(List<T> itemsList, boolean isAscending);
}
