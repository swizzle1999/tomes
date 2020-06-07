package com.swizzle.tomes.TomeTypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swizzle.tomes.Tomes;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class Rewards {

    private HashMap<ItemStack, Integer> rewards;

    public Rewards(){
        rewards = new HashMap<ItemStack, Integer>();
    }

    public ItemStack chooseReward(HashMap<ItemStack, Integer> rewards){
        ArrayList<ItemStack> finalRewardsList = new ArrayList<ItemStack>();
        for (Map.Entry<ItemStack, Integer> entry : rewards.entrySet()){
            for (int i = 0; i < entry.getValue(); i++){
                finalRewardsList.add(entry.getKey());
            }
        }

        Random random = new Random();
        return finalRewardsList.get(random.nextInt(finalRewardsList.size()));
    }

    public void addRewardToHashMap(String tomeName, ItemStack item, Integer probability){
        parseFileToObject(tomeName);
        rewards.put(item, probability);
    }

    public void printRewards(){
        System.out.println(rewards.toString());
    }

    public void parseFileToObject(String tomeName){
        ObjectMapper mapper = new ObjectMapper();

        File file = new File(Tomes.getInstance().getDataFolder() + "/" + tomeName + ".json");

        try {
            rewards = mapper.readValue(file, HashMap.class);
        } catch (IOException e){
            System.out.println("Error");
        }

        for (Map.Entry<ItemStack, Integer> entry : rewards.entrySet()){
            System.out.println(entry.getKey().getData());
        }
    }

    public void saveObjectToFile(String tomeName){
        ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writeValue(new File(Tomes.getInstance().getDataFolder() + "/" + tomeName + ".json"), rewards);
        } catch (IOException e){
            System.out.println("error");
        }
    }
}
