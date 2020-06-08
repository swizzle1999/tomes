package com.swizzle.tomes.QuestTypes;

import com.swizzle.tomes.TomeObject;
import com.swizzle.tomes.Tomes;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Slayer implements IQuest {
    private final String questName = "Slayer";

    private int questIndex;

    public NamespacedKey slayerTargetKey;
    public NamespacedKey slayerCurrentKey;
    public NamespacedKey slayerTargetEntity;
    public NamespacedKey slayerCompletedKey;

    public final ArrayList<EntityType> entityTypes = new ArrayList<EntityType>(Arrays.asList(EntityType.ZOMBIE, EntityType.SKELETON, EntityType.CREEPER, EntityType.SPIDER, EntityType.ENDERMAN));

    //Variables that get parsed from book into this class
    public int currentMobCount;
    public int targetMobCount;
    public EntityType entityType;

    public Slayer(int questIndex){
        //The quest index is the number that this specific quest is in the book
        //For example the first quest might be a mine quest
        //the second quest might be a slayer quest
        //So the quest index for this quest would then be 1. It basically adjusts the keys below so they look in the correct place
        //Also allows there to be multiple of the same quest type on the same book. Just some extra flexability really
        this.questIndex = questIndex;
        this.slayerTargetKey = new NamespacedKey(Tomes.getInstance(), "SlayerTarget"+questIndex);
        this.slayerCurrentKey = slayerCurrentKey = new NamespacedKey(Tomes.getInstance(), "SlayerCurrent"+questIndex);
        this.slayerTargetEntity = new NamespacedKey(Tomes.getInstance(), "SlayerEntityType"+questIndex);
        this.slayerCompletedKey = new NamespacedKey(Tomes.getInstance(), "SlayerCompleted"+questIndex);
    }

    public void incrementCurrentMobCount(int incrementAmmount, ItemStack book){
        ItemMeta tomeMeta = book.getItemMeta();
        int isQuestComplete = tomeMeta.getPersistentDataContainer().get(slayerCompletedKey, PersistentDataType.INTEGER);
        if (isQuestComplete == 1){
            return;
        } else {
            if (currentMobCount + 1 >= targetMobCount){
                currentMobCount += 1;
                tomeMeta.getPersistentDataContainer().set(slayerCompletedKey, PersistentDataType.INTEGER, 1);
                book.setItemMeta(tomeMeta);
                isQuestComplete = tomeMeta.getPersistentDataContainer().get(slayerCompletedKey, PersistentDataType.INTEGER);
            } else {
                currentMobCount += incrementAmmount;
            }

            updateQuestData(book, isQuestComplete);
        }


    }

//    public void setLoreText(ArrayList<String> loreText) {
//        loreText = loreText;
//    }

    public ItemStack applyQuest(ItemStack book, int targetMobCount){
        this.currentMobCount = 0;
        this.targetMobCount = targetMobCount;
        this.entityType = pickRandomMob();

        ItemMeta tomeMeta = book.getItemMeta();

        tomeMeta.getPersistentDataContainer().set(slayerCurrentKey, PersistentDataType.INTEGER, this.currentMobCount);
        tomeMeta.getPersistentDataContainer().set(slayerTargetKey, PersistentDataType.INTEGER, this.targetMobCount);
        tomeMeta.getPersistentDataContainer().set(slayerTargetEntity, PersistentDataType.STRING, this.entityType.toString());
        tomeMeta.getPersistentDataContainer().set(slayerCompletedKey, PersistentDataType.INTEGER, 0);

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

        tomeMeta.getPersistentDataContainer().set(slayerCurrentKey, PersistentDataType.INTEGER, this.currentMobCount);
        tomeMeta.getPersistentDataContainer().set(slayerTargetKey, PersistentDataType.INTEGER, this.targetMobCount);
        tomeMeta.getPersistentDataContainer().set(slayerTargetEntity, PersistentDataType.STRING, this.entityType.toString());

        List<String> tomeLore = tomeMeta.getLore();

        for(int i = 0; i < tomeLore.size(); i++){
            String[] splitLine = tomeLore.get(i).split(" ");
            List<String> list = Arrays.asList(splitLine);
            if (splitLine[0].equalsIgnoreCase(questName) && list.contains(this.entityType.toString().toLowerCase()+"(s)")){
                tomeLore.set(i, getLoreText(isQuestComplete));
            }
        }

        tomeMeta.setLore(tomeLore);
        book.setItemMeta(tomeMeta);

        return book;
    }

    public String getLoreText(int isQuestComplete){
        String loreText = this.questName + " | " + this.currentMobCount + "/" + this.targetMobCount + " " + this.entityType.toString().toLowerCase() + "(s)" + " Killed";
        if (isQuestComplete == 1){
            loreText += " | COMPLETE";
        }
        return loreText;
    };

    public EntityType pickRandomMob(){
        Random random = new Random();
        return entityTypes.get(random.nextInt(entityTypes.size()));
    }

    @Override
    public String getQuestName(){
        return questName;
    }


    public void parseIntoObject(int currentMobCount, int targetMobCount, EntityType entityType) {
        this.currentMobCount = currentMobCount;
        this.targetMobCount = targetMobCount;
        this.entityType = entityType;
    }

}
