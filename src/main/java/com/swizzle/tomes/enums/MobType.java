package com.swizzle.tomes.enums;

import java.util.Random;

public enum MobType {
    ZOMBIE,
    SKELETON,
    CREEPER;

    public static MobType getRandomEnum(){
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
}
