package com.swizzle.tomes.QuestTypes;

import com.swizzle.tomes.TomeObject;

public class Mine implements IQuest {
    private static String questName = "Mine";

    @Override
    public String getQuestName() {
        return questName;
    }
}
