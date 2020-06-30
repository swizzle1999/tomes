package com.swizzle.tomes;

import com.swizzle.tomes.Events.TomeClickEvent;
import com.swizzle.tomes.Events.TomeQuestProgressEvent;
import com.swizzle.tomes.Events.TomeRightClickEvent;
import com.swizzle.tomes.QuestTypes.Fish;
import com.swizzle.tomes.QuestTypes.IQuest;
import com.swizzle.tomes.QuestTypes.Mine;
import com.swizzle.tomes.QuestTypes.Slayer;
import com.swizzle.tomes.TomeClasses.Tome;
import com.swizzle.tomes.commands.TomesCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;


public final class Tomes extends JavaPlugin {

    private static Tomes instance;
    private static ArrayList<Tome> tomes = new ArrayList<Tome>();
    private static ArrayList<IQuest> quests;

    @Override
    public void onEnable() {
        instance = this;

        quests = new ArrayList<IQuest>(Arrays.asList(
                new Slayer(0, null, 0, 0),
                new Mine(0, null, 0, 0),
                new Fish(0, 0, 0)
        ));

        //Registering Events
        getServer().getPluginManager().registerEvents(new TomeClickEvent(), this);
        getServer().getPluginManager().registerEvents(new TomeQuestProgressEvent(), this);
        getServer().getPluginManager().registerEvents(new TomeRightClickEvent(), this);

        //Registering Commands
        getCommand("tomes").setExecutor(new TomesCommand());

        loadConfig();

        instantiateTomes();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void instantiateTomes(){

        //Retrieve all tomes from the config file, instantiate them as a new tome with all of their values from the config file
        for (String tomeKey : this.getConfig().getConfigurationSection("tomes").getKeys(false)){

            if (this.getConfig().getBoolean("tomes." + tomeKey + ".enabled") == false){
                continue;
            }

            ArrayList<IQuest> questsToAddToTome = new ArrayList<IQuest>();
            for (String questKey : this.getConfig().getConfigurationSection("tomes." + tomeKey + ".quests").getKeys(false)){
                for (IQuest quest : quests){
                    if (questKey.equalsIgnoreCase(quest.getQuestName())){
                        questsToAddToTome.add(quest);
                    }
                }
            }

            //Fetch rewards of each tome
            ConfigurationSection itemsSection = Tomes.getInstance().getConfig().getConfigurationSection("tomes." + tomeKey + ".rewards.items");
            ConfigurationSection weightsSection = Tomes.getInstance().getConfig().getConfigurationSection("tomes." + tomeKey + ".rewards.weights");

            ArrayList<ItemStack> rewardsArray = new ArrayList<ItemStack>();

            if (itemsSection != null) {
                for (String key : itemsSection.getKeys(false)) {
                    ItemStack item = itemsSection.getItemStack(key);

                    for (int i = 0; i < Integer.parseInt(weightsSection.getString(key)); i++) {
                        rewardsArray.add(item);
                    }
                }
            }

            ConfigurationSection tomeConfig = this.getConfig().getConfigurationSection("tomes." + tomeKey);
            tomes.add(new Tome(
                    tomeKey,
                    tomeConfig.getString("displayName"),
                    tomeConfig.getInt("numberOfQuests"),
                    tomeConfig.getInt("cost"),
                    questsToAddToTome,
                    rewardsArray
            ));


        }
    }

    public void loadConfig(){
        //With the two below lines uncommented it resulted in some strange behaviour where if there was a syntax error in the configuration it would delete ALL of the config and just copy the default again.
        //This is obviously not good and could lead to a lot of lost data so until i figure out a solution it will stay like this
        //This may actually be a good solution
        //this.getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();
        //this.saveConfig();


    }

    public static ArrayList<Tome> getTomes() {
        return tomes;
    }

    public static Tomes getInstance(){
        return instance;
    }
}
