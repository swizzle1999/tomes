package com.swizzle.tomes.TomeTypes;

public class StoneTome extends Tome{
    private final String tomeVariableName = "stone";
    private final String tomeDisplayName = "Stone Tome";
    private final int numberOfQuests = 3;

    public StoneTome(){

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
