package com.swizzle.tomes.TomeTypes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WoodRewards {

    public static HashMap<ItemStack, Integer> rewards = new HashMap<>();

    public WoodRewards(){
        ItemStack diamonds = new ItemStack(Material.DIAMOND, 64);
        addRewardToHashMap(diamonds, 1);

        ItemStack dirt = new ItemStack(Material.DIRT, 64);
        addRewardToHashMap(dirt, 5);
    }

    public void addRewardToHashMap(ItemStack item, Integer probability){
        rewards.put(item, probability);
    }
}
