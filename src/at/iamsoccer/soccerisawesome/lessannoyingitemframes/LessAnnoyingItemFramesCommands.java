package at.iamsoccer.soccerisawesome.lessannoyingitemframes;

import at.iamsoccer.soccerisawesome.SoccerIsAwesomePlugin;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("sia")
public class LessAnnoyingItemFramesCommands extends BaseCommand {
    private final SoccerIsAwesomePlugin plugin;

    public LessAnnoyingItemFramesCommands(SoccerIsAwesomePlugin plugin) {this.plugin = plugin;}

    @Subcommand("rotationtoggle")
    @CommandPermission("sia.rotationtoggle")
    public void onAllRotationToggle(CommandSender sender) {
        togglePermission(sender, "sia.rotationtoggle", "All item frame rotation has been Enabled", "All item frame rotation has been Disabled");
    }

    @Subcommand("containertoggle")
    @CommandPermission("sia.containertoggle")
    public void onAllContainerToggle(CommandSender sender) {
        togglePermission(sender, "sia.containertoggle", "Item frame rotation on Chests and Barrels has been Enabled", "Item frame rotation on Chests and Barrels has been Disabled");
    }

    @Subcommand("clickthroughtoggle")
    @CommandPermission("sia.clickthroughtoggle")
    public void onClickThroughToggle(CommandSender sender) {
        togglePermission(sender, "sia.clickthroughtoggle", "Item frame click through on Chests and Barrels has been Enabled", "Item frame click through on Chests and Barrels has been Disabled");
    }


    // Just so there's less spam in all the commands, adds or removes the permission after the player executes a command and sends an enabled or disabled message
    public void togglePermission(CommandSender sender, String permission, String messageEnabled, String messageDisabled) {
        LuckPerms luckPerms = LuckPermsProvider.get();
        Player player = (Player) sender;
        User user = luckPerms.getPlayerAdapter(Player.class).getUser(player);
        if (sender.hasPermission(permission + ".enabled")) {
            user.data().remove(Node.builder(permission + ".enabled").build());
            LuckPermsProvider.get().getUserManager().saveUser(user);
            sender.sendMessage(ChatColor.RED + messageDisabled);
        } else {
            user.data().add(Node.builder(permission + ".enabled").build());
            LuckPermsProvider.get().getUserManager().saveUser(user);
            sender.sendMessage(ChatColor.GREEN + messageEnabled);
        }
    }
}
