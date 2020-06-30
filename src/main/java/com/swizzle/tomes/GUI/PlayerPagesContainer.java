package com.swizzle.tomes.GUI;

import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PlayerPagesContainer {
    private UUID playerID;
    private int pageNumber;
    ArrayList<Inventory> pages;

    public PlayerPagesContainer(UUID playerID, int pageNumber, ArrayList<Inventory> pages){
        this.playerID = playerID;
        this.pageNumber = pageNumber;
        this.pages = pages;
    }

    public Inventory getCurrentInventory(){
        return pages.get(pageNumber);
    }

    public void incrementPage(){
        if (pageNumber < pages.size()-1) {
            this.pageNumber += 1;
        }
    }

    public void decrementPage(){
        if (pageNumber > 0) {
            this.pageNumber -= 1;
        }
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public void setPlayerID(UUID playerID) {
        this.playerID = playerID;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public ArrayList<Inventory> getPages() {
        return pages;
    }

    public void setPages(ArrayList<Inventory> pages) {
        this.pages = pages;
    }
}
