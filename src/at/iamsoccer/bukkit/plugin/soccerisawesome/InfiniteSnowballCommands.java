package at.iamsoccer.bukkit.plugin.soccerisawesome;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Snowman;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;


public class InfiniteSnowballCommands extends BaseCommand {
    private final SoccerIsAwesomePlugin plugin;

    public InfiniteSnowballCommands(SoccerIsAwesomePlugin plugin) {
        this.plugin = plugin;
    }

    public static final NamespacedKey magicSnowball = new NamespacedKey((Plugin)SoccerIsAwesomePlugin.getPlugin(SoccerIsAwesomePlugin.class), "isMagicSnowball");

    @CommandAlias("infinitesnowball")
    @CommandPermission("infinitesnowball.give")
    @Description("Gives an infinite snowball to the user.")
    public void infiniteSnowballGive(CommandSender sender, String[] args) {
      if (args.length < 2){
        Player target = null;
          if (args.length == 0) {
              if (!(sender instanceof Player)) {
                  sender.sendMessage("From console, include a player name.");
                  return;
              }
              target = (Player)sender;
          } else {
              target = ((SoccerIsAwesomePlugin)SoccerIsAwesomePlugin.getPlugin(SoccerIsAwesomePlugin.class)).getServer().getPlayer(args[0]);
          }
          if (target == null) {
              sender.sendMessage("Player could not be found.");
              return;
          }
          ItemStack i = new ItemStack(Material.SNOWBALL, 1);
          i.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
          ItemMeta m = i.getItemMeta();
          m.setDisplayName(ChatColor.AQUA + "Magic Snowball");
          m.getPersistentDataContainer().set(magicSnowball, PersistentDataType.INTEGER, ThreadLocalRandom.current().nextInt(1073741823));
          i.setItemMeta(m);
          target.getInventory().addItem(new ItemStack[] { i });
        } else {
          sender.sendMessage("Too many/too few arguments.");
        }
    }

    @CommandAlias("snowman")
    @CommandPermission("snowmancannon.shoot")
    @Description("Launches a fabulous snowman towards the cursor.")
    public void snowmanCannon(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("User command only");
            return;
        }
        Player p = (Player)sender;
        final Snowman snowman = (Snowman)p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.SNOWMAN);
        snowman.setDerp((ThreadLocalRandom.current().nextInt(16) == 7));
        snowman.setVelocity(p.getEyeLocation().getDirection().multiply(3));
        JavaPlugin sc = JavaPlugin.getPlugin(SoccerIsAwesomePlugin.class);
        final BukkitTask smp = (new BukkitRunnable() {
            Vector vec = snowman.getEyeLocation().getDirection().setY(0);

            int i = 0;

            public void run() {
                snowman.getWorld().spawnParticle(Particle.SNOW_SHOVEL, snowman.getLocation(), 8, 1.0D, 1.0D, 1.0D, 0.2D);
                if (++this.i > 15) {
                    Snowball b = (Snowball)snowman.getWorld().spawnEntity(snowman.getEyeLocation(), EntityType.SNOWBALL);
                    b.setVelocity(this.vec);
                    this.vec.rotateAroundY(12.0D);
                }
                if (this.i > 38) {
                    Location l = snowman.getLocation();
                    l.getWorld().spawnParticle(Particle.CLOUD, l, 30, 1.0D, 2.0D, 1.0D, 0.1D);
                    l.getWorld().spawnParticle(Particle.SNOW_SHOVEL, l, 30, 1.0D, 2.0D, 1.0D, 0.05D);
                    snowman.remove();
                    cancel();
                }
            }
        }).runTaskTimer((Plugin)sc, 0L, 1L);
        BukkitTask sme = (new BukkitRunnable() {
            public void run() {
                Location l = snowman.getLocation();
                l.getWorld().spawnParticle(Particle.CLOUD, l, 30, 1.0D, 2.0D, 1.0D, 0.1D);
                l.getWorld().spawnParticle(Particle.SNOW_SHOVEL, l, 30, 1.0D, 2.0D, 1.0D, 0.05D);
                snowman.remove();
                smp.cancel();
            }
        }).runTaskLater((Plugin)sc, 36L);
    }
}

