package com.swizzle.tomes.Events;

import com.swizzle.tomes.QuestTypes.Fish;
import com.swizzle.tomes.QuestTypes.Mine;
import com.swizzle.tomes.QuestTypes.Slayer;
import com.swizzle.tomes.TomeClasses.Tome;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class TomeQuestProgressEvent implements Listener {

    private ArrayList<Block> previouslyMinedBlocks = new ArrayList<Block>();

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
                    } else {
                        continue;
                    }
                }

                ArrayList<ItemStack> tomes = new ArrayList<>();
                ArrayList<Integer> questIndexs = new ArrayList<>();
                //Itterate through all items
                for (int i = 0; i < items.size(); i++){
                    //If the item IS a tome
                    if (items.get(i).getItemMeta().getPersistentDataContainer().has(Tome.getTomeKey(), PersistentDataType.INTEGER) && items.get(i).getItemMeta().getPersistentDataContainer().get(Tome.getTomeCompleteKey(), PersistentDataType.INTEGER) == 0){
                        //Get the number of quests on it
                        int numberOfQuestsOnTome = items.get(i).getItemMeta().getPersistentDataContainer().get(Tome.getTomeNumberOfQuestsKey(), PersistentDataType.INTEGER);
                        Slayer throwawayInstance = new Slayer(0, null, 0, 0);
                        //For loop to itterate through each possible quest index
                        for (int j = 0; j < numberOfQuestsOnTome; j++){
                            if (items.get(i).getItemMeta().getPersistentDataContainer().get(Tome.getQuestTypeKey(j), PersistentDataType.STRING).equalsIgnoreCase(throwawayInstance.getQuestName())){
                                tomes.add(items.get(i));
                                questIndexs.add(j);
                            }
                        }
                    }
                }

                for (int i = 0; i < tomes.size(); i++){
                    //A list of all the SLAYER tomes
                    ItemStack item = tomes.get(i);
                    int questIndex = questIndexs.get(i);

                    ItemMeta tomeMeta = item.getItemMeta();

                    Slayer throwAwayInstance = new Slayer(questIndex, null, 0, 0);
                    Slayer slayer = new Slayer(questIndex, EntityType.valueOf(tomeMeta.getPersistentDataContainer().get(throwAwayInstance.getSlayerTargetEntityKey(), PersistentDataType.STRING)), tomeMeta.getPersistentDataContainer().get(throwAwayInstance.getSlayerCurrentKey(), PersistentDataType.INTEGER), tomeMeta.getPersistentDataContainer().get(throwAwayInstance.getSlayerTargetKey(), PersistentDataType.INTEGER));

                    if (e.getEntityType().equals(slayer.getEntityType())){
                        slayer.incrementCurrentMobCount(1, item);
                    }

                    //System.out.println("Name: " + item.getItemMeta().getDisplayName() + " | Lore: " + item.getItemMeta().getLore().get(0));

                    Tome.checkIfTomeIsComplete(player, item);
                }
                //Entity Will Die
            }
        }
    }

    //Mine Progress
    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e){
        Player player = e.getPlayer();
        Inventory inventory = player.getInventory();

        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        for (ItemStack item : inventory.getContents()){
            if (item != null) {
                items.add(item);
            } else {
                continue;
            }
        }


        ArrayList<ItemStack> tomes = new ArrayList<>();
        ArrayList<Integer> questIndexs = new ArrayList<>();
        //Itterate through all items
        for (int i = 0; i < items.size(); i++){
            //If the item IS a tome
            if (items.get(i).getItemMeta().getPersistentDataContainer().has(Tome.getTomeKey(), PersistentDataType.INTEGER) && items.get(i).getItemMeta().getPersistentDataContainer().get(Tome.getTomeCompleteKey(), PersistentDataType.INTEGER) == 0){
                //Get the number of quests on it
                int numberOfQuestsOnTome = items.get(i).getItemMeta().getPersistentDataContainer().get(Tome.getTomeNumberOfQuestsKey(), PersistentDataType.INTEGER);
                Mine throwawayInstance = new Mine(0, null, 0, 0);
                //For loop to itterate through each possible quest index
                for (int j = 0; j < numberOfQuestsOnTome; j++){
                    if (items.get(i).getItemMeta().getPersistentDataContainer().get(Tome.getQuestTypeKey(j), PersistentDataType.STRING).equalsIgnoreCase(throwawayInstance.getQuestName())){
                        tomes.add(items.get(i));
                        questIndexs.add(j);
                    }
                }
            }
        }

        for (int i = 0; i < tomes.size(); i++){
            //A list of all the MINE tomes
            ItemStack item = tomes.get(i);
            int questIndex = questIndexs.get(i);

            ItemMeta tomeMeta = item.getItemMeta();

            Mine throwAwayInstance = new Mine(questIndex, null, 0, 0);
            Mine mine = new Mine(questIndex, Material.valueOf(tomeMeta.getPersistentDataContainer().get(throwAwayInstance.getMineTargetMaterialKey(), PersistentDataType.STRING)), tomeMeta.getPersistentDataContainer().get(throwAwayInstance.getMineCurrentKey(), PersistentDataType.INTEGER), tomeMeta.getPersistentDataContainer().get(throwAwayInstance.getMineTargetKey(), PersistentDataType.INTEGER));

            if (e.getBlock().getBlockData().getMaterial().equals(mine.getMaterial())){

                if (!previouslyMinedBlocks.contains(e.getBlock())){
                    mine.incrementCurrentMineCount(1, item);
                    previouslyMinedBlocks.add(e.getBlock());

                    //Start removing blocks from the list after it is 100 blocks long to stop excessive memory usage.
                    //Could be removed later if it was found to not be that heavy on memory usage
                    if (previouslyMinedBlocks.size() >= 100){
                        previouslyMinedBlocks.remove(0);
                    }
                }
            }

            //Just a debug statement
            //System.out.println("Name: " + item.getItemMeta().getDisplayName() + " | Lore: " + item.getItemMeta().getLore().get(0));

            Tome.checkIfTomeIsComplete(player, item);
        }
        //Entity Will Die
    }


    //Fish Progress
    @EventHandler
    public void onPlayerCatchFish(PlayerFishEvent e){
        Player player = e.getPlayer();
        Inventory inventory = player.getInventory();

        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        for (ItemStack item : inventory.getContents()){
            if (item != null) {
                items.add(item);
            } else {
                continue;
            }
        }


        ArrayList<ItemStack> tomes = new ArrayList<>();
        ArrayList<Integer> questIndexs = new ArrayList<>();
        //Itterate through all items
        for (int i = 0; i < items.size(); i++){
            //If the item IS a tome
            if (items.get(i).getItemMeta().getPersistentDataContainer().has(Tome.getTomeKey(), PersistentDataType.INTEGER) && items.get(i).getItemMeta().getPersistentDataContainer().get(Tome.getTomeCompleteKey(), PersistentDataType.INTEGER) == 0){
                //Get the number of quests on it
                int numberOfQuestsOnTome = items.get(i).getItemMeta().getPersistentDataContainer().get(Tome.getTomeNumberOfQuestsKey(), PersistentDataType.INTEGER);
                Fish throwawayInstance = new com.swizzle.tomes.QuestTypes.Fish(0, 0, 0);
                //For loop to itterate through each possible quest index
                for (int j = 0; j < numberOfQuestsOnTome; j++){
                    if (items.get(i).getItemMeta().getPersistentDataContainer().get(Tome.getQuestTypeKey(j), PersistentDataType.STRING).equalsIgnoreCase(throwawayInstance.getQuestName())){
                        tomes.add(items.get(i));
                        questIndexs.add(j);
                    }
                }
            }
        }

        for (int i = 0; i < tomes.size(); i++){
            //A list of all the FISH tomes
            ItemStack item = tomes.get(i);
            int questIndex = questIndexs.get(i);

            ItemMeta tomeMeta = item.getItemMeta();

            Fish throwAwayInstance = new Fish(questIndex, 0, 0);
            Fish fish = new Fish(questIndex, tomeMeta.getPersistentDataContainer().get(throwAwayInstance.getFishCurrentKey(), PersistentDataType.INTEGER), tomeMeta.getPersistentDataContainer().get(throwAwayInstance.getFishTargetKey(), PersistentDataType.INTEGER));

            if (e.getState() == PlayerFishEvent.State.CAUGHT_FISH){
                fish.incrementCurrentFishCount(1, item);
            }

            Tome.checkIfTomeIsComplete(player, item);
        }
        //Entity Will Die
    }
}
