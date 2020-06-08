package com.swizzle.tomes.commands;

import com.swizzle.tomes.TomeObject;
import com.swizzle.tomes.TomeTypes.Tome;
import com.swizzle.tomes.Tomes;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class TomesCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = ((Player) sender).getPlayer();
            if (args.length == 0){
                Inventory gui = Bukkit.createInventory(player, 9, "Tomes");

                ItemStack[] menuItems = new ItemStack[TomeObject.tomes.size()];

                int counter = 0;
                for (Tome tome : TomeObject.tomes){
                    ItemStack newTome = new ItemStack(Material.BOOK);

                    ItemMeta newTomeMeta = newTome.getItemMeta();
                    newTomeMeta.setDisplayName(tome.getTomeDisplayName());

                    ArrayList<String> newTomeLore = new ArrayList<>();
                    newTomeLore.add("Random Quests For Rewards!");

                    newTomeMeta.setLore(newTomeLore);
                    newTome.setItemMeta(newTomeMeta);

                    menuItems[counter] = newTome;

                    counter += 1;
                }

//                ItemStack dirtTome = new ItemStack(Material.BOOK);
//                ItemStack woodTome = new ItemStack(Material.BOOK);
//                ItemStack stoneTome = new ItemStack(Material.BOOK);
//
//                //Dirt Meta And Lore
//                ItemMeta dirtTomeMeta = dirtTome.getItemMeta();
//                dirtTomeMeta.setDisplayName("Dirt Tome");
//                ArrayList<String> dirtTomeLore = new ArrayList<>();
//                dirtTomeLore.add("Random Quests For Rewards!");
//                dirtTomeMeta.setLore(dirtTomeLore);
//                dirtTome.setItemMeta(dirtTomeMeta);
//
//                //Wood Meta And Lore
//                ItemMeta woodTomeMeta = woodTome.getItemMeta();
//                woodTomeMeta.setDisplayName("Wood Tome");
//                ArrayList<String> woodTomeLore = new ArrayList<>();
//                woodTomeLore.add("Random Quests For Rewards!");
//                woodTomeMeta.setLore(woodTomeLore);
//                woodTome.setItemMeta(woodTomeMeta);
//
//                //Stone Meta And Lore
//                ItemMeta stoneTomeMeta = stoneTome.getItemMeta();
//                stoneTomeMeta.setDisplayName("Stone Tome");
//                ArrayList<String> stoneTomeLore = new ArrayList<>();
//                stoneTomeLore.add("Random Quests For Rewards!");
//                stoneTomeMeta.setLore(stoneTomeLore);
//                stoneTome.setItemMeta(stoneTomeMeta);
//
//                ItemStack[] menuItems = {dirtTome, woodTome, stoneTome};
                gui.setContents(menuItems);
                player.openInventory(gui);
            } else {
                if (args[0].equalsIgnoreCase("rewards")) {
                    if (args[1].equalsIgnoreCase("add")) {
                        if (args[3] == null){
                            System.out.println("No weight argument.");
                            return false;
                        }
                        ItemStack newReward = player.getInventory().getItemInMainHand();

                        int index = 0;

                        ConfigurationSection configSection = Tomes.getInstance().getConfig().getConfigurationSection("loottable." + args[2] + ".items");
                        if (configSection == null){
                            System.out.println("Configuration Section Is Empty");
                            index = 0;
                        } else {
                            for (String key : configSection.getKeys(false)){
                                try {
                                    //Adding one to the index so as to not overwrite the last index
                                    index = Integer.parseInt(key) + 1;
                                } catch (NumberFormatException e){
                                    System.out.println("A key inside of the loot table is not parseable as an integer");
                                }
                            }
                        }

                        Tomes.getInstance().getConfig().set("loottable." + args[2] + ".items." + String.valueOf(index), newReward);

                        configSection = Tomes.getInstance().getConfig().getConfigurationSection("loottable." + args[2] + ".weights");
                        if (configSection == null){
                            System.out.println("Configuration Section Is Empty");
                            index = 0;
                        } else {
                            for (String key : configSection.getKeys(false)){
                                try {
                                    //Adding one to the index so as to not overwrite the last index
                                    index = Integer.parseInt(key) + 1;
                                } catch (NumberFormatException e){
                                    System.out.println("A key inside of the loot table is not parseable as an integer");
                                }
                            }
                        }

                        Tomes.getInstance().getConfig().set("loottable." + args[2] + ".weights." + String.valueOf(index), args[3]);

                        Tomes.getInstance().saveConfig();
                    }

                }
            }
        }
        return false;
    }
}
