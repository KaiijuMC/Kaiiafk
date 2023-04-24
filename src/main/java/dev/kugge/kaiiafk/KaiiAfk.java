package dev.kugge.kaiiafk;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class KaiiAfk extends JavaPlugin {

    public static Logger logger;
    public static KaiiAfk instance;

    @Override
    public void onEnable() {
        instance = this;
        logger = getLogger();
        register();
    }

    private void register(){
        Bukkit.getGlobalRegionScheduler().runAtFixedRate(this, task -> new AfkWatcher().run(), 10, 100);
    }
}
