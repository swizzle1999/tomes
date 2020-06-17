package com.swizzle.tomes.Events;

import com.swizzle.tomes.TomeClasses.Tome;
import com.swizzle.tomes.Tomes;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class TomeClickEvent implements Listener {
    @EventHandler
    public void onTomeGUIClick(InventoryClickEvent e){

        if (e.getView().getTitle().equalsIgnoreCase("tomes")){
            if (e.getCurrentItem() != null && e.getCurrentItem().getItemMeta().getLore().get(0).equals("Random Quests For Rewards!")){
                //Check which tome was clicked
                for (Tome tome : Tomes.getTomes()){
                    if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(tome.getTomeDisplayName())){
                        if (((Player)e.getWhoClicked()).getLevel() >= Tomes.getInstance().getConfig().getInt("tomes." + tome.getTomeVariableName() + ".cost")){
                            ((Player)e.getWhoClicked()).setLevel(((Player)e.getWhoClicked()).getLevel() - Tomes.getInstance().getConfig().getInt("tomes." + tome.getTomeVariableName() + ".cost"));
                            e.getWhoClicked().getInventory().addItem(tome.giveBook());
                        } else {
                            e.getWhoClicked().sendMessage("Sorry, You do not have the required levels to purchase this tome");
                        }

                    }
                }
            }

            e.setCancelled(true);
        }


    }
}
