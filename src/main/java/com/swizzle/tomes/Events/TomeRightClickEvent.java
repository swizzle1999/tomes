package com.swizzle.tomes.Events;

import com.swizzle.tomes.TomeClasses.Tome;
import com.swizzle.tomes.Tomes;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TomeRightClickEvent implements Listener {
    @EventHandler
    public void onRightClickTome(PlayerInteractEvent e){
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) && e.getItem().getItemMeta().getPersistentDataContainer().has(Tome.getTomeKey(), PersistentDataType.INTEGER)){
            e.setCancelled(true);
            ItemStack currentItem = e.getItem();
            if (currentItem.getItemMeta().getPersistentDataContainer().get(Tome.getTomeCompleteKey(), PersistentDataType.INTEGER) == 1){
                for (Tome tome : Tomes.getTomes()){
                    if (currentItem.getItemMeta().getPersistentDataContainer().get(Tome.getTomeTypeKey(), PersistentDataType.STRING).equalsIgnoreCase(tome.getTomeVariableName())){
                        List<ItemStack> rewardsArray = tome.getRewards();

                        Random random = new Random();

                        if (rewardsArray.size() > 0) {
                            e.getPlayer().getInventory().addItem(rewardsArray.get(random.nextInt(rewardsArray.size())));
                            e.getPlayer().getInventory().removeItem(currentItem);
                        } else {
                            e.getPlayer().sendMessage(ChatColor.RED + "Sorry, the server owner forgot to add rewards to your tome. Complain to him!");
                        }
                        break;
                    }
                }
            }
        }
    }
}
