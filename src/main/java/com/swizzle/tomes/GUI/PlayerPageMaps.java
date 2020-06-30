package com.swizzle.tomes.GUI;

import java.util.HashMap;
import java.util.UUID;

public class PlayerPageMaps {
    //A map to keep track of players navigating the rewards pages
    private static HashMap<UUID, PlayerPagesContainer> playerRewardPageMap = new HashMap<>();

    //A map to keep track of players navigating the tomes pages
    private static HashMap<UUID, PlayerPagesContainer> playerTomesPageMap = new HashMap<>();

    public static HashMap<UUID, PlayerPagesContainer> getPlayerRewardPageMap() {
        return playerRewardPageMap;
    }

    public static void updatePlayerRewardPageMap(UUID playerID, PlayerPagesContainer playerPagesContainer) {
        PlayerPageMaps.playerRewardPageMap.put(playerID, playerPagesContainer);
    }

    public static HashMap<UUID, PlayerPagesContainer> getPlayerTomesPageMap() {
        return playerTomesPageMap;
    }

    public static void updatePlayerTomesPageMap(UUID playerID, PlayerPagesContainer playerPagesContainer) {
        PlayerPageMaps.playerTomesPageMap.put(playerID, playerPagesContainer);
    }
}
