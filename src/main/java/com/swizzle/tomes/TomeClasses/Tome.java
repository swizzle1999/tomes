package com.swizzle.tomes.TomeClasses;

import com.swizzle.tomes.QuestTypes.FishTomeCustomization;
import com.swizzle.tomes.QuestTypes.IQuest;
import com.swizzle.tomes.QuestTypes.MineTomeCustomization;
import com.swizzle.tomes.QuestTypes.SlayerTomeCustomization;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tome{
    private String tomeVariableName;
    private String tomeDisplayName;
    private int numberOfQuests;
    private int cost;

    private List<IQuest> availableQuests = new ArrayList<IQuest>();

    private SlayerTomeCustomization slayerTomeCustomization;
    private MineTomeCustomization mineTomeCustomization;
    private FishTomeCustomization fishTomeCustomization;

    private Random random = new Random();

    public Tome(String tomeVariableName, String tomeDisplayName, int numberOfQuests, int cost, List<IQuest> availableQuests){
        this.tomeVariableName = tomeVariableName;
        this.tomeDisplayName = tomeDisplayName;
        this.numberOfQuests = numberOfQuests;
        this.cost = cost;
        this.availableQuests = availableQuests;
    }

    public String getTomeVariableName() {
        return tomeVariableName;
    }

    public String getTomeDisplayName() {
        return tomeDisplayName;
    }

    public int getNumberOfQuests() {
        return numberOfQuests;
    }

    public int getCost() {
        return cost;
    }

    public List<IQuest> getAvailableQuests() {
        return availableQuests;
    }

    public SlayerTomeCustomization getSlayerTomeCustomization() {
        return slayerTomeCustomization;
    }

    public MineTomeCustomization getMineTomeCustomization() {
        return mineTomeCustomization;
    }

    public FishTomeCustomization getFishTomeCustomization() {
        return fishTomeCustomization;
    }
}
