package com.swizzle.tomes.Events;

import com.swizzle.tomes.TomeObject;
import com.swizzle.tomes.TomeTypes.DirtTome;
import com.swizzle.tomes.TomeTypes.Tome;
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

                for (Tome tome : TomeObject.tomes){
                    if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(tome.getTomeDisplayName())){
                        e.getWhoClicked().getInventory().addItem(TomeObject.giveBook(tome.getTomeDisplayName(), tome.getNumberOfQuests(), tome.getTomeVariableName()));
                    }
                }
//                switch(e.getCurrentItem().getItemMeta().getDisplayName()){
//                    case "Dirt Tome":
//                        e.getWhoClicked().getInventory().addItem(TomeObject.giveBook("Dirt Tome", 1, "dirt"));
//                        break;
//                    case "Wood Tome":
//                        e.getWhoClicked().getInventory().addItem(TomeObject.giveBook("Wood Tome", 2, "wood"));
//                        break;
//                    case "Stone Tome":
//                        e.getWhoClicked().getInventory().addItem(TomeObject.giveBook("Stone Tome", 3, "stone"));
//                        break;
//                }

            }

            e.setCancelled(true);
        }


    }
}
