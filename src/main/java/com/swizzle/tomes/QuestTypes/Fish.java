package com.swizzle.tomes.QuestTypes;

import com.swizzle.tomes.Tomes;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Fish implements IQuest {
    private final String questName = "Fish";

    private int questIndex;

    //These are instance variables
    //They are appended with the index that the quest sits at in the tome
    private NamespacedKey fishTargetKey;
    private NamespacedKey fishCurrentKey;
    private NamespacedKey fishCompletedKey;

    //These are for other classes to use so that there is no need to re create keys constantly
    private static final NamespacedKey fishTargetKeyStatic = new NamespacedKey(Tomes.getInstance(), "FishTarget");
    private static final NamespacedKey fishCurrentKeyStatic = new NamespacedKey(Tomes.getInstance(), "FishCurrent");
    private static final NamespacedKey fishCompletedKeyStatic = new NamespacedKey(Tomes.getInstance(), "FishCompleted");


    //Variables that get parsed from book into this class
    private int currentFishCount;
    private int targetFishCount;

    public Fish(int questIndex, int currentFishCount, int targetFishCount){
        
        this.targetFishCount = targetFishCount;
        this.currentFishCount = currentFishCount;

        //The quest index is the number that this specific quest is in the book
        //For example the first quest might be a mine quest
        //the second quest might be a slayer quest
        //So the quest index for this quest would then be 1. It basically adjusts the keys below so they look in the correct place
        //Also allows there to be multiple of the same quest type on the same book. Just some extra flexability really
        this.questIndex = questIndex;
        this.fishTargetKey = new NamespacedKey(Tomes.getInstance(), Fish.fishTargetKeyStatic.getKey()+questIndex);
        this.fishCurrentKey = new NamespacedKey(Tomes.getInstance(), Fish.fishCurrentKeyStatic.getKey()+questIndex);
        this.fishCompletedKey = new NamespacedKey(Tomes.getInstance(), Fish.fishCompletedKeyStatic.getKey()+questIndex);

    }

    public void incrementCurrentFishCount(int incrementAmmount, ItemStack book){
        ItemMeta tomeMeta = book.getItemMeta();
        int isQuestComplete = tomeMeta.getPersistentDataContainer().get(fishCompletedKey, PersistentDataType.INTEGER);
        if (isQuestComplete == 1){
            return;
        } else {
            if (currentFishCount + 1 >= targetFishCount){
                currentFishCount += 1;
                tomeMeta.getPersistentDataContainer().set(fishCompletedKey, PersistentDataType.INTEGER, 1);
                book.setItemMeta(tomeMeta);
                isQuestComplete = tomeMeta.getPersistentDataContainer().get(fishCompletedKey, PersistentDataType.INTEGER);
            } else {
                currentFishCount += incrementAmmount;
            }

            updateQuestData(book, isQuestComplete);
        }


    }
    
    public ItemStack applyQuest(ItemStack book){
        ItemMeta tomeMeta = book.getItemMeta();

        tomeMeta.getPersistentDataContainer().set(fishCurrentKey, PersistentDataType.INTEGER, this.currentFishCount);
        tomeMeta.getPersistentDataContainer().set(fishTargetKey, PersistentDataType.INTEGER, this.targetFishCount);
        tomeMeta.getPersistentDataContainer().set(fishCompletedKey, PersistentDataType.INTEGER, 0);

        List<String> loreTextArray = tomeMeta.getLore();

        //The lore is likley empty so creating a new empty array so that no null pointer exception occurs
        if (loreTextArray == null){
            ArrayList<String> newLoreTextArray = new ArrayList<>();
            tomeMeta.setLore(newLoreTextArray);
            loreTextArray = tomeMeta.getLore();
        }


        String loreText = getLoreText(0);
        loreTextArray.add(loreText);

        tomeMeta.setLore(loreTextArray);

        book.setItemMeta(tomeMeta);

        return book;
    }

    public ItemStack updateQuestData(ItemStack book, int isQuestComplete){

        ItemMeta tomeMeta = book.getItemMeta();

        tomeMeta.getPersistentDataContainer().set(fishCurrentKey, PersistentDataType.INTEGER, this.currentFishCount);
        tomeMeta.getPersistentDataContainer().set(fishTargetKey, PersistentDataType.INTEGER, this.targetFishCount);

        List<String> tomeLore = tomeMeta.getLore();

        for(int i = 0; i < tomeLore.size(); i++){
            String[] splitLine = tomeLore.get(i).split(" ");
            List<String> list = Arrays.asList(splitLine);
            if (splitLine[0].equalsIgnoreCase(questName) && list.contains("fish(s)")){
                tomeLore.set(i, getLoreText(isQuestComplete));
            }
        }

        tomeMeta.setLore(tomeLore);
        book.setItemMeta(tomeMeta);

        return book;
    }

    public String getLoreText(int isQuestComplete){
        String loreText = this.questName + " | " + this.currentFishCount + "/" + this.targetFishCount + " fish(s) caught";
        if (isQuestComplete == 1){
            loreText += " | COMPLETE";
        }
        return loreText;
    };

    @Override
    public String getQuestName(){
        return questName;
    }

    public NamespacedKey getFishTargetKey() {
        return fishTargetKey;
    }

    public NamespacedKey getFishCurrentKey() {
        return fishCurrentKey;
    }

    public NamespacedKey getFishCompletedKey() {
        return fishCompletedKey;
    }

    public static NamespacedKey getFishTargetKeyStatic() {
        return fishTargetKeyStatic;
    }

    public static NamespacedKey getFishCurrentKeyStatic() {
        return fishCurrentKeyStatic;
    }

    public static NamespacedKey getFishCompletedKeyStatic() {
        return fishCompletedKeyStatic;
    }
}
