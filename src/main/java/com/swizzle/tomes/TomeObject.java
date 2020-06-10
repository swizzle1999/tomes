package com.swizzle.tomes;

import com.swizzle.tomes.QuestTypes.IQuest;
import com.swizzle.tomes.QuestTypes.Mine;
import com.swizzle.tomes.QuestTypes.Slayer;
import com.swizzle.tomes.TomeTypes.DirtTome;
import com.swizzle.tomes.TomeTypes.StoneTome;
import com.swizzle.tomes.TomeTypes.Tome;
import com.swizzle.tomes.TomeTypes.WoodTome;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class TomeObject {

    public static final ArrayList<Tome> tomes = new ArrayList<Tome>(Arrays.asList(new DirtTome(), new WoodTome(), new StoneTome()));
    //private static ArrayList<IQuest> questTypes = new ArrayList<IQuest>(Arrays.asList(new Slayer(0)));
    //private static ArrayList<IQuest> questTypes = new ArrayList<IQuest>(Arrays.asList(new Slayer(0), new Mine()));

    public static final NamespacedKey tomeKey = new NamespacedKey(Tomes.getInstance(), "Tome");
    public static final NamespacedKey tomeTypeKey = new NamespacedKey(Tomes.getInstance(), "TomeType");
    public static final NamespacedKey numberOfQuestsKey = new NamespacedKey(Tomes.getInstance(), "NumberOfQuests");
    public static final NamespacedKey tomeCompleteKey = new NamespacedKey(Tomes.getInstance(), "TomeComplete");

    public static IQuest chooseRandomQuest(ArrayList<IQuest> questsToChooseFrom){
        Random random = new Random();
        return questsToChooseFrom.get(random.nextInt(questsToChooseFrom.size()));
    }

//    public static ItemStack giveBook(String title, int numberOfQuests, String tomeType){
//        ItemStack tome = new ItemStack(Material.BOOK);
//        ItemMeta tomeMeta = tome.getItemMeta();
//
//        tomeMeta.getPersistentDataContainer().set(numberOfQuestsKey, PersistentDataType.INTEGER, numberOfQuests);
//
//        tome.setItemMeta(tomeMeta);
//
//        ArrayList<IQuest> questTypesCopy = new ArrayList<>(questTypes);
//        for (int i = 0; i < numberOfQuests; i++){
//            IQuest questType = chooseRandomQuest(questTypesCopy);
//
//            tomeMeta = tome.getItemMeta();
//            NamespacedKey questTypeKey = new NamespacedKey(Tomes.getInstance(), "QuestType"+i);
//            tomeMeta.getPersistentDataContainer().set(questTypeKey, PersistentDataType.STRING, questType.getQuestName());
//            System.out.println("THIS: " + tomeType);
//            tomeMeta.getPersistentDataContainer().set(tomeTypeKey, PersistentDataType.STRING, tomeType);
//
//            tome.setItemMeta(tomeMeta);
//
//            //Applying unique quest data
//            if (questType instanceof Mine){
//
//            } else if (questType instanceof Slayer){
//                Slayer slayer = new Slayer(i);
//                tome = slayer.applyQuest(tome,  3);
//            }
//        }
//
//        tomeMeta = tome.getItemMeta();
//
//        tomeMeta.setDisplayName(title);
//
//
//        tomeMeta.getPersistentDataContainer().set(tomeKey, PersistentDataType.INTEGER, 1);
//        tomeMeta.getPersistentDataContainer().set(tomeCompleteKey, PersistentDataType.INTEGER, 0);
//
//        tome.setItemMeta(tomeMeta);
//
//        return tome;
//    }

}
