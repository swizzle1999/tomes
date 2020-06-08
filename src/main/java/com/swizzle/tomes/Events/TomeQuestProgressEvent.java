package com.swizzle.tomes.Events;

import com.swizzle.tomes.QuestTypes.Slayer;
import com.swizzle.tomes.TomeObject;
import com.swizzle.tomes.Tomes;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

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
                NamespacedKey numberOfQuestsKey = new NamespacedKey(Tomes.getInstance(), "NumberOfQuests");

                ArrayList<ItemStack> tomes = new ArrayList<>();
                ArrayList<Integer> questIndexs = new ArrayList<>();
                //Itterate through all items
                for (int i = 0; i < items.size(); i++){
                    //If the item IS a tome
                    if (items.get(i).getItemMeta().getPersistentDataContainer().has(tomeKey, PersistentDataType.INTEGER) && items.get(i).getItemMeta().getPersistentDataContainer().get(TomeObject.tomeCompleteKey, PersistentDataType.INTEGER) == 0){
                        //Get the number of quests on it
                        int numberOfQuestsOnTome = items.get(i).getItemMeta().getPersistentDataContainer().get(numberOfQuestsKey, PersistentDataType.INTEGER);
                        Slayer throwawayInstance = new Slayer(0);
                        //For loop to itterate through each possible quest index
                        for (int j = 0; j < numberOfQuestsOnTome; j++){
                            NamespacedKey questTypeKey = new NamespacedKey(Tomes.getInstance(), "QuestType"+j);
                            if (items.get(i).getItemMeta().getPersistentDataContainer().get(questTypeKey, PersistentDataType.STRING).equalsIgnoreCase(throwawayInstance.getQuestName())){
                                tomes.add(items.get(i));
                                questIndexs.add(j);
                            }
                        }
                    }
                }





                //List<ItemStack> tomes = items.stream().filter(itemStack -> itemStack.getItemMeta().getPersistentDataContainer().has(tomeKey, PersistentDataType.INTEGER)).filter(itemStack -> itemStack.getItemMeta().getPersistentDataContainer().get(questTypeKey, PersistentDataType.STRING).equalsIgnoreCase(throwawayInstance.getQuestName())).collect(Collectors.toList());


                for (int i = 0; i < tomes.size(); i++){
                    //A list of all the SLAYER tomes
                    ItemStack item = tomes.get(i);
                    int questIndex = questIndexs.get(i);

                    NamespacedKey questCurrentKey = new NamespacedKey(Tomes.getInstance(), "SlayerCurrent"+questIndex);
                    NamespacedKey questTargetKey = new NamespacedKey(Tomes.getInstance(), "SlayerTarget"+questIndex);
                    NamespacedKey questTargetEntity = new NamespacedKey(Tomes.getInstance(), "SlayerEntityType"+questIndex);

                    ItemMeta tomeMeta = item.getItemMeta();

                    Slayer slayer = new Slayer(questIndex);
                    slayer.parseIntoObject(tomeMeta.getPersistentDataContainer().get(questCurrentKey, PersistentDataType.INTEGER), tomeMeta.getPersistentDataContainer().get(questTargetKey, PersistentDataType.INTEGER), EntityType.valueOf(tomeMeta.getPersistentDataContainer().get(questTargetEntity, PersistentDataType.STRING)));

                    if (e.getEntityType().equals(slayer.entityType)){
                        slayer.incrementCurrentMobCount(1, item);
                    }


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

                    TomeObject.checkIfTomeIsComplete(item);
                }
                //Entity Will Die
            }
        }
    }
}
