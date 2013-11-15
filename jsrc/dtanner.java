//package com.reuben.dtanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 * @author reuben
 */
public class dtanner {
    private static int numBags;
    private static int maxBagSpace;
    private static HashSet<Item> items;
    private static LinkedList<String> initialItems = new LinkedList<>();
    
    public static void main(String[] args)
    {
        parseFile(Paths.get(args[0]));
        backPropagateIndices(items, initialItems);
        
        Bag[] bags = new Bag[numBags];
        
        for (int i = 0; i < numBags; i++)
        {
            bags[i] = new Bag(maxBagSpace, items.size()+1);
        }
        
        Solver solver = new Solver();
        
        System.out.print(solver.solve(bags, items));
        
    }
    
    private static void parseFile(Path paths)
    {
        items = new HashSet<>();
        try (BufferedReader reader = Files.newBufferedReader(paths, Charset.defaultCharset()))
        {
            String line = null;
            int lineCount = 0;
            while ((line = reader.readLine()) != null)
            {
                switch (lineCount)
                {
                    case 0:
                        try
                        {
                            numBags = Integer.parseInt(line.trim());
                        }
                        catch (NumberFormatException e)
                        {
                            System.err.println("File provided does not have correct form, please provide a new file");
                            System.exit(1);
                        }
                        break;
                    case 1:
                        try
                        {
                            maxBagSpace = Integer.parseInt(line.trim());
                        }
                        catch (NumberFormatException e)
                        {
                            System.err.println("File provided does not have correct form, please provide a new file");
                            System.exit(1);
                        }
                        break;
                    default:
                        items.add(parseItem(line.trim()));
                        break;
                }
                ++lineCount;
            }
        }
        catch (IOException e)
        {
            System.err.println("There was something wrong with the file provided, please provide a file with valid data.");
            System.exit(1);
        }
    }

    private static Item parseItem(String line)
    {
        String name = "";
        int weight = 0;
        char posneg = '\0';
        LinkedList<String> itemNames = new LinkedList<>();

        StringTokenizer tok = new StringTokenizer(line, " ");
        String token = "";
        int tokCount = 0;

        while (tok.hasMoreTokens())
        {
            switch (tokCount)
            {
                case 0:
                    name = tok.nextToken();
                    initialItems.add(name);
                    break;
                case 1:
                    try
                    {
                        weight = Integer.parseInt(tok.nextToken());
                    }
                    catch (NumberFormatException e)
                    {
                        System.err.println("This item was not properly formed, please provide a file with valid items");
                        System.exit(1);
                    }
                    break;
                case 2:
                        posneg = tok.nextToken().charAt(0);
                    break;
                default:
                        itemNames.add(tok.nextToken());
                    break;
            }
            ++tokCount;
        }
        return new Item(name, weight, posneg, itemNames);
    }
    
    public static boolean[] processBooleanArray(LinkedList<Integer> list, Boolean with)
    {
        if (with == null) return null;
        
        Integer[] nums = list.toArray(new Integer[0]);
        Arrays.sort(nums);
        int max = nums[nums.length-1];

        boolean[] bits = new boolean[max+1];
        boolean constrainedAGAINST;
        if (with)
        {
            Arrays.fill(bits, true);
            constrainedAGAINST = false;
        }
        else
        {
            constrainedAGAINST = true;
        }

        for (Integer integer : nums)
        {
            bits[integer] = constrainedAGAINST;
        }
        return bits;
    }

    private static void backPropagateIndices(HashSet<Item> items, LinkedList<String> initialItems)
    {
        for (Item item : items)
        {
            item.setIndex(initialItems.indexOf(item.getName()));
            LinkedList<Integer> indices = new LinkedList<>();
            LinkedList<String> constraintNames = item.getItemNames();
            if (constraintNames == null || constraintNames.isEmpty()) continue;
            for (String string : constraintNames)
            {
                indices.add(initialItems.indexOf(string));
            }
            item.setConstraints(processBooleanArray(indices, item.isWith()));
        }
    }
}
