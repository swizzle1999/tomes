package com.swizzle.tomes.Events;

import com.swizzle.tomes.TomeTypes.Tome;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class TomeClickEvent implements Listener {
    @EventHandler
    public void onTomeGUIClick(InventoryClickEvent e){

        if (e.getView().getTitle().equalsIgnoreCase("tomes")){
            if (e.getCurrentItem() != null && e.getCurrentItem().getItemMeta().getLore().get(0).equals("Random Quests For Rewards!")){
                //Check which tome was clicked
                for (Tome tome : Tome.getTomes()){
                    if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(tome.getTomeDisplayName())){
                        e.getWhoClicked().getInventory().addItem(tome.giveBook());
                    }
                }
            }

            e.setCancelled(true);
        }


    }
}
