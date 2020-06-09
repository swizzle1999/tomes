package com.swizzle.tomes.Events;

import com.swizzle.tomes.TomeObject;
import com.swizzle.tomes.TomeTypes.Tome;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Random;

public class TomeRightClickEvent implements Listener {
    @EventHandler
    public void onRightClickTome(PlayerInteractEvent e){
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) && e.getItem().getItemMeta().getPersistentDataContainer().has(TomeObject.tomeKey, PersistentDataType.INTEGER)){
            ItemStack currentItem = e.getItem();
            if (currentItem.getItemMeta().getPersistentDataContainer().get(TomeObject.tomeCompleteKey, PersistentDataType.INTEGER) == 1){
                for (Tome tome : TomeObject.tomes){
                    if (currentItem.getItemMeta().getPersistentDataContainer().get(TomeObject.tomeTypeKey, PersistentDataType.STRING).equalsIgnoreCase(tome.getTomeVariableName())){
                        //System.out.println("its a " + tome.getTomeDisplayName());
                        ArrayList<ItemStack> rewardsArray = tome.getRewardsArray(tome.getTomeVariableName());

                        Random random = new Random();
                        e.getPlayer().getInventory().addItem(rewardsArray.get(random.nextInt(rewardsArray.size())));
                        break;
                    }
                }
            }
        }
    }
}
