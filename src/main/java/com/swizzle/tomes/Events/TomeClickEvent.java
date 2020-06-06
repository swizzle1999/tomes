package com.swizzle.tomes.Events;

import com.swizzle.tomes.TomeObject;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class TomeClickEvent implements Listener {
    @EventHandler
    public void onTomeGUIClick(InventoryClickEvent e){

        if (e.getView().getTitle().equalsIgnoreCase("tomes")){
            if (e.getCurrentItem() != null && e.getCurrentItem().getItemMeta().getLore().get(0).equals("Random Quests For Rewards!")){
                //Check which tome was clicked
                switch(e.getCurrentItem().getItemMeta().getDisplayName()){
                    case "Dirt Tome":
                        e.getWhoClicked().getInventory().addItem(TomeObject.giveBook("Dirt Tome", 1));
                        break;
                    case "Wood Tome":
                        e.getWhoClicked().getInventory().addItem(TomeObject.giveBook("Wood Tome", 2));
                        break;
                    case "Stone Tome":
                        e.getWhoClicked().getInventory().addItem(TomeObject.giveBook("Stone Tome", 3));
                        break;
                }

            }

            e.setCancelled(true);
        }


    }
}
