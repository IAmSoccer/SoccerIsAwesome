package at.iamsoccer.soccerisawesome.damagenullifier;

import at.iamsoccer.soccerisawesome.SoccerIsAwesomePlugin;
import org.bukkit.command.CommandSender;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;

@CommandAlias("damagenullifier")
public class DamageNullifierOnTeleportOrJoinCommand extends BaseCommand {
    private final SoccerIsAwesomePlugin plugin;

    public DamageNullifierOnTeleportOrJoinCommand(SoccerIsAwesomePlugin plugin) {
        this.plugin = plugin;
    }

    @Subcommand("reload")
    @CommandPermission("damagenullifier.reload")
    public void onReload(CommandSender sender) {
        sender.sendMessage("[DamageNullifier] Reloading");
        plugin.reload();
        sender.sendMessage("[DamageNullifier] Reloaded");
    }
}
