package com.swizzle.tomes;

import com.google.common.collect.ImmutableMap;
import com.swizzle.tomes.QuestTypes.IQuest;
import com.swizzle.tomes.QuestTypes.Mine;
import com.swizzle.tomes.QuestTypes.Slayer;
import com.swizzle.tomes.TomeTypes.DirtTome;
import com.swizzle.tomes.TomeTypes.StoneTome;
import com.swizzle.tomes.TomeTypes.Tome;
import com.swizzle.tomes.TomeTypes.WoodTome;
import com.swizzle.tomes.enums.QuestType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.swing.text.html.parser.Entity;
import java.util.*;

public class TomeObject {

    public static final ArrayList<Tome> tomes = new ArrayList<Tome>(Arrays.asList(new DirtTome(), new WoodTome(), new StoneTome()));
    private static ArrayList<IQuest> questTypes = new ArrayList<IQuest>(Arrays.asList(new Slayer(0)));
    //private static ArrayList<IQuest> questTypes = new ArrayList<IQuest>(Arrays.asList(new Slayer(0), new Mine()));

    public static final NamespacedKey tomeKey = new NamespacedKey(Tomes.getInstance(), "Tome");
    public static final NamespacedKey tomeTypeKey = new NamespacedKey(Tomes.getInstance(), "TomeType");
    public static final NamespacedKey numberOfQuestsKey = new NamespacedKey(Tomes.getInstance(), "NumberOfQuests");
    public static final NamespacedKey tomeCompleteKey = new NamespacedKey(Tomes.getInstance(), "TomeComplete");

    public static IQuest chooseRandomQuest(ArrayList<IQuest> questsToChooseFrom){
        Random random = new Random();
        return questsToChooseFrom.get(random.nextInt(questsToChooseFrom.size()));
    }

    public static ItemStack giveBook(String title, int numberOfQuests, String tomeType){
        ItemStack tome = new ItemStack(Material.BOOK);
        ItemMeta tomeMeta = tome.getItemMeta();

        tomeMeta.getPersistentDataContainer().set(numberOfQuestsKey, PersistentDataType.INTEGER, numberOfQuests);

        tome.setItemMeta(tomeMeta);

        ArrayList<IQuest> questTypesCopy = new ArrayList<>(questTypes);
        for (int i = 0; i < numberOfQuests; i++){
            IQuest questType = chooseRandomQuest(questTypesCopy);

            tomeMeta = tome.getItemMeta();
            NamespacedKey questTypeKey = new NamespacedKey(Tomes.getInstance(), "QuestType"+i);
            tomeMeta.getPersistentDataContainer().set(questTypeKey, PersistentDataType.STRING, questType.getQuestName());
            System.out.println("THIS: " + tomeType);
            tomeMeta.getPersistentDataContainer().set(tomeTypeKey, PersistentDataType.STRING, tomeType);

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


        tomeMeta.getPersistentDataContainer().set(tomeKey, PersistentDataType.INTEGER, 1);

        tomeMeta.getPersistentDataContainer().set(tomeCompleteKey, PersistentDataType.INTEGER, 0);

        tome.setItemMeta(tomeMeta);

        return tome;
    }

    public static void checkIfTomeIsComplete(ItemStack tome){
        ItemMeta tomeMeta = tome.getItemMeta();

        int numberOfQuests = tomeMeta.getPersistentDataContainer().get(numberOfQuestsKey, PersistentDataType.INTEGER);

        int numberOfCompleteQuests = 0;
        for (int i = 0; i < numberOfQuests; i++){
            NamespacedKey questTypeKey = new NamespacedKey(Tomes.getInstance(), "QuestType"+i);
            String questTypeString = tomeMeta.getPersistentDataContainer().get(questTypeKey, PersistentDataType.STRING).toLowerCase();

            switch (questTypeString){
                case "slayer":
                    NamespacedKey questCurrentKey = new NamespacedKey(Tomes.getInstance(), "SlayerCurrent"+i);
                    NamespacedKey questTargetKey = new NamespacedKey(Tomes.getInstance(), "SlayerTarget"+i);
                    NamespacedKey questTargetEntity = new NamespacedKey(Tomes.getInstance(), "SlayerEntityType"+i);

                    Slayer slayer = new Slayer(i);

                    if (tomeMeta.getPersistentDataContainer().get(slayer.slayerCompletedKey, PersistentDataType.INTEGER) == 1){
                        numberOfCompleteQuests += 1;
                    }
                case "mine":
                    //TODO

            }
        }

        if (numberOfCompleteQuests == numberOfQuests){
            tomeMeta.getPersistentDataContainer().set(tomeCompleteKey, PersistentDataType.INTEGER, 1);

            List<String> completedTombLore = new ArrayList<>();
            completedTombLore.add("COMPLETE!");
            tomeMeta.setLore(completedTombLore);

            tome.setItemMeta(tomeMeta);
        }
    }




}
