package com.swizzle.tomes.TomeTypes;

import com.swizzle.tomes.QuestTypes.*;
import com.swizzle.tomes.Tomes;
import com.swizzle.tomes.Utils.RandomNumberBetweenBounds;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WoodTome extends Tome{
    private final String tomeVariableName = "wood";
    private final String tomeDisplayName = "Wood Tome";
    private final int numberOfQuests = 2;

    private List<IQuest> availableQuests = new ArrayList<IQuest>();

    private SlayerTomeCustomization slayerTomeCustomization;
    private MineTomeCustomization mineTomeCustomization;
    private FishTomeCustomization fishTomeCustomization;

    public WoodTome(){
        //Slayer Section
        ArrayList<EntityType> entities = new ArrayList<EntityType>();
        ArrayList<Integer> minNums = new ArrayList<Integer>();
        ArrayList<Integer> maxNums = new ArrayList<Integer>();

        ConfigurationSection slayerEntitiesConfig = Tomes.getInstance().getConfig().getConfigurationSection("tomes." + this.tomeVariableName + ".slayer.entities");
        for (String key : slayerEntitiesConfig.getKeys(false)){
            EntityType entityType = EntityType.valueOf(Tomes.getInstance().getConfig().getString("tomes." + this.tomeVariableName + ".slayer.entities."+key));
            int minNum = Tomes.getInstance().getConfig().getInt("tomes." + this.tomeVariableName + ".slayer.minNumbers."+key);
            int maxNum = Tomes.getInstance().getConfig().getInt("tomes." + this.tomeVariableName + ".slayer.maxNumbers."+key);

            if (entityType == null || minNum == 0 || maxNum == 0){
                System.out.println("ERROR CONFIGURATION IS WRONG SOMEWHERE");
            }
            entities.add(entityType);
            minNums.add(minNum);
            maxNums.add(maxNum);
        }

        this.slayerTomeCustomization = new SlayerTomeCustomization(entities, minNums, maxNums);
        this.availableQuests.add(new Slayer(0, null, 0,0));

        //Mine Section
        ArrayList<Material> materials = new ArrayList<Material>();
        minNums = new ArrayList<Integer>();
        maxNums = new ArrayList<Integer>();

        ConfigurationSection mineMaterialsConfig = Tomes.getInstance().getConfig().getConfigurationSection("tomes." + this.tomeVariableName + ".mine.materials");
        for (String key : mineMaterialsConfig.getKeys(false)){
            Material material = Material.valueOf(Tomes.getInstance().getConfig().getString("tomes." + this.tomeVariableName + ".mine.materials."+key));
            int minNum = Tomes.getInstance().getConfig().getInt("tomes." + this.tomeVariableName + ".mine.minNumbers."+key);
            int maxNum = Tomes.getInstance().getConfig().getInt("tomes." + this.tomeVariableName + ".mine.maxNumbers."+key);

            if (material == null || minNum == 0 || maxNum == 0){
                System.out.println("ERROR CONFIGURATION IS WRONG SOMEWHERE");
            }
            materials.add(material);
            minNums.add(minNum);
            maxNums.add(maxNum);
        }

        this.mineTomeCustomization = new MineTomeCustomization(materials, minNums, maxNums);
        this.availableQuests.add(new Mine(0, null, 0,0));

        //Fish Section
        minNums = new ArrayList<Integer>();
        maxNums = new ArrayList<Integer>();

        ConfigurationSection fishConfig = Tomes.getInstance().getConfig().getConfigurationSection("tomes." + this.tomeVariableName + ".fish.minNumbers");
        for (String key : fishConfig.getKeys(false)){
            int minNum = Tomes.getInstance().getConfig().getInt("tomes." + this.tomeVariableName + ".fish.minNumbers."+key);
            int maxNum = Tomes.getInstance().getConfig().getInt("tomes." + this.tomeVariableName + ".fish.maxNumbers."+key);

            if (minNum == 0 || maxNum == 0){
                System.out.println("ERROR CONFIGURATION IS WRONG SOMEWHERE");
            }
            minNums.add(minNum);
            maxNums.add(maxNum);
        }

        this.fishTomeCustomization = new FishTomeCustomization(minNums, maxNums);
        this.availableQuests.add(new Fish(0, 0, 0));
    }


    @Override
    public String getTomeVariableName() {
        return tomeVariableName;
    }

    @Override
    public String getTomeDisplayName() {
        return tomeDisplayName;
    }

    @Override
    public int getNumberOfQuests() {
        return numberOfQuests;
    }

    @Override
    public ItemStack giveBook() {
        ItemStack tome = new ItemStack(Material.BOOK);
        ItemMeta tomeMeta = tome.getItemMeta();

        tomeMeta.getPersistentDataContainer().set(Tome.getTomeNumberOfQuestsKey(), PersistentDataType.INTEGER, numberOfQuests);

        tome.setItemMeta(tomeMeta);

        for (int i = 0; i < numberOfQuests; i++){
            IQuest questType = chooseRandomQuest();

            tomeMeta = tome.getItemMeta();

            tomeMeta.getPersistentDataContainer().set(Tome.getQuestTypeKey(i), PersistentDataType.STRING, questType.getQuestName());

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


        tomeMeta.getPersistentDataContainer().set(Tome.getTomeKey(), PersistentDataType.INTEGER, 1);
        tomeMeta.getPersistentDataContainer().set(Tome.getTomeCompleteKey(), PersistentDataType.INTEGER, 0);

        tome.setItemMeta(tomeMeta);

        return tome;
    }

    @Override
    public List<IQuest> availableQuests() {
        return this.availableQuests;
    }

    @Override
    public IQuest chooseRandomQuest(){
        Random random = new Random();
        return this.availableQuests.get(random.nextInt(availableQuests.size()));
    }
}
