package at.iamsoccer.bukkit.plugin.soccerisawesome;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class InfiniteSnowballInteractListener implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    public void onInvChange(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Action a = e.getAction();
        ItemStack i = p.getInventory().getItemInMainHand();
        ItemStack ioff = p.getInventory().getItemInOffHand();
        if (i.getType().equals(Material.SNOWBALL)) {
            try {
                boolean isMagic = i.getItemMeta().getPersistentDataContainer().has(InfiniteSnowballCommands.magicSnowball, PersistentDataType.INTEGER);
                if (isMagic) {
                    if (i.getAmount() != 1)
                        i.setAmount(1);
                    if (a.equals(Action.RIGHT_CLICK_AIR) || a.equals(Action.RIGHT_CLICK_BLOCK))
                        if (p.hasPermission("infinitesnowball.use")) {
                            e.setCancelled(true);
                            p.launchProjectile(Snowball.class);
                        } else {
                            p.sendMessage("You aren't allowed to use the infinite snowball.");
                            e.setCancelled(true);
                        }
                }
            } catch (NullPointerException nullPointerException) {}
        } else if (ioff.getType().equals(Material.SNOWBALL)) {
            boolean isMagic = ioff.getItemMeta().getPersistentDataContainer().has(InfiniteSnowballCommands.magicSnowball, PersistentDataType.INTEGER);
            if (isMagic) {
                if (ioff.getAmount() != 1)
                    ioff.setAmount(1);
                e.setCancelled(true);
            }
        }
    }
}