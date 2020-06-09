package com.swizzle.tomes.TomeTypes;

import com.swizzle.tomes.QuestTypes.IQuest;
import com.swizzle.tomes.QuestTypes.Mine;
import com.swizzle.tomes.QuestTypes.Slayer;
import com.swizzle.tomes.QuestTypes.SlayerTomeCustomization;
import com.swizzle.tomes.TomeObject;
import com.swizzle.tomes.Tomes;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class DirtTome extends Tome{
    private final String tomeVariableName = "dirt";
    private final String tomeDisplayName = "Dirt Tome";
    private final int numberOfQuests = 1;

    private List<IQuest> availableQuests = new ArrayList<IQuest>();

    private SlayerTomeCustomization slayerTomeCustomization;

    public DirtTome(){
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

        tomeMeta.getPersistentDataContainer().set(TomeObject.numberOfQuestsKey, PersistentDataType.INTEGER, numberOfQuests);

        tome.setItemMeta(tomeMeta);

        for (int i = 0; i < numberOfQuests; i++){
            IQuest questType = chooseRandomQuest();

            tomeMeta = tome.getItemMeta();
            NamespacedKey questTypeKey = new NamespacedKey(Tomes.getInstance(), "QuestType"+i);
            tomeMeta.getPersistentDataContainer().set(questTypeKey, PersistentDataType.STRING, questType.getQuestName());

            tomeMeta.getPersistentDataContainer().set(TomeObject.tomeTypeKey, PersistentDataType.STRING, this.tomeVariableName);

            tome.setItemMeta(tomeMeta);

            Random random = new Random();
            //Applying unique quest data
            if (questType instanceof Mine){

            } else if (questType instanceof Slayer){
                EntityType entityType = this.slayerTomeCustomization.getEntities().get(random.nextInt(this.slayerTomeCustomization.getEntities().size()));
                int indexOfEntity = this.slayerTomeCustomization.getEntities().indexOf(entityType);
                int targetNumber = random.nextInt(this.slayerTomeCustomization.getMaxNums().get(indexOfEntity) - this.slayerTomeCustomization.getMinNums().get(indexOfEntity)) + this.slayerTomeCustomization.getMinNums().get(indexOfEntity);

                Slayer slayer = new Slayer(i, entityType, 0, targetNumber);
                tome = slayer.applyQuest(tome);
            }
        }

        tomeMeta = tome.getItemMeta();

        tomeMeta.setDisplayName(this.tomeDisplayName);


        tomeMeta.getPersistentDataContainer().set(TomeObject.tomeKey, PersistentDataType.INTEGER, 1);
        tomeMeta.getPersistentDataContainer().set(TomeObject.tomeCompleteKey, PersistentDataType.INTEGER, 0);

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
