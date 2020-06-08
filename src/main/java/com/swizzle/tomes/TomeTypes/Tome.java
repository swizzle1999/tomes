package com.swizzle.tomes.TomeTypes;

import com.swizzle.tomes.TomeObject;
import com.swizzle.tomes.Tomes;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public abstract class Tome {
    public abstract String getTomeVariableName();
    public abstract String getTomeDisplayName();
    public abstract int getNumberOfQuests();
    public ArrayList<ItemStack> getRewardsArray(String tomeType){

        ConfigurationSection itemsSection = Tomes.getInstance().getConfig().getConfigurationSection("loottable." + tomeType + ".items");
        ConfigurationSection weightsSection = Tomes.getInstance().getConfig().getConfigurationSection("loottable." + tomeType + ".weights");

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
}
