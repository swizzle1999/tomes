package com.swizzle.tomes.QuestTypes;

import org.bukkit.entity.EntityType;

import java.util.ArrayList;

public class FishTomeCustomization {

    private ArrayList<Integer> minNums;
    private ArrayList<Integer> maxNums;

    public FishTomeCustomization(ArrayList<Integer> minNums, ArrayList<Integer> maxNums){
        this.minNums = minNums;
        this.maxNums = maxNums;
    }

    public ArrayList<Integer> getMinNums() {
        return minNums;
    }

    public ArrayList<Integer> getMaxNums() {
        return maxNums;
    }
}
