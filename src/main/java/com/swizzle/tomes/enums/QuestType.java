package com.swizzle.tomes.enums;

import com.swizzle.tomes.TomeObject;

import java.util.Random;

public enum QuestType {

    SLAYER,
    MINE;

    public static QuestType getRandomEnum(){
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }

}
