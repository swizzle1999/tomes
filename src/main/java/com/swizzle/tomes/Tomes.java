package com.swizzle.tomes;

import com.swizzle.tomes.Events.TomeClickEvent;
import com.swizzle.tomes.Events.TomeQuestProgressEvent;
import com.swizzle.tomes.commands.TomesCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Tomes extends JavaPlugin {

    private static Tomes instance;

    @Override
    public void onEnable() {
        instance = this;

        //Registering Events
        getServer().getPluginManager().registerEvents(new TomeClickEvent(), this);
        getServer().getPluginManager().registerEvents(new TomeQuestProgressEvent(), this);

        //Registering Commands
        getCommand("tomes").setExecutor(new TomesCommand());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Tomes getInstance(){
        return instance;
    }
}
