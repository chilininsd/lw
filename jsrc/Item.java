//package com.reuben.dtanner;

import java.util.LinkedList;

/**
 * @author reuben
 */
public class Item{

    private String name;
    private int index;
    private int size;
    private Boolean with;
    private boolean[] constraints;
    private LinkedList<String> itemNames;

    public Item(String name, int size, char posneg, LinkedList<String> itemNames)
    {
        this.name = name;
        this.size = size;
        this.with = posneg == '\0' ? null : posneg == '+' ? true : false;
        this.itemNames = itemNames;
        this.constraints = itemNames == null ? null : new boolean[itemNames.size()];
    }
    
    public boolean canPackWith(int item)
    {
        if (with == null) return true;
        if (item >= constraints.length) return !with;
        else return !constraints[item];
    }

    @Override
    public int hashCode()
    {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        return this.name.equals(((Item)obj).name);
    }
    
    public String getName()
    {
        return name;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public Boolean isWith()
    {
        return with;
    }

    public void setWith(Boolean with)
    {
        this.with = with;
    }

    public boolean[] getConstraints()
    {
        return constraints;
    }

    public void setConstraints(boolean[] constraints)
    {
        this.constraints = constraints;
    }
    
    public LinkedList<String> getItemNames()
    {
        return itemNames;
    }
    
    @Override
    public String toString()
    {
        return this.name;
    }
    
    public String debugToString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("item").append(this.index).append(" ").append(this.size).append(" ");
        if (with != null)
        {
            sb.append(with ? "+" : "-").append(" ");
            for (int i = 0; i < constraints.length; i++)
            {
                if (constraints[i])
                {
                    sb.append("item").append(i).append(" ");
                }
            }
            sb.append("\n");
            return sb.toString();
        }
        else
        {
            sb.append("\n");
            return sb.toString();
        }
    }}