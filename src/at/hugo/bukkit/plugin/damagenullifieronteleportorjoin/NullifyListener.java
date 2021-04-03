package at.hugo.bukkit.plugin.damagenullifieronteleportorjoin;

import java.util.Collection;
import java.util.WeakHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class NullifyListener implements Listener {
    private TextComponent bossBarName = Component.text("Immunity");
    private BossBar.Color bossBarColor = BossBar.Color.RED;
    private BossBar.Overlay bossBarOverlay = BossBar.Overlay.PROGRESS;
    private boolean showBossBar = true;
    private long immunityTime = 30;
    private double minTpDistanceSquared = 1;
    WeakHashMap<Player, PlayerInfo> players = new WeakHashMap<>();

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onJoin(final PlayerJoinEvent event) {
        addPlayer(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onTeleport(final PlayerTeleportEvent event) {
        if (!event.getFrom().getWorld().equals(event.getTo().getWorld())
                || minTpDistanceSquared <= event.getFrom().distanceSquared(event.getTo()))
            addPlayer(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onQuit(final PlayerQuitEvent event) {
        if (players.containsKey(event.getPlayer()))
            removePlayer(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onMove(final PlayerMoveEvent event) {
        if (players.containsKey(event.getPlayer()) && (event.getFrom().getX() != event.getTo().getX()
                || event.getFrom().getY() < event.getTo().getY() || event.getFrom().getZ() != event.getTo().getZ()))
            removePlayer(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onDamageDeal(final EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && players.containsKey(event.getDamager()))
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onDamageRecieve(final EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && players.containsKey(event.getEntity()))
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onHunger(final FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player && players.containsKey(event.getEntity()))
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onRegen(final EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player && players.containsKey(event.getEntity()))
            event.setCancelled(true);
    }

    public void update(final double minTpDistance, final long immunityTime, final boolean showBossBar,
            final String bossBarName, final String bossBarColor, final String bossBarOverlay) {
        this.minTpDistanceSquared = minTpDistance * minTpDistance;
        this.immunityTime = immunityTime;
        this.showBossBar = showBossBar;
        this.bossBarName = LegacyComponentSerializer.legacyAmpersand().deserialize(bossBarName);
        this.bossBarColor = BossBar.Color.valueOf(bossBarColor);
        this.bossBarOverlay = BossBar.Overlay.valueOf(bossBarOverlay);
    }

    private void removePlayer(@NotNull final Player player) {
        if (players.containsKey(player)) {
            final PlayerInfo pi = players.remove(player);
            pi.applyToPlayer(player);
            if (!pi.removalTask.isCancelled())
                pi.removalTask.cancel();
            if (pi.bossBarTask != null && !pi.bossBarTask.isCancelled())
                pi.bossBarTask.cancel();
        }
    }

    private void addPlayer(@NotNull final Player player) {
        removePlayer(player);
        players.put(player, new PlayerInfo(
                Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(DamageNullifierOnTeleportOrJoinPlugin.class),
                        () -> removePlayer(player), immunityTime * 20l),
                showBossBar
                        ? new BossBarTimer(player, immunityTime * 20l, 1l,
                                BossBar.bossBar(bossBarName, 1f, bossBarColor, bossBarOverlay))
                        : null,
                player));

    }

    private class PlayerInfo {
        public final BukkitTask removalTask;
        public final BossBarTimer bossBarTask;
        public final int fireTicks;
        public final int foodLevel;
        public final float saturation;
        public final float exhaustion;
        public final Collection<PotionEffect> potionEffects;

        public PlayerInfo(final BukkitTask removalTask, final BossBarTimer bossBarTask, final Player player) {
            this.removalTask = removalTask;
            this.bossBarTask = bossBarTask;
            this.fireTicks = player.getFireTicks();
            this.foodLevel = player.getFoodLevel();
            this.saturation = player.getSaturation();
            this.exhaustion = player.getExhaustion();
            this.potionEffects = player.getActivePotionEffects();
        }

        public void applyToPlayer(final Player player) {
            player.setFireTicks(fireTicks);
            player.setFoodLevel(foodLevel);
            player.setExhaustion(exhaustion);
            player.setSaturation(saturation);
            player.addPotionEffects(potionEffects);
        }
    }
}
