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
    private SlayerBundle slayerBundle;

    public final String questName = "Slayer";

    public final NamespacedKey questGoalKey = new NamespacedKey(Tomes.getInstance(), "SlayerTarget");
    public final NamespacedKey questCurrentKey = new NamespacedKey(Tomes.getInstance(), "SlayerCurrent");
    public final NamespacedKey questTargetEntity = new NamespacedKey(Tomes.getInstance(), "SlayerEntityType");

    public final ArrayList<EntityType> entityTypes = new ArrayList<EntityType>(Arrays.asList(EntityType.ZOMBIE, EntityType.SKELETON, EntityType.CREEPER, EntityType.SPIDER, EntityType.ENDERMAN));

    //Variables that get parsed from book into this class
    public int currentMobCount;
    public int targetMobCount;
    public EntityType entityType;

    public void incrementCurrentMobCount(int incrementAmmount){
        currentMobCount += incrementAmmount;
    }

//    public void setLoreText(ArrayList<String> loreText) {
//        loreText = loreText;
//    }

    public ItemStack applyQuest(ItemStack book, int targetMobCount){
        this.currentMobCount = 0;
        this.targetMobCount = targetMobCount;
        this.entityType = pickRandomMob();

        ItemMeta tomeMeta = book.getItemMeta();

        tomeMeta.getPersistentDataContainer().set(questCurrentKey, PersistentDataType.INTEGER, this.currentMobCount);
        tomeMeta.getPersistentDataContainer().set(questGoalKey, PersistentDataType.INTEGER, this.targetMobCount);
        tomeMeta.getPersistentDataContainer().set(questTargetEntity, PersistentDataType.STRING, EntityType.ZOMBIE.toString());

        List<String> loreTextArray = tomeMeta.getLore();

        //The lore is likley empty so creating a new empty array so that no null pointer exception occurs
        if (loreTextArray == null){
            ArrayList<String> newLoreTextArray = new ArrayList<>();
            tomeMeta.setLore(newLoreTextArray);
            loreTextArray = tomeMeta.getLore();
        }

        String loreText = questName + " | " + tomeMeta.getPersistentDataContainer().get(questCurrentKey, PersistentDataType.INTEGER).toString() + "/" + tomeMeta.getPersistentDataContainer().get(questGoalKey, PersistentDataType.INTEGER).toString() + " " + this.entityType.toString().toLowerCase() + "(s)" + " Killed";
        loreTextArray.add(loreText);

        tomeMeta.setLore(loreTextArray);

        book.setItemMeta(tomeMeta);

        return book;
    }

    public ItemStack updateQuestData(ItemStack book){

        ItemMeta tomeMeta = book.getItemMeta();

        tomeMeta.getPersistentDataContainer().set(questCurrentKey, PersistentDataType.INTEGER, this.currentMobCount);
        tomeMeta.getPersistentDataContainer().set(questGoalKey, PersistentDataType.INTEGER, this.targetMobCount);
        tomeMeta.getPersistentDataContainer().set(questTargetEntity, PersistentDataType.STRING, EntityType.ZOMBIE.toString());

        List<String> tomeLore = tomeMeta.getLore();

        for(int i = 0; i < tomeLore.size(); i++){
            String[] splitLine = tomeLore.get(i).split(" ");
            if (splitLine[0].equalsIgnoreCase(questName)){
                tomeLore.set(i, questName + " | " + tomeMeta.getPersistentDataContainer().get(questCurrentKey, PersistentDataType.INTEGER).toString() + "/" + tomeMeta.getPersistentDataContainer().get(questGoalKey, PersistentDataType.INTEGER).toString() + " Killed");
                break;
            }
        }

        tomeMeta.setLore(tomeLore);
        book.setItemMeta(tomeMeta);

        return book;
    }

    public EntityType pickRandomMob(){
        Random random = new Random();
        return entityTypes.get(random.nextInt(entityTypes.size()));
    }

    @Override
    public String getQuestName() {
        return questName;
    }

    public void parseIntoObject(int currentMobCount, int targetMobCount, EntityType entityType) {
        this.currentMobCount = currentMobCount;
        this.targetMobCount = targetMobCount;
        this.entityType = entityType;
    }

}
