package com.swizzle.tomes.Events;

import com.swizzle.tomes.TomeObject;
import com.swizzle.tomes.TomeTypes.GetReward;
import com.swizzle.tomes.TomeTypes.WoodRewards;
import com.swizzle.tomes.Tomes;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TomeRightClickEvent implements Listener {
    @EventHandler
    public void onRightClickTome(PlayerInteractEvent e){
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) && e.getItem().getItemMeta().getPersistentDataContainer().has(TomeObject.tomeKey, PersistentDataType.INTEGER)){
            ItemStack tome = e.getItem();
            if (tome.getItemMeta().getPersistentDataContainer().get(TomeObject.tomeCompleteKey, PersistentDataType.INTEGER) == 1){
                System.out.println("TOME IS COMPLETE");

                ItemStack reward = GetReward.chooseReward(WoodRewards.rewards);
                e.getPlayer().getInventory().addItem(reward);

                //System.out.println(Tomes.getInstance().getConfig().getList("loottable.wood").toString());

//                List<?> tempLootTableList = Tomes.getInstance().getConfig().getList("loottable.wood");
//
//                for (int i = 0; i < tempLootTableList.size(); i++){
//                    ItemStack item = (ItemStack)tempLootTableList.get(i);
//
//                    System.out.println(item.getData());
//                }
//                if (lootTableList == null){
//                    lootTableList = new ArrayList<>();
//                }
//
////                NamespacedKey key = new NamespacedKey(Tomes.getInstance(), "test");
////                ItemStack customItem = new ItemStack(Material.BOOK);
////                ItemMeta customItemMeta = customItem.getItemMeta();
////                customItemMeta.getPersistentDataContainer().set(key , PersistentDataType.STRING, "hgello");
////                customItem.setItemMeta(customItemMeta);
//
//                ItemStack diamonds = new ItemStack(Material.DIAMOND, 64);
//                ItemStack dirt = new ItemStack(Material.DIRT, 64);
//
//
//                lootTableList.add(diamonds);
//                lootTableList.add(dirt);
//
//                Tomes.getInstance().getConfig().set("loottable.wood", lootTableList);
//                Tomes.getInstance().saveConfig();
//
//                lootTableList = (List<ItemStack>)Tomes.getInstance().getConfig().getList("loottable.wood");
//
////                for (int i = 0; i < lootTableList.size(); i ++){
////                    ItemStack test = lootTableList.get(i);
////                }
//
//                Random random = new Random();
//                ItemStack randItem = lootTableList.getItemStack(random.nextInt(lootTableList.size()));
//
//                System.out.println(randItem.getData());
            }
        }
    }
}
