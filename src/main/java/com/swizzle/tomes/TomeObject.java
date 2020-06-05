package com.swizzle.tomes;

import com.swizzle.tomes.QuestTypes.IQuest;
import com.swizzle.tomes.QuestTypes.Mine;
import com.swizzle.tomes.QuestTypes.Slayer;
import com.swizzle.tomes.enums.QuestType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TomeObject {

    private static ArrayList<IQuest> questTypes = new ArrayList<IQuest>(Arrays.asList(new Slayer(), new Mine()));

    public static IQuest chooseRandomQuest(){
        Random random = new Random();
        return questTypes.get(random.nextInt(questTypes.size()));
    }

    public static ItemStack giveBook(String title){
        ItemStack tome = new ItemStack(Material.BOOK);

        ItemMeta tomeMeta = tome.getItemMeta();

        ArrayList<String> tomeLore = new ArrayList<>();
        IQuest questType = chooseRandomQuest();

//        String loreText = "";

//        switch(questType){
//            case inst:
//                loreText = "Kill Mobs";
//                break;
//            case MINE:
//                loreText = "Mine Blocks";
//                break;
//        }

        tomeLore.add(questType.getQuestName());
        tomeLore.add("----------");
//        tomeLore.add(loreText);
//        tomeMeta.setLore(tomeLore);

        if (questType instanceof Mine){
            System.out.println("Its a mine book");
        } else if (questType instanceof Slayer){
            NamespacedKey questGoalKey = new NamespacedKey(Tomes.getInstance(), "Goal");
            NamespacedKey questCurrentKey = new NamespacedKey(Tomes.getInstance(), "Current");

            tomeMeta.getPersistentDataContainer().set(questGoalKey, PersistentDataType.INTEGER, 3);
            tomeMeta.getPersistentDataContainer().set(questCurrentKey, PersistentDataType.INTEGER, 0);

            tomeLore.add(tomeMeta.getPersistentDataContainer().get(questCurrentKey, PersistentDataType.INTEGER).toString() + "/" + tomeMeta.getPersistentDataContainer().get(questGoalKey, PersistentDataType.INTEGER).toString());
        }
        tomeMeta.setLore(tomeLore);

        tomeMeta.setDisplayName(title);

        NamespacedKey tomeKey = new NamespacedKey(Tomes.getInstance(), "Tome");
        tomeMeta.getPersistentDataContainer().set(tomeKey, PersistentDataType.INTEGER, 1);

        NamespacedKey questTypeKey = new NamespacedKey(Tomes.getInstance(), "QuestType");
        tomeMeta.getPersistentDataContainer().set(questTypeKey, PersistentDataType.STRING, questType.getQuestName());

        tome.setItemMeta(tomeMeta);

        return tome;
    }



}
