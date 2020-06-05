package com.swizzle.tomes.QuestTypes;

public interface IQuest<T> {
    public String getQuestName();

    public void parseIntoObject(T object);
}
