package at.iamsoccer.soccerisawesome.sheepcolorchanger;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class SheepColorChangerListener implements Listener {
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onSheepInteract(PlayerInteractEntityEvent event) {
        if (!event.getPlayer().hasPermission("sheepcolorchanger.use")) {
            return;
        }
        if (event.getRightClicked() instanceof Sheep && event.getHand() == EquipmentSlot.HAND) {
            Sheep sheep = (Sheep) event.getRightClicked();
            ItemStack itemInHand = event.getPlayer().getInventory().getItemInMainHand();
            if (event.getPlayer().isSneaking() && (itemInHand == null || itemInHand.getType() == Material.AIR)) {
                Random random = new Random();
                sheep.setColor(DyeColor.values()[random.nextInt(DyeColor.values().length)]);
            }
        }
    }
}
