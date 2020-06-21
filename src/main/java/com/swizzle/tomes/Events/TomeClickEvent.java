package com.swizzle.tomes.Events;

import com.swizzle.tomes.TomeClasses.Tome;
import com.swizzle.tomes.Tomes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class TomeClickEvent implements Listener {
    @EventHandler
    public void onTomeGUIClick(InventoryClickEvent e){

        if (e.getView().getTitle().equalsIgnoreCase("tomes") || e.getView().getTitle().equalsIgnoreCase("Tome Rewards")){
            if (e.getCurrentItem() != null && e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().get(0).equals("Random Quests For Rewards!")){
                //Check which tome was clicked
                for (Tome tome : Tomes.getTomes()){
                    if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(tome.getTomeDisplayName())){
                        if (e.getClick() == ClickType.LEFT) {
                            if (((Player) e.getWhoClicked()).getLevel() >= Tomes.getInstance().getConfig().getInt("tomes." + tome.getTomeVariableName() + ".cost")) {
                                ((Player) e.getWhoClicked()).setLevel(((Player) e.getWhoClicked()).getLevel() - Tomes.getInstance().getConfig().getInt("tomes." + tome.getTomeVariableName() + ".cost"));
                                e.getWhoClicked().getInventory().addItem(tome.giveBook());
                            } else {
                                e.getWhoClicked().sendMessage(ChatColor.RED + "Sorry, You do not have the required levels to purchase this tome");
                            }
                        } else if (e.getClick() == ClickType.RIGHT){
                            Player player = (Player) e.getWhoClicked();
                            Inventory rewardsInventory = Bukkit.createInventory(player, 54, "Tome Rewards");

                            rewardsInventory.setItem(45, new ItemStack(Material.OAK_SIGN));

                            ConfigurationSection tomeRewardsItemsConfig = Tomes.getInstance().getConfig().getConfigurationSection("tomes." + tome.getTomeVariableName() + ".rewards.items");
                            ConfigurationSection tomeRewardsWeightsConfig = Tomes.getInstance().getConfig().getConfigurationSection("tomes." + tome.getTomeVariableName() + ".rewards.weights");

                            if (tomeRewardsItemsConfig != null && tomeRewardsWeightsConfig != null) {
                                for (String key : tomeRewardsItemsConfig.getKeys(false)) {
                                    ItemStack rewardItem = tomeRewardsItemsConfig.getItemStack(key);
                                    ItemMeta rewardItemMeta = rewardItem.getItemMeta();
                                    ArrayList<String> rewardItemLore = new ArrayList<String>();

                                    rewardItemLore.add("Weight: " + tomeRewardsWeightsConfig.getString(key));

                                    rewardItemMeta.setLore(rewardItemLore);
                                    rewardItem.setItemMeta(rewardItemMeta);

                                    rewardsInventory.addItem(rewardItem);
                                }
                            }

                            for (int i = 0; i < 300; i++){
                                rewardsInventory.addItem(new ItemStack(Material.DIRT, 64));
                            }

                            e.getWhoClicked().openInventory(rewardsInventory);
                        }

                    }
                }
            }

            e.setCancelled(true);
        }


    }
}
