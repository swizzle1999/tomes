package com.swizzle.tomes.commands;

import com.swizzle.tomes.TomeTypes.Rewards;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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

                ItemStack dirtTome = new ItemStack(Material.BOOK);
                ItemStack woodTome = new ItemStack(Material.BOOK);
                ItemStack stoneTome = new ItemStack(Material.BOOK);

                //Dirt Meta And Lore
                ItemMeta dirtTomeMeta = dirtTome.getItemMeta();
                dirtTomeMeta.setDisplayName("Dirt Tome");
                ArrayList<String> dirtTomeLore = new ArrayList<>();
                dirtTomeLore.add("Random Quests For Rewards!");
                dirtTomeMeta.setLore(dirtTomeLore);
                dirtTome.setItemMeta(dirtTomeMeta);

                //Wood Meta And Lore
                ItemMeta woodTomeMeta = woodTome.getItemMeta();
                woodTomeMeta.setDisplayName("Wood Tome");
                ArrayList<String> woodTomeLore = new ArrayList<>();
                woodTomeLore.add("Random Quests For Rewards!");
                woodTomeMeta.setLore(woodTomeLore);
                woodTome.setItemMeta(woodTomeMeta);

                //Stone Meta And Lore
                ItemMeta stoneTomeMeta = stoneTome.getItemMeta();
                stoneTomeMeta.setDisplayName("Stone Tome");
                ArrayList<String> stoneTomeLore = new ArrayList<>();
                stoneTomeLore.add("Random Quests For Rewards!");
                stoneTomeMeta.setLore(stoneTomeLore);
                stoneTome.setItemMeta(stoneTomeMeta);

                ItemStack[] menuItems = {dirtTome, woodTome, stoneTome};
                gui.setContents(menuItems);
                player.openInventory(gui);
            } else {
                if (args[0].equalsIgnoreCase("rewards")) {
                    if (args[1].equalsIgnoreCase("add")) {
                        if (args[2].equalsIgnoreCase("wood")) {
                            ItemStack newReward = player.getInventory().getItemInMainHand();

                            Rewards rewards = new Rewards();
                            int weight = 0;
                            try {
                                weight = Integer.parseInt(args[3]);
                            } catch (NumberFormatException e){
                                throw e;
                            }
                            rewards.addRewardToHashMap("wood", newReward, weight);
                            rewards.printRewards();
                            System.out.println(newReward.getData());
                        }
                    }
                }
            }
        }
        return false;
    }
}
