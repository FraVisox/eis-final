package it.unipd.dei.dbdc.SEARCH_TERMS;

import java.util.AbstractMap;

public class MapEntry extends AbstractMap.SimpleEntry<String, Integer>
{
    MapEntry(String s, int a)
    {
        super(s, a);
    }

    public boolean isMajorThan(it.unipd.dei.dbdc.SEARCH_TERMS.MapEntry a)
    {
        if (this.getValue() > a.getValue())
        {
            return true;
        }
        else return this.getValue().equals(a.getValue()) && (this.getKey().compareToIgnoreCase(a.getKey()) < 0);
    }
}