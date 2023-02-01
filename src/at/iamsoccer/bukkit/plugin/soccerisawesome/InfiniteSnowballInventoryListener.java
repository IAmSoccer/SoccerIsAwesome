package at.iamsoccer.bukkit.plugin.soccerisawesome;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class InfiniteSnowballInventoryListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInvClick(InventoryClickEvent e) {

        ItemStack i = e.getCurrentItem();

        if (i == null || i.getItemMeta() == null) {
            return;
        }

        if (i.getItemMeta().getPersistentDataContainer().has(InfiniteSnowballCommands.magicSnowball, PersistentDataType.INTEGER)) {

            InventoryType it = e.getInventory().getType();
            if (it.equals(InventoryType.ANVIL) || it.equals(InventoryType.DISPENSER) || it.equals(InventoryType.GRINDSTONE)) {

                e.getWhoClicked().sendMessage("Gonna stop you right there chief.");
                e.getWhoClicked().sendMessage("You don't wanna lose that sucker by mistake.");

                e.setCancelled(true);
            }
        }

    }

}