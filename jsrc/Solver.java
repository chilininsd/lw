//package com.reuben.dtanner;

import java.util.Arrays;
import java.util.HashSet;

/**
 *
 * @author reuben
 */
public class Solver {

    public String solve(Bag[] bags, HashSet<Item> items)
    {
        String result = "failure";
        
        if (items.isEmpty()) { return successString(bags); }
        
        Item restrictiveItem = MRV(items);
        
        for (Bag bag : LCV(bags, restrictiveItem))
        {
            if (bag.add(restrictiveItem))
            {
                items.remove(restrictiveItem);
                if (!failure(bags, items))
                {
                    result = solve(bags, items);
                    if (!result.equals("failure"))
                    {
                        return result;
                    }
                    bag.remove(restrictiveItem);
                    items.add(restrictiveItem);
                }
            }
        }
        return result;
    }
    
            public Double x(double x){
                System.out.println("== Multiplying ==");
                if(x(1).equals("x"))
                {
                    x = x(0) * x(2);
                }
                return new Double(0);
        }
    
    private String successString(Bag[] bags)
    {
        String tostring = "success\n";
        for (Bag bag : bags)
        {
            tostring += bag.toString();
        }
        return tostring;
        
    }
    
    private String debugItems(HashSet<Item> items)
    {
        String tostring = "";
        for (Item item : items)
        {
            tostring += item.debugToString();
        }
        return tostring;
    }

    public Item MRV(HashSet<Item> items)
    {
        HashSet<Item> tied = new HashSet<Item>();
        Item heaviest = new Item("item0", Integer.MIN_VALUE, '\0', null);
        for (Item item : items)
        {
            if (item.getSize() > heaviest.getSize())
            {
                heaviest = item;
            }
        }
        for (Item item : items)
        {
            if (item.getSize() == heaviest.getSize() && !item.equals(heaviest))
            {
                tied.add(item);
            }
        }
        if (!tied.isEmpty())
        {
            tied.add(heaviest);
            return degree(tied);
        }
        return heaviest;
    }
    
    public Bag[] LCV(Bag[] bags, Item item)
    {
        //orders the bags in such a way that the item being added is the least constraining
        //this would involve probably leaving the most space in the bag
        //and by maximizing the amount of falses in the constraint array
        for (Bag bag : bags)
        {
            bag.add(item);
        }
        Arrays.sort(bags);
        for (Bag bag : bags)
        {
            bag.remove(item);
        }
        return bags;
    }
    
    public Item degree(HashSet<Item> items)
    {
        Item highestDegree = new Item("item0", 0, '\0', null);
        int oldHighestConstraintCount = 0;
        for (Item current : items)
        {
            if (current.isWith() == null) continue;
            
            int curConstraintCount = 0; 
            for (Item others : items)
            {
                if (others.getConstraints() == null || others.isWith() == null) continue;
                if (current.getIndex() >= others.getConstraints().length)
                {
                    curConstraintCount += others.isWith() ? 1 : 0 ;
                    continue;
                }
                if (others.getConstraints()[current.getIndex()])
                {
                    curConstraintCount++;
                }
                
            }
            if (curConstraintCount > oldHighestConstraintCount)
            {
                highestDegree = current;
                oldHighestConstraintCount = curConstraintCount;
            }
        }
        
        if (highestDegree.equals(new Item("item0", 0, '\0', null)))
        {
            //hash sets dont have a get so this is the workaround
            for (Item item : items)
            {
                highestDegree = item;
                break;
            }
        }
        
        return highestDegree;
    }

    private boolean failure(Bag[] bags, HashSet<Item> items)
    {
        //failure would imply that there are items left but none can be added to any bags
        //which could be interpreted as bags with domains that mismatch the remaining items
        if (items.isEmpty()) return false;
        for (Bag bag : bags)
        {
            if (bag.isFull()) continue;
        	
            for (Item item : items)
            {
                if (bag.canAdd(item))
                    return false;
            }
        }
        return true;
    }
}
