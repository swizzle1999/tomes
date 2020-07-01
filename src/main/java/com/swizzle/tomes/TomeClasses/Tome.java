package com.swizzle.tomes.TomeClasses;

import com.swizzle.tomes.QuestTypes.*;
import com.swizzle.tomes.Tomes;
import com.swizzle.tomes.Utils.RandomNumberBetweenBounds;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tome{
    private String tomeVariableName;
    private String tomeDisplayName;
    private int numberOfQuests;
    private int cost;

    private List<IQuest> availableQuests = new ArrayList<IQuest>();
    private List<ItemStack> rewards = new ArrayList<ItemStack>();

    private SlayerTomeCustomization slayerTomeCustomization;
    private MineTomeCustomization mineTomeCustomization;
    private FishTomeCustomization fishTomeCustomization;

    private Random random = new Random();

    //Generic Tome Keys
    private static final NamespacedKey tomeKey = new NamespacedKey(Tomes.getInstance(), "Tome");
    private static final NamespacedKey tomeTypeKey = new NamespacedKey(Tomes.getInstance(), "TomeType");
    private static final NamespacedKey tomeNumberOfQuestsKey = new NamespacedKey(Tomes.getInstance(), "TomeNumberOfQuests");
    private static final NamespacedKey tomeCompleteKey = new NamespacedKey(Tomes.getInstance(), "TomeComplete");

    public Tome(String tomeVariableName, String tomeDisplayName, int numberOfQuests, int cost, List<IQuest> availableQuests, ArrayList<ItemStack> rewards){
        this.tomeVariableName = tomeVariableName;
        this.tomeDisplayName = tomeDisplayName;
        this.numberOfQuests = numberOfQuests;
        this.cost = cost;
        this.availableQuests = availableQuests;
        this.rewards = rewards;

        //Itterate through all the available quests and find the exact configuration for this specific tome
        for (IQuest quest : availableQuests){
            if (quest instanceof Slayer){
                //Slayer Section
                ArrayList<EntityType> entities = new ArrayList<EntityType>();
                ArrayList<Integer> minNums = new ArrayList<Integer>();
                ArrayList<Integer> maxNums = new ArrayList<Integer>();

                ConfigurationSection slayerEntitiesConfig = Tomes.getInstance().getConfig().getConfigurationSection("tomes." + this.tomeVariableName + ".quests.slayer.entities");
                for (String key : slayerEntitiesConfig.getKeys(false)){
                    EntityType entityType = EntityType.valueOf(Tomes.getInstance().getConfig().getString("tomes." + this.tomeVariableName + ".quests.slayer.entities."+key));
                    int minNum = Tomes.getInstance().getConfig().getInt("tomes." + this.tomeVariableName + ".quests.slayer.minNumbers."+key);
                    int maxNum = Tomes.getInstance().getConfig().getInt("tomes." + this.tomeVariableName + ".quests.slayer.maxNumbers."+key);

                    if (entityType == null || minNum == 0 || maxNum == 0){
                        System.out.println("ERROR CONFIGURATION IS WRONG SOMEWHERE");
                    }
                    entities.add(entityType);
                    minNums.add(minNum);
                    maxNums.add(maxNum);
                }

                this.slayerTomeCustomization = new SlayerTomeCustomization(entities, minNums, maxNums);
            } else if (quest instanceof Mine){
                //Mine Section
                ArrayList<Material> materials = new ArrayList<Material>();
                ArrayList<Integer> minNums = new ArrayList<Integer>();
                ArrayList<Integer> maxNums = new ArrayList<Integer>();

                ConfigurationSection mineMaterialsConfig = Tomes.getInstance().getConfig().getConfigurationSection("tomes." + this.tomeVariableName + ".quests.mine.materials");
                for (String key : mineMaterialsConfig.getKeys(false)){
                    Material material = Material.valueOf(Tomes.getInstance().getConfig().getString("tomes." + this.tomeVariableName + ".quests.mine.materials."+key));
                    int minNum = Tomes.getInstance().getConfig().getInt("tomes." + this.tomeVariableName + ".quests.mine.minNumbers."+key);
                    int maxNum = Tomes.getInstance().getConfig().getInt("tomes." + this.tomeVariableName + ".quests.mine.maxNumbers."+key);

                    if (material == null || minNum == 0 || maxNum == 0){
                        System.out.println("ERROR CONFIGURATION IS WRONG SOMEWHERE");
                    }
                    materials.add(material);
                    minNums.add(minNum);
                    maxNums.add(maxNum);
                }

                this.mineTomeCustomization = new MineTomeCustomization(materials, minNums, maxNums);
            } else if (quest instanceof Fish){
                //Fish Section
                ArrayList<Integer> minNums = new ArrayList<Integer>();
                ArrayList<Integer> maxNums = new ArrayList<Integer>();

                ConfigurationSection fishConfig = Tomes.getInstance().getConfig().getConfigurationSection("tomes." + this.tomeVariableName + ".quests.fish.minNumbers");
                for (String key : fishConfig.getKeys(false)){
                    int minNum = Tomes.getInstance().getConfig().getInt("tomes." + this.tomeVariableName + ".quests.fish.minNumbers."+key);
                    int maxNum = Tomes.getInstance().getConfig().getInt("tomes." + this.tomeVariableName + ".quests.fish.maxNumbers."+key);

                    if (minNum == 0 || maxNum == 0){
                        System.out.println("ERROR CONFIGURATION IS WRONG SOMEWHERE");
                    }
                    minNums.add(minNum);
                    maxNums.add(maxNum);
                }

                this.fishTomeCustomization = new FishTomeCustomization(minNums, maxNums);
            }
        }
    }
    
    public ItemStack giveBook() {
        ItemStack tome = new ItemStack(Material.BOOK);
        ItemMeta tomeMeta = tome.getItemMeta();

        tomeMeta.getPersistentDataContainer().set(getTomeNumberOfQuestsKey(), PersistentDataType.INTEGER, numberOfQuests);

        tome.setItemMeta(tomeMeta);

        for (int i = 0; i < numberOfQuests; i++){
            IQuest questType = chooseRandomQuest();

            tomeMeta = tome.getItemMeta();

            tomeMeta.getPersistentDataContainer().set(getQuestTypeKey(i), PersistentDataType.STRING, questType.getQuestName());

            tomeMeta.getPersistentDataContainer().set(Tome.getTomeTypeKey(), PersistentDataType.STRING, this.tomeVariableName);

            tome.setItemMeta(tomeMeta);

            Random random = new Random();
            //Applying unique quest data
            if (questType instanceof Mine){
                Material material = this.mineTomeCustomization.getMaterials().get(random.nextInt(this.mineTomeCustomization.getMaterials().size()));
                int indexOfEntity = this.mineTomeCustomization.getMaterials().indexOf(material);
                int targetNumber = RandomNumberBetweenBounds.getRandomInt(this.mineTomeCustomization.getMinNums().get(indexOfEntity), this.mineTomeCustomization.getMaxNums().get(indexOfEntity));

                Mine mine = new Mine(i, material, 0, targetNumber);
                tome = mine.applyQuest(tome);

            } else if (questType instanceof Slayer){
                EntityType entityType = this.slayerTomeCustomization.getEntities().get(random.nextInt(this.slayerTomeCustomization.getEntities().size()));
                int indexOfEntity = this.slayerTomeCustomization.getEntities().indexOf(entityType);
                int targetNumber = RandomNumberBetweenBounds.getRandomInt(this.slayerTomeCustomization.getMinNums().get(indexOfEntity), this.slayerTomeCustomization.getMaxNums().get(indexOfEntity));

                Slayer slayer = new Slayer(i, entityType, 0, targetNumber);
                tome = slayer.applyQuest(tome);
            } else if (questType instanceof Fish){
                int targetNumber = RandomNumberBetweenBounds.getRandomInt(this.fishTomeCustomization.getMinNums().get(0), this.fishTomeCustomization.getMaxNums().get(0));

                Fish fish = new Fish(i, 0, targetNumber);
                tome = fish.applyQuest(tome);
            }
        }

        tomeMeta = tome.getItemMeta();

        tomeMeta.setDisplayName(this.tomeDisplayName);


        tomeMeta.getPersistentDataContainer().set(getTomeKey(), PersistentDataType.INTEGER, 1);
        tomeMeta.getPersistentDataContainer().set(getTomeCompleteKey(), PersistentDataType.INTEGER, 0);

        tome.setItemMeta(tomeMeta);

        return tome;
    }
    
    public IQuest chooseRandomQuest(){
        Random random = new Random();
        return this.availableQuests.get(random.nextInt(availableQuests.size()));
    }

    public static NamespacedKey getQuestTypeKey(int index){
        return new NamespacedKey(Tomes.getInstance(), "QuestType"+index);
    }

    public static void checkIfTomeIsComplete(Player player, ItemStack tome){
        ItemMeta tomeMeta = tome.getItemMeta();

        int numberOfQuests = tomeMeta.getPersistentDataContainer().get(tomeNumberOfQuestsKey, PersistentDataType.INTEGER);

        int numberOfCompleteQuests = 0;
        for (int i = 0; i < numberOfQuests; i++){
            NamespacedKey questTypeKey = new NamespacedKey(Tomes.getInstance(), "QuestType"+i);
            String questTypeString = tomeMeta.getPersistentDataContainer().get(questTypeKey, PersistentDataType.STRING).toLowerCase();

            switch (questTypeString){
                case "slayer":
                    NamespacedKey questCurrentKey = new NamespacedKey(Tomes.getInstance(), "SlayerCurrent"+i);
                    NamespacedKey questTargetKey = new NamespacedKey(Tomes.getInstance(), "SlayerTarget"+i);
                    NamespacedKey questTargetEntity = new NamespacedKey(Tomes.getInstance(), "SlayerEntityType"+i);

                    Slayer slayer = new Slayer(i, null, 0, 0);

                    if (tomeMeta.getPersistentDataContainer().get(slayer.getSlayerCompletedKey(), PersistentDataType.INTEGER) != null && tomeMeta.getPersistentDataContainer().get(slayer.getSlayerCompletedKey(), PersistentDataType.INTEGER) == 1){
                        numberOfCompleteQuests += 1;
                    }
                case "mine":
                    questCurrentKey = new NamespacedKey(Tomes.getInstance(), "MineCurrent"+i);
                    questTargetKey = new NamespacedKey(Tomes.getInstance(), "MineTarget"+i);
                    questTargetEntity = new NamespacedKey(Tomes.getInstance(), "MineMaterial"+i);

                    Mine mine = new Mine(i, null, 0, 0);

                    if (tomeMeta.getPersistentDataContainer().get(mine.getMineCompletedKey(), PersistentDataType.INTEGER) != null && tomeMeta.getPersistentDataContainer().get(mine.getMineCompletedKey(), PersistentDataType.INTEGER) == 1){
                        numberOfCompleteQuests += 1;
                    }
                case "fish":
                    questCurrentKey = new NamespacedKey(Tomes.getInstance(), "FishCurrent"+i);
                    questTargetKey = new NamespacedKey(Tomes.getInstance(), "FishTarget"+i);

                    Fish fish = new Fish(i, 0, 0);

                    if (tomeMeta.getPersistentDataContainer().get(fish.getFishCompletedKey(), PersistentDataType.INTEGER) != null && tomeMeta.getPersistentDataContainer().get(fish.getFishCompletedKey(), PersistentDataType.INTEGER) == 1){
                        numberOfCompleteQuests += 1;
                    }

            }
        }

        if (numberOfCompleteQuests == numberOfQuests){
            tomeMeta.getPersistentDataContainer().set(tomeCompleteKey, PersistentDataType.INTEGER, 1);

            List<String> completedTombLore = new ArrayList<>();
            completedTombLore.add("COMPLETE!");
            tomeMeta.setLore(completedTombLore);

            tome.setItemMeta(tomeMeta);

            player.sendMessage(ChatColor.LIGHT_PURPLE + "[Tomes] Tome Complete!");
        }
    }


    public String getTomeVariableName() {
        return tomeVariableName;
    }

    public String getTomeDisplayName() {
        return tomeDisplayName;
    }

    public int getNumberOfQuests() {
        return numberOfQuests;
    }

    public int getCost() {
        return cost;
    }

    public List<IQuest> getAvailableQuests() {
        return availableQuests;
    }

    public List<ItemStack> getRewards() {
        return rewards;
    }

    public SlayerTomeCustomization getSlayerTomeCustomization() {
        return slayerTomeCustomization;
    }

    public MineTomeCustomization getMineTomeCustomization() {
        return mineTomeCustomization;
    }

    public FishTomeCustomization getFishTomeCustomization() {
        return fishTomeCustomization;
    }

    public static NamespacedKey getTomeKey() {
        return tomeKey;
    }

    public static NamespacedKey getTomeTypeKey() {
        return tomeTypeKey;
    }

    public static NamespacedKey getTomeNumberOfQuestsKey() {
        return tomeNumberOfQuestsKey;
    }

    public static NamespacedKey getTomeCompleteKey() {
        return tomeCompleteKey;
    }
}
