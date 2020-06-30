package com.swizzle.tomes.commands;

import com.swizzle.tomes.Events.TomeClickEvent;
import com.swizzle.tomes.GUI.PlayerPageMaps;
import com.swizzle.tomes.GUI.PlayerPagesContainer;
import com.swizzle.tomes.TomeClasses.Tome;
import com.swizzle.tomes.Tomes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TomesCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = ((Player) sender).getPlayer();

            //Command == /tomes
            if (args.length == 0){

                List<ItemStack> allItems = new ArrayList<ItemStack>();

                for (Tome tome : Tomes.getTomes()){
                    ItemStack newTome = new ItemStack(Material.BOOK);

                    ItemMeta newTomeMeta = newTome.getItemMeta();
                    newTomeMeta.setDisplayName(tome.getTomeDisplayName());

                    ArrayList<String> newTomeLore = new ArrayList<>();
                    newTomeLore.add("Random Quests For Rewards!");
                    newTomeLore.add("Right Click To View Possible Rewards");
                    newTomeLore.add("Cost: " + tome.getCost() + " Levels");

                    newTomeMeta.setLore(newTomeLore);
                    newTome.setItemMeta(newTomeMeta);

                    allItems.add(newTome);
                }

                Inventory gui = Bukkit.createInventory(player, 18, "Tomes");

                //Left Arrow
                ItemStack leftArrow = new ItemStack(Material.OAK_SIGN);
                ItemMeta leftArrowMetaData = leftArrow.getItemMeta();
                leftArrowMetaData.setDisplayName("Last Page");
                leftArrow.setItemMeta(leftArrowMetaData);

                gui.setItem(9, leftArrow);

                //Right Arrow
                ItemStack rightArrow = new ItemStack(Material.OAK_SIGN);
                ItemMeta rightArrowMetaData = leftArrow.getItemMeta();
                rightArrowMetaData.setDisplayName("Next Page");
                rightArrow.setItemMeta(rightArrowMetaData);

                gui.setItem(17, rightArrow);

                //Red Glass
                gui.setItem(10, new ItemStack(Material.RED_STAINED_GLASS_PANE));
                gui.setItem(11, new ItemStack(Material.RED_STAINED_GLASS_PANE));
                gui.setItem(12, new ItemStack(Material.RED_STAINED_GLASS_PANE));
                gui.setItem(13, new ItemStack(Material.RED_STAINED_GLASS_PANE));
                gui.setItem(14, new ItemStack(Material.RED_STAINED_GLASS_PANE));
                gui.setItem(15, new ItemStack(Material.RED_STAINED_GLASS_PANE));
                gui.setItem(16, new ItemStack(Material.RED_STAINED_GLASS_PANE));

                //Finding out how many empty slots we have available in the inventory to fill
                int numberOfEmptySlots = 0;
                for (ItemStack slot : gui.getContents()){
                    if (slot == null){
                        numberOfEmptySlots += 1;
                    }
                }

                //Finding out how many inventories (pages) are actually required based on the number of items we need to fit in and the number of empty slots we have per page
                int numberOfRequiredInventories = (int) Math.ceil((float)allItems.size() / (float)numberOfEmptySlots);

                //Formula breaks when there is no rewards so this should be a fix
                if (numberOfRequiredInventories == 0){
                    numberOfRequiredInventories = 1;
                }

                //A counter to keep track of what item we are adding from the list
                int currentAllItemsIndex = 0;
                //Creating an array list of inventories
                ArrayList<Inventory> allInventories = new ArrayList<>();
                //Creating all the inventories and filling them with items
                for (int i = 0; i < numberOfRequiredInventories; i++){

                    Inventory inventory = Bukkit.createInventory(player, 18, "Tomes - Page " + (i + 1));
                    inventory.setContents(gui.getContents());

                    for (int j = 0; j < numberOfEmptySlots; j++){
                        if (currentAllItemsIndex > allItems.size() - 1){
                            break;
                        }

                        if (allItems.size() > 0) {
                            inventory.setItem(j, allItems.get(currentAllItemsIndex));
                        }

                        currentAllItemsIndex += 1;
                    }

                    allInventories.add(inventory);
                }

                //Creating a player pages container object and instantiating it with the player who it belongs to, the page number they are on and all of the inventories (pages)
                PlayerPagesContainer playerPagesContainer = new PlayerPagesContainer(player.getUniqueId(), 0, allInventories);
                PlayerPageMaps.updatePlayerTomesPageMap(player.getUniqueId(), playerPagesContainer);

                //Opening the correct inventory
                player.openInventory(playerPagesContainer.getCurrentInventory());

            }
            //Command == /tomes <something>
            else {
                String editTomesPermission = "tomes.editTomes";
                if (player.hasPermission(editTomesPermission)) {
                    if (args[0].equalsIgnoreCase("rewards")) {
                        if (args[2].equalsIgnoreCase("add")) {
                            if (args[3] == null) {
                                sender.sendMessage(ChatColor.RED + "Please add in a weight");
                                return false;
                            }
                            ItemStack newReward = player.getInventory().getItemInMainHand();

                            int index = 0;

                            ConfigurationSection configSection = Tomes.getInstance().getConfig().getConfigurationSection("tomes." + args[1] + ".rewards.items");
                            if (configSection == null) {
                                index = 0;
                            } else {
                                for (String key : configSection.getKeys(false)) {
                                    try {
                                        //Adding one to the index so as to not overwrite the last index
                                        index = Integer.parseInt(key) + 1;
                                    } catch (NumberFormatException e) {
                                        System.out.println("A key inside of the loot table is not parseable as an integer");
                                    }
                                }
                            }

                            Tomes.getInstance().getConfig().set("tomes." + args[1] + ".rewards.items." + String.valueOf(index), newReward);

                            configSection = Tomes.getInstance().getConfig().getConfigurationSection("tomes." + args[1] + ".rewards.weights");
                            if (configSection == null) {
                                index = 0;
                            } else {
                                for (String key : configSection.getKeys(false)) {
                                    try {
                                        //Adding one to the index so as to not overwrite the last index
                                        index = Integer.parseInt(key) + 1;
                                    } catch (NumberFormatException e) {
                                        System.out.println("A key inside of the loot table is not parseable as an integer");
                                    }
                                }
                            }

                            Tomes.getInstance().getConfig().set("tomes." + args[1] + ".rewards.weights." + String.valueOf(index), Integer.parseInt(args[3]));

                            Tomes.getInstance().saveConfig();
                        }

                    }
                } else {
                    player.sendMessage("Sorry, You do not have the required permission (" + editTomesPermission + ")");
                }
            }
        }
        return false;
    }
}
