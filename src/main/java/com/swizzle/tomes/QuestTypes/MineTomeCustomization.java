package com.swizzle.tomes.QuestTypes;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;

public class MineTomeCustomization {

    private ArrayList<Material> materials;
    private ArrayList<Integer> minNums;
    private ArrayList<Integer> maxNums;

    public MineTomeCustomization(ArrayList<Material> materials, ArrayList<Integer> minNums, ArrayList<Integer> maxNums){
        this.materials = materials;
        this.minNums = minNums;
        this.maxNums = maxNums;
    }

    public ArrayList<Material> getMaterials() {
        return materials;
    }

    public ArrayList<Integer> getMinNums() {
        return minNums;
    }

    public ArrayList<Integer> getMaxNums() {
        return maxNums;
    }
}
