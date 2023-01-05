package me.rqmses.aktiboom.global;

import java.util.Comparator;

public class CompareUtils implements Comparator<String> {

    @Override
    public int compare(String sortKey1,String sortKey2)
    {
        return sortKey1.compareTo(sortKey2);
    }
}
