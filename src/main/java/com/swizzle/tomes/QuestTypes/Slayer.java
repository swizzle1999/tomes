package com.swizzle.tomes.QuestTypes;

import com.swizzle.tomes.TomeObject;
import org.bukkit.entity.Entity;

public class Slayer implements IQuest {
    private String questName = "Slayer";

    private int currentMobCount;
    private int targetMobCount;

    private Entity targetMob;

    public void incrementCurrentMobCount(int incrementAmmount){
        currentMobCount += incrementAmmount;
    }

    @Override
    public String getQuestName() {
        return questName;
    }
}
