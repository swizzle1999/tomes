package com.swizzle.tomes.TomeTypes;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.swizzle.tomes.Tomes;
import jdk.nashorn.internal.parser.JSONParser;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


public class Rewards {

    private HashMap<ItemStack, Integer> rewards;
    private String tomeName;

    public Rewards(String tomeName){
        rewards = new HashMap<ItemStack, Integer>();
        this.tomeName = tomeName;
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

    public void addRewardToHashMap(ItemStack item, Integer probability){
        rewards.put(item, probability);
        saveObjectToFile();
        parseFileToObject();
    }

    public void printRewards(){
        System.out.println(rewards.toString());
    }

    private void parseFileToObject(){

        List<ItemStack> items = (List<ItemStack>)Tomes.getInstance().getConfig().get("loottable.wood");

        for (int i = 0; i < items.size(); i++){
            System.out.println(items.get(i).getData());
        }

//        ObjectMapper mapper = new ObjectMapper();
//
//        File file = new File(Tomes.getInstance().getDataFolder() + "/" + this.tomeName + ".json");
//        HashMap<String, Integer> rewardsStrings = null;
//        try {
//            rewardsStrings = mapper.readValue(file, HashMap.class);
//        } catch (IOException e){
//            System.out.println("Error");
//        }
//
//        for (Map.Entry<String, Integer> entry : rewardsStrings.entrySet()){
//            JsonObject object = null;
//            try{
//                object = new JsonParser().parse(entry.getKey()).getAsJsonObject();
//            } catch (Exception e){
//                System.out.println("error");
//            }
//
//            System.out.println(object.toString());
//        }
    }

    private void saveObjectToFile(){
        ObjectMapper mapper = new ObjectMapper();

        try {
            File file = new File(Tomes.getInstance().getDataFolder() + "/" + this.tomeName + ".json");
            mapper.writeValue(file, rewards);

        } catch (IOException e){
            System.out.println("error");
        }
    }
}
