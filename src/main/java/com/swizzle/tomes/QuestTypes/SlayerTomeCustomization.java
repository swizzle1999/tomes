package com.swizzle.tomes.QuestTypes;

import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.HashMap;

public class SlayerTomeCustomization {

    private ArrayList<EntityType> entities;
    private ArrayList<Integer> minNums;
    private ArrayList<Integer> maxNums;

    public SlayerTomeCustomization(ArrayList<EntityType> entities, ArrayList<Integer> minNums, ArrayList<Integer> maxNums){
        this.entities = entities;
        this.minNums = minNums;
        this.maxNums = maxNums;
    }

    public ArrayList<EntityType> getEntities() {
        return entities;
    }

    public ArrayList<Integer> getMinNums() {
        return minNums;
    }

    public ArrayList<Integer> getMaxNums() {
        return maxNums;
    }
}
