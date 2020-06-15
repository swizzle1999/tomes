package com.swizzle.tomes.Events;

import com.swizzle.tomes.TomeClasses.AbstractTome;
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
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) && e.getItem().getItemMeta().getPersistentDataContainer().has(AbstractTome.getTomeKey(), PersistentDataType.INTEGER)){
            ItemStack currentItem = e.getItem();
            if (currentItem.getItemMeta().getPersistentDataContainer().get(AbstractTome.getTomeCompleteKey(), PersistentDataType.INTEGER) == 1){
                for (AbstractTome abstractTome : AbstractTome.getAbstractTomes()){
                    if (currentItem.getItemMeta().getPersistentDataContainer().get(AbstractTome.getTomeTypeKey(), PersistentDataType.STRING).equalsIgnoreCase(abstractTome.getTomeVariableName())){
                        //System.out.println("its a " + tome.getTomeDisplayName());
                        ArrayList<ItemStack> rewardsArray = abstractTome.getRewardsArray(abstractTome.getTomeVariableName());

                        Random random = new Random();
                        e.getPlayer().getInventory().addItem(rewardsArray.get(random.nextInt(rewardsArray.size())));
                        break;
                    }
                }
            }
        }
    }
}
