package com.swizzle.tomes.TomeTypes;

import com.swizzle.tomes.QuestTypes.IQuest;
import com.swizzle.tomes.QuestTypes.Mine;
import com.swizzle.tomes.QuestTypes.Slayer;
import com.swizzle.tomes.Tomes;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Tome {

    //An array of all possible tomes
    private static final ArrayList<Tome> tomes = new ArrayList<Tome>(Arrays.asList(new DirtTome(), new WoodTome(), new StoneTome()));

    //Generic Tome Keys
    private static final NamespacedKey tomeKey = new NamespacedKey(Tomes.getInstance(), "Tome");
    private static final NamespacedKey tomeTypeKey = new NamespacedKey(Tomes.getInstance(), "TomeType");
    private static final NamespacedKey tomeNumberOfQuestsKey = new NamespacedKey(Tomes.getInstance(), "TomeNumberOfQuests");
    private static final NamespacedKey tomeCompleteKey = new NamespacedKey(Tomes.getInstance(), "TomeComplete");

    public abstract String getTomeVariableName();
    public abstract String getTomeDisplayName();
    public abstract int getNumberOfQuests();

    public ArrayList<ItemStack> getRewardsArray(String tomeType){

        ConfigurationSection itemsSection = Tomes.getInstance().getConfig().getConfigurationSection("tomes." + tomeType + ".rewards.items");
        ConfigurationSection weightsSection = Tomes.getInstance().getConfig().getConfigurationSection("tomes." + tomeType + ".rewards.weights");

        ArrayList<ItemStack> rewardsArray = new ArrayList<ItemStack>();
        for (String key : itemsSection.getKeys(false)){
            ItemStack item = itemsSection.getItemStack(key);

            for (int i = 0; i < Integer.parseInt(weightsSection.getString(key)); i++){
                rewardsArray.add(item);
            }

        }
        System.out.println(rewardsArray);

        return rewardsArray;
    };

    public abstract ItemStack giveBook();

    public abstract List<IQuest> availableQuests();

    public abstract IQuest chooseRandomQuest();

    public static void checkIfTomeIsComplete(ItemStack tome){
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

            }
        }

        System.out.println(numberOfCompleteQuests);
        System.out.println(numberOfQuests);
        if (numberOfCompleteQuests == numberOfQuests){
            tomeMeta.getPersistentDataContainer().set(tomeCompleteKey, PersistentDataType.INTEGER, 1);

            List<String> completedTombLore = new ArrayList<>();
            completedTombLore.add("COMPLETE!");
            tomeMeta.setLore(completedTombLore);

            tome.setItemMeta(tomeMeta);
        }
    }

    public static NamespacedKey getQuestTypeKey(int index){
        return new NamespacedKey(Tomes.getInstance(), "QuestType"+index);
    }

    public static ArrayList<Tome> getTomes() {
        return tomes;
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
