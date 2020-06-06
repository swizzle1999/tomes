package com.swizzle.tomes;

import com.swizzle.tomes.QuestTypes.IQuest;
import com.swizzle.tomes.QuestTypes.Mine;
import com.swizzle.tomes.QuestTypes.Slayer;
import com.swizzle.tomes.enums.QuestType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TomeObject {

    private static ArrayList<IQuest> questTypes = new ArrayList<IQuest>(Arrays.asList(new Slayer(0)));
    //private static ArrayList<IQuest> questTypes = new ArrayList<IQuest>(Arrays.asList(new Slayer(0), new Mine()));

    public static IQuest chooseRandomQuest(ArrayList<IQuest> questsToChooseFrom){
        Random random = new Random();
        return questsToChooseFrom.get(random.nextInt(questsToChooseFrom.size()));
    }

    public static ItemStack giveBook(String title, int numberOfQuests){
        ItemStack tome = new ItemStack(Material.BOOK);
        ItemMeta tomeMeta = tome.getItemMeta();

        NamespacedKey numberOfQuestsKey = new NamespacedKey(Tomes.getInstance(), "NumberOfQuests");
        tomeMeta.getPersistentDataContainer().set(numberOfQuestsKey, PersistentDataType.INTEGER, numberOfQuests);

        tome.setItemMeta(tomeMeta);

        ArrayList<IQuest> questTypesCopy = new ArrayList<>(questTypes);
        for (int i = 0; i < numberOfQuests; i++){
            IQuest questType = chooseRandomQuest(questTypesCopy);

            tomeMeta = tome.getItemMeta();
            NamespacedKey questTypeKey = new NamespacedKey(Tomes.getInstance(), "QuestType"+i);
            tomeMeta.getPersistentDataContainer().set(questTypeKey, PersistentDataType.STRING, questType.getQuestName());
            tome.setItemMeta(tomeMeta);

            //Applying unique quest data
            if (questType instanceof Mine){

            } else if (questType instanceof Slayer){
                Slayer slayer = new Slayer(i);
                tome = slayer.applyQuest(tome,  3);
            }
        }

        tomeMeta = tome.getItemMeta();

//        String loreText = "";

//        switch(questType){
//            case inst:
//                loreText = "Kill Mobs";
//                break;
//            case MINE:
//                loreText = "Mine Blocks";
//                break;
//        }

//        tomeLore.add(loreText);
//        tomeMeta.setLore(tomeLore);



        //Data that is common between all books

        tomeMeta.setDisplayName(title);

        NamespacedKey tomeKey = new NamespacedKey(Tomes.getInstance(), "Tome");
        tomeMeta.getPersistentDataContainer().set(tomeKey, PersistentDataType.INTEGER, 1);

        NamespacedKey tomeCompleteKey = new NamespacedKey(Tomes.getInstance(), "TomeComplete");
        tomeMeta.getPersistentDataContainer().set(tomeCompleteKey, PersistentDataType.INTEGER, 0);

        tome.setItemMeta(tomeMeta);

        return tome;
    }



}
