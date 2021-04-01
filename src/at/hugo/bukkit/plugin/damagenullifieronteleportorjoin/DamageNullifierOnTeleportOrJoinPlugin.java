package at.hugo.bukkit.plugin.damagenullifieronteleportorjoin;

import org.bukkit.plugin.java.JavaPlugin;

import co.aikar.commands.PaperCommandManager;

public class DamageNullifierOnTeleportOrJoinPlugin extends JavaPlugin {
    private NullifyListener nullifyListener;

    @Override
    public void onEnable() {
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new DamageNullifierOnTeleportOrJoinCommand(this));
        nullifyListener = new NullifyListener();
        getServer().getPluginManager().registerEvents(nullifyListener, this);
        reload();
    }

    public void reload() {
        saveDefaultConfig();
        reloadConfig();
        nullifyListener.update(getConfig().getLong("immunity-time"), getConfig().getBoolean("bossbar.show"),
                getConfig().getString("bossbar.name"), getConfig().getString("bossbar.color"),
                getConfig().getString("bossbar.type"));
    }
}
