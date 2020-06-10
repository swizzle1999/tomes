package com.swizzle.tomes.QuestTypes;

import com.swizzle.tomes.TomeObject;
import com.swizzle.tomes.Tomes;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Mine implements IQuest {
    private final String questName = "Mine";

    private int questIndex;

    //These are instance variables
    //They are appended with the index that the quest sits at in the tome
    private NamespacedKey mineTargetKey;
    private NamespacedKey mineCurrentKey;
    private NamespacedKey mineTargetMaterialKey;
    private NamespacedKey mineCompletedKey;

    //These are for other classes to use so that there is no need to re create keys constantly
    public static final NamespacedKey mineTargetKeyStatic = new NamespacedKey(Tomes.getInstance(), "MineTarget");
    public static final NamespacedKey mineCurrentKeyStatic = new NamespacedKey(Tomes.getInstance(), "MineCurrent");
    public static final NamespacedKey mineTargetMaterialKeyStatic = new NamespacedKey(Tomes.getInstance(), "MineMaterial");
    public static final NamespacedKey mineCompletedKeyStatic = new NamespacedKey(Tomes.getInstance(), "MineCompleted");

    //Variables that get parsed from book into this class
    private int currentMineCount;
    private int targetMineCount;
    private Material materialType;

    public Mine(int questIndex, Material materialType, int currentMineCount, int targetMineCount){

        this.materialType = materialType;
        this.targetMineCount = targetMineCount;
        this.currentMineCount = currentMineCount;

        //The quest index is the number that this specific quest is in the book
        //For example the first quest might be a mine quest
        //the second quest might be a slayer quest
        //So the quest index for this quest would then be 1. It basically adjusts the keys below so they look in the correct place
        //Also allows there to be multiple of the same quest type on the same book. Just some extra flexability really
        this.questIndex = questIndex;
        this.mineTargetKey = new NamespacedKey(Tomes.getInstance(), "MineTarget"+questIndex);
        this.mineCurrentKey = new NamespacedKey(Tomes.getInstance(), "MineCurrent"+questIndex);
        this.mineTargetMaterialKey = new NamespacedKey(Tomes.getInstance(), "MineMaterial"+questIndex);
        this.mineCompletedKey = new NamespacedKey(Tomes.getInstance(), "MineCompleted"+questIndex);
    }

    public void incrementCurrentMineCount(int incrementAmmount, ItemStack book){
        ItemMeta tomeMeta = book.getItemMeta();
        int isQuestComplete = tomeMeta.getPersistentDataContainer().get(mineCompletedKey, PersistentDataType.INTEGER);
        if (isQuestComplete == 1){
            return;
        } else {
            if (currentMineCount + 1 >= targetMineCount){
                currentMineCount += 1;
                tomeMeta.getPersistentDataContainer().set(mineCompletedKey, PersistentDataType.INTEGER, 1);
                book.setItemMeta(tomeMeta);
                isQuestComplete = tomeMeta.getPersistentDataContainer().get(mineCompletedKey, PersistentDataType.INTEGER);
            } else {
                currentMineCount += incrementAmmount;
            }

            updateQuestData(book, isQuestComplete);
        }


    }

//    public void setLoreText(ArrayList<String> loreText) {
//        loreText = loreText;
//    }

    public ItemStack applyQuest(ItemStack book){
        ItemMeta tomeMeta = book.getItemMeta();

        tomeMeta.getPersistentDataContainer().set(mineCurrentKey, PersistentDataType.INTEGER, this.currentMineCount);
        tomeMeta.getPersistentDataContainer().set(mineTargetKey, PersistentDataType.INTEGER, this.targetMineCount);
        tomeMeta.getPersistentDataContainer().set(mineTargetMaterialKey, PersistentDataType.STRING, this.materialType.toString());
        tomeMeta.getPersistentDataContainer().set(mineCompletedKey, PersistentDataType.INTEGER, 0);

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

        tomeMeta.getPersistentDataContainer().set(mineCurrentKey, PersistentDataType.INTEGER, this.currentMineCount);
        tomeMeta.getPersistentDataContainer().set(mineTargetKey, PersistentDataType.INTEGER, this.targetMineCount);
        tomeMeta.getPersistentDataContainer().set(mineTargetMaterialKey, PersistentDataType.STRING, this.materialType.toString());

        List<String> tomeLore = tomeMeta.getLore();

        for(int i = 0; i < tomeLore.size(); i++){
            String[] splitLine = tomeLore.get(i).split(" ");
            List<String> list = Arrays.asList(splitLine);
            if (splitLine[0].equalsIgnoreCase(questName) && list.contains(this.materialType.toString().toLowerCase()+"(s)")){
                tomeLore.set(i, getLoreText(isQuestComplete));
            }
        }

        tomeMeta.setLore(tomeLore);
        book.setItemMeta(tomeMeta);

        return book;
    }

    public String getLoreText(int isQuestComplete){
        String loreText = this.questName + " | " + this.currentMineCount + "/" + this.targetMineCount + " " + this.materialType.toString().toLowerCase() + "(s)" + " Mined";
        if (isQuestComplete == 1){
            loreText += " | COMPLETE";
        }
        return loreText;
    };

    @Override
    public String getQuestName(){
        return questName;
    }

    public Material getMaterial(){
        return this.materialType;
    }

    public NamespacedKey getMineTargetKey() {
        return mineTargetKey;
    }

    public NamespacedKey getMineCurrentKey() {
        return mineCurrentKey;
    }

    public NamespacedKey getMineTargetMaterialKey() {
        return mineTargetMaterialKey;
    }

    public NamespacedKey getMineCompletedKey() {
        return mineCompletedKey;
    }

}
