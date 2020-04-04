package me.aberdeener.ezdev;

import lombok.Getter;
import me.aberdeener.ezdev.ingest.ScriptHandler;
import me.aberdeener.ezdev.variables.VariableManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ezDev extends JavaPlugin {

    @Getter
    public static ezDev instance;
    long startTime;
    @Getter
    public static List<String> activeScripts = new ArrayList<>();

    File scriptFolder = new File(getDataFolder() + File.separator + "scripts");

    public void onEnable() {
        instance = this;
        startTime = System.currentTimeMillis();
        if (!scriptFolder.exists()) {
            scriptFolder.mkdirs();
        }
        initScripts();
        VariableManager.initVariables();
        Registry.registerListeners();
        Registry.registerCommands();
        getLogger().info("Started in " + (System.currentTimeMillis() - startTime) + "ms!");
    }
    public void onDisable() {
    }

    private void initScripts() {
        getLogger().info("Pre-loading user scripts...");
        try {
            for (File script : scriptFolder.listFiles()) {
                if (script.getName().endsWith(".ez")){
                    getLogger().info("Loading script " + script.getName() + "...");
                    ScriptHandler.handle(script);
                } else {
                    getLogger().warning("Skipped script "  + script.getName() + ": Incorrect file extension.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            getLogger().severe("Fatal error pre-loading user scripts.");
            getPluginLoader().disablePlugin(this);
        }
    }
}
