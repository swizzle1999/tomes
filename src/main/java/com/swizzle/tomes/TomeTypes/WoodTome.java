package com.swizzle.tomes.TomeTypes;

public class WoodTome extends Tome{
    private final String tomeVariableName = "wood";
    private final String tomeDisplayName = "Wood Tome";
    private final int numberOfQuests = 2;

    public WoodTome(){

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
