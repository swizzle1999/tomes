package com.swizzle.tomes.Events;

import com.swizzle.tomes.GUI.PlayerPageMaps;
import com.swizzle.tomes.GUI.PlayerPagesContainer;
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

import java.util.*;

public class TomeClickEvent implements Listener {

    @EventHandler
    public void onTomeGUIClick(InventoryClickEvent e){

        String title = e.getView().getTitle();
        List<String> titleSplitBySpace= Arrays.asList(title.split(" "));

        //Yes... i hate this as much as you do
        boolean isATomeMenu = false;
        for (String string : titleSplitBySpace){
            if (string.toLowerCase().contains("tome")){
                isATomeMenu = true;
                break;
            }
        }

        if (isATomeMenu){
            e.setCancelled(true);
            if (e.getCurrentItem() != null){
                if (e.getCurrentItem().getItemMeta().getLore() != null && e.getCurrentItem().getItemMeta().getLore().get(0).equals("Random Quests For Rewards!")){
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

                                ConfigurationSection tomeRewardsItemsConfig = Tomes.getInstance().getConfig().getConfigurationSection("tomes." + tome.getTomeVariableName() + ".rewards.items");
                                ConfigurationSection tomeRewardsWeightsConfig = Tomes.getInstance().getConfig().getConfigurationSection("tomes." + tome.getTomeVariableName() + ".rewards.weights");

                                //Storing all of the items we need to fit into these pages in an array list
                                ArrayList<ItemStack> allItems = new ArrayList<>();
                                ArrayList<Integer> allItemsWeights = new ArrayList<>();
                                int totalWeightOfAllItems = 0;
                                if (tomeRewardsItemsConfig != null && tomeRewardsWeightsConfig != null) {
                                    for (String key : tomeRewardsItemsConfig.getKeys(false)) {
                                        ItemStack rewardItem = new ItemStack(tomeRewardsItemsConfig.getItemStack(key));
                                        ItemMeta rewardItemMeta = rewardItem.getItemMeta();

                                        List<String> rewardItemLore;
                                        if (rewardItemMeta.getLore() == null){
                                            rewardItemLore = new ArrayList<>();
                                        } else {
                                            rewardItemLore = rewardItemMeta.getLore();
                                        }


                                        rewardItemMeta.setLore(rewardItemLore);
                                        rewardItem.setItemMeta(rewardItemMeta);

                                        allItems.add(rewardItem);
                                        allItemsWeights.add(Integer.parseInt(tomeRewardsWeightsConfig.getString(key)));
                                        totalWeightOfAllItems += Integer.parseInt(tomeRewardsWeightsConfig.getString(key));

                                    }
                                }

                                for (int i = 0; i < allItems.size(); i++){
                                    System.out.println(allItems.get(i).getData());
                                    ItemMeta itemMeta = allItems.get(i).getItemMeta();

                                    List<String> itemLore;
                                    if (itemMeta.getLore() == null){
                                        itemLore = new ArrayList<>();
                                    } else {
                                        itemLore = itemMeta.getLore();
                                    }


//                                    System.out.println((float)totalWeightOfAllItems);
//                                    System.out.println((float)allItemsWeights.get(i));
                                    float dropChance = ((float)allItemsWeights.get(i) / (float)totalWeightOfAllItems)*100;

                                    itemLore.add("Drop Chance: " + dropChance + "%");
                                    itemLore.add("Weight: " + allItemsWeights.get(i));

                                    itemMeta.setLore(itemLore);
                                    allItems.get(i).setItemMeta(itemMeta);

                                }


                                //Creating the place holder inventory with only the navigation in it
                                //This allows us to find out how many open slots we have to fill with items
                                //It also allows us to take copies of this inventory and use it for all the other inventories to fill
                                Inventory rewardsInventory = Bukkit.createInventory(player, 54, "Tome Rewards");

                                //Left Arrow
                                ItemStack leftArrow = new ItemStack(Material.OAK_SIGN);
                                ItemMeta leftArrowMetaData = leftArrow.getItemMeta();
                                leftArrowMetaData.setDisplayName("Last Page");
                                leftArrow.setItemMeta(leftArrowMetaData);

                                rewardsInventory.setItem(45, leftArrow);

                                //Right Arrow
                                ItemStack rightArrow = new ItemStack(Material.OAK_SIGN);
                                ItemMeta rightArrowMetaData = leftArrow.getItemMeta();
                                rightArrowMetaData.setDisplayName("Next Page");
                                rightArrow.setItemMeta(rightArrowMetaData);

                                rewardsInventory.setItem(53, rightArrow);

                                //Red Glass
                                rewardsInventory.setItem(46, new ItemStack(Material.RED_STAINED_GLASS_PANE));
                                rewardsInventory.setItem(47, new ItemStack(Material.RED_STAINED_GLASS_PANE));
                                rewardsInventory.setItem(48, new ItemStack(Material.RED_STAINED_GLASS_PANE));
                                rewardsInventory.setItem(49, new ItemStack(Material.RED_STAINED_GLASS_PANE));
                                rewardsInventory.setItem(50, new ItemStack(Material.RED_STAINED_GLASS_PANE));
                                rewardsInventory.setItem(51, new ItemStack(Material.RED_STAINED_GLASS_PANE));
                                rewardsInventory.setItem(52, new ItemStack(Material.RED_STAINED_GLASS_PANE));

                                //Finding out how many empty slots we have available in the inventory to fill
                                int numberOfEmptySlots = 0;
                                for (ItemStack slot : rewardsInventory.getContents()){
                                    if (slot == null){
                                        numberOfEmptySlots += 1;
                                    }
                                }

                                //Creating an array list of inventories
                                ArrayList<Inventory> allInventories = new ArrayList<>();

                                //Finding out how many inventories (pages) are actually required based on the number of items we need to fit in and the number of empty slots we have per page
                                int numberOfRequiredInventories = (int) Math.ceil((float)allItems.size() / (float)numberOfEmptySlots);

                                //Formula breaks when there is no rewards so this should be a fix
                                if (numberOfRequiredInventories == 0){
                                    numberOfRequiredInventories = 1;
                                }
                                //A counter to keep track of what item we are adding from the list
                                int currentAllItemsIndex = 0;
                                //Creating all the inventories and filling them with items
                                for (int i = 0; i < numberOfRequiredInventories; i++){

                                    Inventory inventory = Bukkit.createInventory(player, 54, "Tome Rewards - Page " + (i + 1));
                                    inventory.setContents(rewardsInventory.getContents());

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
                                PlayerPageMaps.updatePlayerRewardPageMap(playerPagesContainer.getPlayerID(), playerPagesContainer);

                                //Opening the correct inventory
                                e.getWhoClicked().openInventory(playerPagesContainer.getCurrentInventory());
                            }

                        }
                    }
                }
                else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("next page")){
                    Player player = (Player) e.getWhoClicked();

                    String menuType = checkWhichTypeOfMenuWasClicked(titleSplitBySpace);
                    if (menuType != null){
                        //If we are navigating through a rewards menu
                        if (menuType.equalsIgnoreCase("rewards")){
                            PlayerPagesContainer playerPagesContainer = PlayerPageMaps.getPlayerRewardPageMap().get(player.getUniqueId());
                            playerPagesContainer.incrementPage();

                            e.getWhoClicked().openInventory(playerPagesContainer.getCurrentInventory());
                        }
                        //Navigating through the main tomes menu
                        else if (menuType.equalsIgnoreCase("tomes")){
                            PlayerPagesContainer playerPagesContainer = PlayerPageMaps.getPlayerTomesPageMap().get(player.getUniqueId());
                            playerPagesContainer.incrementPage();

                            e.getWhoClicked().openInventory(playerPagesContainer.getCurrentInventory());
                        }
                    }

                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("last page")){
                    Player player = (Player) e.getWhoClicked();

                    String menuType = checkWhichTypeOfMenuWasClicked(titleSplitBySpace);
                    if (menuType != null){
                        //If we are navigating through a rewards menu
                        if (menuType.equalsIgnoreCase("rewards")){
                            PlayerPagesContainer playerPagesContainer = PlayerPageMaps.getPlayerRewardPageMap().get(player.getUniqueId());
                            playerPagesContainer.decrementPage();

                            e.getWhoClicked().openInventory(playerPagesContainer.getCurrentInventory());
                        }
                        //Navigating through the main tomes menu
                        else if (menuType.equalsIgnoreCase("tomes")){
                            PlayerPagesContainer playerPagesContainer = PlayerPageMaps.getPlayerTomesPageMap().get(player.getUniqueId());
                            playerPagesContainer.decrementPage();

                            e.getWhoClicked().openInventory(playerPagesContainer.getCurrentInventory());
                        }
                    }
                }
            }
        }
    }

    public String checkWhichTypeOfMenuWasClicked(List<String> titleSplitBySpace){
        //yes this is also awfull but i dont know how to identify inventories without just checking the title.

        String menuType = null;
        for (String string : titleSplitBySpace){
            if (string.equalsIgnoreCase("rewards")){
                menuType = "rewards";
                break;
            } else if (string.equalsIgnoreCase("tomes")){
                menuType = "tomes";
                break;
            }
        }

        return menuType;
    }
}
