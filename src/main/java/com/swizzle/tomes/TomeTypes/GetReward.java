package com.swizzle.tomes.TomeTypes;

import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GetReward {
    public static ItemStack chooseReward(HashMap<ItemStack, Integer> rewards){
        ArrayList<ItemStack> finalRewardsList = new ArrayList<ItemStack>();
        for (Map.Entry<ItemStack, Integer> entry : rewards.entrySet()){
            for (int i = 0; i < entry.getValue(); i++){
                finalRewardsList.add(entry.getKey());
            }
        }

        Random random = new Random();
        return finalRewardsList.get(random.nextInt(finalRewardsList.size()));
    }
}
