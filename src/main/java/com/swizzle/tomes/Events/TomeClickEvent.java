package com.swizzle.tomes.Events;

import com.swizzle.tomes.TomeClasses.AbstractTome;
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
                for (AbstractTome abstractTome : AbstractTome.getAbstractTomes()){
                    if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(abstractTome.getTomeDisplayName())){
                        if (((Player)e.getWhoClicked()).getLevel() >= Tomes.getInstance().getConfig().getInt("tomes." + abstractTome.getTomeVariableName() + ".cost")){
                            ((Player)e.getWhoClicked()).setLevel(((Player)e.getWhoClicked()).getLevel() - Tomes.getInstance().getConfig().getInt("tomes." + abstractTome.getTomeVariableName() + ".cost"));
                            e.getWhoClicked().getInventory().addItem(abstractTome.giveBook());
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
