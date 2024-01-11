package at.iamsoccer.soccerisawesome.essentialsafkhook;

import at.iamsoccer.soccerisawesome.SoccerIsAwesomePlugin;
import org.bukkit.command.CommandSender;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;

@CommandAlias("essentialsafk")
public class EssentialsAFKHookCommands extends BaseCommand {
    private final SoccerIsAwesomePlugin plugin;

    public EssentialsAFKHookCommands(SoccerIsAwesomePlugin plugin) {
        this.plugin = plugin;
    }

    @Subcommand("reload")
    @CommandPermission("sia.essentialsafk.reload")
    public void onReload(CommandSender sender) {
        sender.sendMessage("[Essentials AFK Hook] Reloading");
        this.plugin.EssentialsAFKHookListener.reload();
        sender.sendMessage("[Essentials AFK Hook] Reloaded");
    }
}