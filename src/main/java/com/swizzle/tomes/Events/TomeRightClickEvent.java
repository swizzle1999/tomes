package com.swizzle.tomes.Events;

import com.swizzle.tomes.TomeClasses.Tome;
import com.swizzle.tomes.Tomes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
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
                            ItemStack reward = rewardsArray.get(random.nextInt(rewardsArray.size()));
                            e.getPlayer().getInventory().addItem(reward);
                            e.getPlayer().getInventory().removeItem(currentItem);

                            for(Player player : Bukkit.getOnlinePlayers()){
                                String finalItemNameToDisplay = "";
                                if (reward.getItemMeta().hasDisplayName()){
                                    finalItemNameToDisplay = reward.getItemMeta().getDisplayName();
                                } else {
                                    String[] itemNameToDisplaySplit = reward.getType().name().split("_");

                                    for (String string : itemNameToDisplaySplit){
                                        string = string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
                                        finalItemNameToDisplay += (string + " ");
                                    }
                                }
                                player.sendMessage(ChatColor.LIGHT_PURPLE + "[Tomes] " + e.getPlayer().getName() + " Received: x" + reward.getAmount() + " " + finalItemNameToDisplay);
                            }
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
