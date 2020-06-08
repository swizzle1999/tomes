package com.swizzle.tomes.TomeTypes;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class DirtTome extends Tome{
    private final String tomeVariableName = "dirt";
    private final String tomeDisplayName = "Dirt Tome";
    private final int numberOfQuests = 1;

    public DirtTome(){

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

}
