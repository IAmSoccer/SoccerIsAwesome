package at.iamsoccer.soccerisawesome.essentialsafkhook;

import at.iamsoccer.soccerisawesome.SoccerIsAwesomePlugin;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import net.ess3.api.events.AfkStatusChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.io.File;
import java.util.ArrayList;

import static com.earth2me.essentials.I18n.tl;


public class EssentialsAFKHookListener implements Listener {
    private final Essentials essentials;
    private final ArrayList<AFKMessageRecord> permissionMessages = new ArrayList<>();
    private final SoccerIsAwesomePlugin plugin;

    public EssentialsAFKHookListener(SoccerIsAwesomePlugin plugin) {
        this.plugin = plugin;
        essentials = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
        reload();
    }

    public void reload() {
        permissionMessages.clear();
        plugin.saveResource("essentialsafk-config.yml", false);
        var config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(),"essentialsafk-config.yml"));
        for (String permission : config.getKeys(true)) {
            if (!config.isString(permission + ".message") || !config.isString(permission + ".msg-response")) continue;
            final String message = config.getString(permission + ".message");
            final String msgResponse = config.getString(permission + ".msg-response");
            final Permission perm = new Permission(permission, message, PermissionDefault.FALSE);
            permissionMessages.add(new AFKMessageRecord(perm, message, msgResponse));
            registerPermission(perm);
        }
        for (var permissionMessage : permissionMessages) {
            plugin.getLogger().info(permissionMessage.permission.getName() + ": " + permissionMessage.message + " (" + permissionMessage.msgResponse + ")");
        }
    }

    private void registerPermission(Permission perm) {
        if (Bukkit.getPluginManager().getPermission(perm.getName()) != null) return;
        Bukkit.getPluginManager().addPermission(perm);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerLeave(PlayerQuitEvent event) {
        // unhide them when they leave, just to be sure if this breaks
        final User user = (User) essentials.getUser(event.getPlayer());
        if (!user.isVanished()) user.setHidden(false);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerAFK(AfkStatusChangeEvent event) {
        final User user = (User) event.getAffected();
        // only do this when the player goes afk and is not muted & is not hidden
        if (!event.getValue() || user.isMuted() || user.isHidden()) return;
        for (var permissionMessage : permissionMessages) {
            // check if the player has the permission for this afk message beacuse we will hide the player to hide the initial afk messages
            if (!event.getAffected().getBase().hasPermission(permissionMessage.permission)) continue;
            // get the user away self message
            final String selfmsg = tl("userIsAwaySelf", user.getDisplayName());
            // hide the player so he doesnt send the essentials afk messages
            user.setHidden(true);
            Bukkit.getScheduler().runTask(plugin, () -> {
                // unhide the player the next tick
                user.setHidden(false);
                // send self msg if its not empty
                if (!selfmsg.isEmpty()) {
                    user.sendMessage(selfmsg);
                }
                // send the custom afk message
                essentials.broadcastMessage(user, permissionMessage.message.replace("{0}", user.getDisplayName()), u -> u == user);
                // also while we are at it set the afk message
                if (!permissionMessage.msgResponse.isBlank())
                    event.getAffected().setAfkMessage(permissionMessage.msgResponse);
            });
            return;
        }
    }

    private record AFKMessageRecord(Permission permission, String message, String msgResponse) {
    }
}
