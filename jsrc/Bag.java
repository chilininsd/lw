//package com.reuben.dtanner;

import java.util.HashSet;

/**
 * truth values in constraints represent that a bag cannot have that item in it
 * @author reuben
 */
public class Bag implements Comparable {
    
    private int capacity;
    private int currentSize;
    private HashSet<Item> items;
    private boolean[] domain;
    
    public Bag(int capacity, int domainSize)
    {
        this.capacity = capacity;
        currentSize = 0;
        items = new HashSet<>();
        domain = new boolean[domainSize];
    }
    
    /**
     * canAdd needs to be used before this method is called
     * @param item 
     */
    public boolean add(Item item)
    {
        if (canAdd(item))
        {
            items.add(item);
            currentSize += item.getSize();
            addConstraints(item);
            return true;
        }
        return false;
    }

    public HashSet<Item> getItems()
    {
        return items;
    }
    
    public void remove(Item item)
    {
    	if (items.contains(item))
    	{
            items.remove(item);
            removeConstraints(item);
            currentSize -= item.getSize();
    	}
    }
    
    public boolean isFull()
    {
        return capacity <= currentSize;
    }

    public boolean canAdd(Item i)
    {
        if (i.getSize() > (capacity - currentSize) || isFull()) return false;
        for (Item item : items)
            if (!item.canPackWith(i.getIndex()) || !i.canPackWith(item.getIndex()))
                return false;
        return true;
    }
    
    public void addConstraints(Item i)
    {
        boolean[] itemConst = i.getConstraints();
        
        if (itemConst == null) return;
        
        for (int j = 0; j < itemConst.length; j++)
        {
            if (itemConst[j])
            {
                domain[j] = true;
            }
        }
    }
    
    public void removeConstraints(Item i)
    {
        boolean[] itemConst = i.getConstraints();
        
        if (itemConst == null) return;
        
        for (int j = 0; j < itemConst.length; j++)
        {
            if (itemConst[j])
            {
                domain[j] = false;
            }
        }
    }
    
    public int constraintsLeft()
    {
        int count = 0;
        for (boolean bool : domain)
        {
            if (!bool)
            {
                ++count;
            }
        }
        return count;
    }
    
    public int remainingCapacity()
    {
        return capacity - currentSize;
    }
    
    @Override
    public String toString()
    {
    	String toString ="";
    	if (!items.isEmpty())
    	{
            for (Item item : items)
            {
                toString += item.toString() + " ";
            }
            toString += "\n";
    	}
        return toString;
    }

    public boolean[] getDomain()
    {
        return domain;
    }

    @Override
    public int compareTo(Object o)
    {
        Bag that = (Bag)o;
        int thatsConstraints = that.constraintsLeft() + that.remainingCapacity()*2;
        int thissConstraints = this.constraintsLeft() + this.remainingCapacity()*2;
        return Integer.compare(thatsConstraints, thissConstraints);
    }
    
    
}
