package at.hugo.bukkit.plugin.damagenullifieronteleportorjoin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import co.aikar.commands.PaperCommandManager;

public class DamageNullifierOnTeleportOrJoinPlugin extends JavaPlugin {
    private NullifyListener nullifyListener;

    @Override
    public void onEnable() {
        updateConfig();
        reloadConfig();
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new DamageNullifierOnTeleportOrJoinCommand(this));
        nullifyListener = new NullifyListener();
        getServer().getPluginManager().registerEvents(nullifyListener, this);
        reload();
    }

    public void reload() {
        saveDefaultConfig();
        reloadConfig();
        nullifyListener.update(getConfig().getLong("min-tp-distance"), getConfig().getLong("immunity-time"),
                getConfig().getBoolean("bossbar.show"), getConfig().getString("bossbar.name"),
                getConfig().getString("bossbar.color"), getConfig().getString("bossbar.type"));
    }

    private void updateConfig() {
        saveDefaultConfig();
        reloadConfig();
        Path configFilePath = Path.of(getDataFolder().getPath(), "config.yml");
        if (!Files.exists(configFilePath)) {
            saveDefaultConfig();
            return;
        }

        int version = getConfig().getInt("version", 1);
        LinkedList<String> lines;
        try {
            lines = new LinkedList<>(Files.readAllLines(configFilePath));
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Could not update config!", e);
            return;
        }
        if (version < 2) {
            addConfigOption(lines, "min-tp-distance");
            updateVersion(lines, 2);
        }
        try {
            Files.write(configFilePath, lines, StandardOpenOption.CREATE);
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Could not update config!", e);
        }
    }

    private void addConfigOption(List<String> lines, String configOption) {
        if (!configOption.contains("."))
            lines.add(String.format("%s: %s", configOption, getConfig().getString(configOption)));
    }

    private void updateVersion(List<String> lines, int version) {
        lines.removeIf(line -> line.startsWith("version: "));
        lines.add("version: " + version);
    }
}
