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

public class Slayer implements IQuest {
    private final String questName = "Slayer";

    private int questIndex;

    //These are instance variables
    //They are appended with the index that the quest sits at in the tome
    private NamespacedKey slayerTargetKey;
    private NamespacedKey slayerCurrentKey;
    private NamespacedKey slayerTargetEntityKey;
    private NamespacedKey slayerCompletedKey;

    //These are for other classes to use so that there is no need to re create keys constantly
    private static final NamespacedKey slayerTargetKeyStatic = new NamespacedKey(Tomes.getInstance(), "SlayerTarget");
    private static final NamespacedKey slayerCurrentKeyStatic = new NamespacedKey(Tomes.getInstance(), "SlayerCurrent");
    private static final NamespacedKey slayerTargetEntityKeyStatic = new NamespacedKey(Tomes.getInstance(), "SlayerEntityType");
    private static final NamespacedKey slayerCompletedKeyStatic = new NamespacedKey(Tomes.getInstance(), "SlayerCompleted");


    //Variables that get parsed from book into this class
    private int currentMobCount;
    private int targetMobCount;
    private EntityType entityType;

    public Slayer(int questIndex, EntityType entityType, int currentMobCount, int targetMobCount){

        this.entityType = entityType;
        this.targetMobCount = targetMobCount;
        this.currentMobCount = currentMobCount;

        //The quest index is the number that this specific quest is in the book
        //For example the first quest might be a mine quest
        //the second quest might be a slayer quest
        //So the quest index for this quest would then be 1. It basically adjusts the keys below so they look in the correct place
        //Also allows there to be multiple of the same quest type on the same book. Just some extra flexability really
        this.questIndex = questIndex;
        this.slayerTargetKey = new NamespacedKey(Tomes.getInstance(), Slayer.slayerTargetKeyStatic.getKey()+questIndex);//new NamespacedKey(Tomes.getInstance(), "SlayerTarget"+questIndex);
        this.slayerCurrentKey = new NamespacedKey(Tomes.getInstance(), Slayer.slayerCurrentKeyStatic.getKey()+questIndex);//"SlayerCurrent"+questIndex
        this.slayerTargetEntityKey = new NamespacedKey(Tomes.getInstance(), Slayer.slayerTargetEntityKeyStatic.getKey()+questIndex);//"SlayerEntityType"+questIndex
        this.slayerCompletedKey = new NamespacedKey(Tomes.getInstance(), Slayer.slayerCompletedKeyStatic.getKey()+questIndex);//"SlayerCompleted"+questIndex

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

    public ItemStack applyQuest(ItemStack book){
        ItemMeta tomeMeta = book.getItemMeta();

        tomeMeta.getPersistentDataContainer().set(slayerCurrentKey, PersistentDataType.INTEGER, this.currentMobCount);
        tomeMeta.getPersistentDataContainer().set(slayerTargetKey, PersistentDataType.INTEGER, this.targetMobCount);
        tomeMeta.getPersistentDataContainer().set(slayerTargetEntityKey, PersistentDataType.STRING, this.entityType.toString());
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
        tomeMeta.getPersistentDataContainer().set(slayerTargetEntityKey, PersistentDataType.STRING, this.entityType.toString());

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

    @Override
    public String getQuestName(){
        return questName;
    }

    public EntityType getEntityType(){
        return this.entityType;
    }

    public NamespacedKey getSlayerTargetKey() {
        return slayerTargetKey;
    }

    public NamespacedKey getSlayerCurrentKey() {
        return slayerCurrentKey;
    }

    public NamespacedKey getSlayerTargetEntityKey() {
        return slayerTargetEntityKey;
    }

    public NamespacedKey getSlayerCompletedKey() {
        return slayerCompletedKey;
    }

    public static NamespacedKey getSlayerTargetKeyStatic() {
        return slayerTargetKeyStatic;
    }

    public static NamespacedKey getSlayerCurrentKeyStatic() {
        return slayerCurrentKeyStatic;
    }

    public static NamespacedKey getSlayerTargetEntityKeyStatic() {
        return slayerTargetEntityKeyStatic;
    }

    public static NamespacedKey getSlayerCompletedKeyStatic() {
        return slayerCompletedKeyStatic;
    }
}
