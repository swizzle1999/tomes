package com.swizzle.tomes.Events;

import com.swizzle.tomes.QuestTypes.IQuest;
import com.swizzle.tomes.QuestTypes.Slayer;
import com.swizzle.tomes.Tomes;
import com.swizzle.tomes.enums.QuestType;
import com.swizzle.tomes.enums.TomeTypes;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TomeQuestProgressEvent implements Listener {

    //Slayer Progress
    @EventHandler
    public void onPlayerKillEntity(EntityDamageByEntityEvent e){
        if (e.getDamager() instanceof Player){
            if (((Damageable)e.getEntity()).getHealth() - e.getFinalDamage() <= 0){
                Player player = (Player)e.getDamager();
                Inventory inventory = player.getInventory();

                ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                for (ItemStack item : inventory.getContents()){
                    if (item != null) {
                        items.add(item);
                        //Debugging Stuff
//                        if (item.getItemMeta().getLore() != null) {
//                            System.out.println("Name: " + item.getItemMeta().getDisplayName() + " | Lore:" + item.getItemMeta().getLore().get(0));
//                            NamespacedKey questTypeKey = new NamespacedKey(Tomes.getInstance(), "QuestType");
//                            //System.out.println(item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING));
//                            System.out.println(item.getItemMeta().getPersistentDataContainer().get(questTypeKey, PersistentDataType.STRING).equalsIgnoreCase(QuestType.SLAYER.toString()));
//                        }
                    } else {
                        continue;
                    }
                }

                NamespacedKey tomeKey = new NamespacedKey(Tomes.getInstance(), "Tome");
                NamespacedKey questTypeKey = new NamespacedKey(Tomes.getInstance(), "QuestType");

                Slayer throwawayInstance = new Slayer();
                List<ItemStack> tomes = items.stream().filter(itemStack -> itemStack.getItemMeta().getPersistentDataContainer().has(tomeKey, PersistentDataType.INTEGER)).filter(itemStack -> itemStack.getItemMeta().getPersistentDataContainer().get(questTypeKey, PersistentDataType.STRING).equalsIgnoreCase(throwawayInstance.getQuestName())).collect(Collectors.toList());

                NamespacedKey questCurrentKey = new NamespacedKey(Tomes.getInstance(), "SlayerCurrent");
                NamespacedKey questTargetKey = new NamespacedKey(Tomes.getInstance(), "SlayerTarget");
                NamespacedKey questTargetEntity = new NamespacedKey(Tomes.getInstance(), "SlayerEntityType");
                for (ItemStack item : tomes){
                    //A list of all the SLAYER tomes

//                    if (!item.getItemMeta().getPersistentDataContainer().has(questGoalKey, PersistentDataType.INTEGER)){
//                        item.getItemMeta().getPersistentDataContainer().set(questGoalKey, PersistentDataType.INTEGER, 0);
//                        item.getItemMeta().getPersistentDataContainer().set(questCurrentKey, PersistentDataType.INTEGER, 0);
//                    }

                    ItemMeta tomeMeta = item.getItemMeta();

                    Slayer slayer = new Slayer();
                    slayer.parseIntoObject(tomeMeta.getPersistentDataContainer().get(questCurrentKey, PersistentDataType.INTEGER), tomeMeta.getPersistentDataContainer().get(questTargetKey, PersistentDataType.INTEGER), EntityType.valueOf(tomeMeta.getPersistentDataContainer().get(questTargetEntity, PersistentDataType.STRING)));
                    slayer.incrementCurrentMobCount(1);

                    slayer.updateQuestData(item);

//                    tomeMeta.getPersistentDataContainer().set(questCurrentKey, PersistentDataType.INTEGER, (tomeMeta.getPersistentDataContainer().get(questCurrentKey, PersistentDataType.INTEGER) + 1));
//
//                    //List<String> tomeLore = new List<String>();
//                    List<String> tomeLore = tomeMeta.getLore();
//                    tomeLore.remove(tomeLore.size() - 1);
//                    tomeLore.add(tomeMeta.getPersistentDataContainer().get(questCurrentKey, PersistentDataType.INTEGER).toString() + "/" + item.getItemMeta().getPersistentDataContainer().get(questGoalKey, PersistentDataType.INTEGER).toString());
//
//                    tomeMeta.setLore(tomeLore);
//
//                    item.setItemMeta(tomeMeta);
                    System.out.println("Name: " + item.getItemMeta().getDisplayName() + " | Lore: " + item.getItemMeta().getLore().get(0));
                }
                //Entity Will Die
            }
        }
    }
}
