package com.swizzle.tomes.TomeTypes;

import com.swizzle.tomes.QuestTypes.IQuest;
import com.swizzle.tomes.QuestTypes.SlayerTomeCustomization;
import com.swizzle.tomes.Tomes;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class Tome {
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
}
