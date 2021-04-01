package at.hugo.bukkit.plugin.damagenullifieronteleportorjoin;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.kyori.adventure.bossbar.BossBar;

public class BossBarTimer extends BukkitRunnable {
    private final BossBar bossBar;
    private final Player player;
    private final long timeSteps;
    private final long maxTime;
    private long remainingTime;

    /**
     * @param player
     * @param maxTime
     * @param remainingTime
     */
    public BossBarTimer(final Player player, final long ticks, final long timeSteps, final BossBar bossBar) {
        this.timeSteps = timeSteps;
        this.bossBar = bossBar;
        this.player = player;
        this.maxTime = ticks;
        this.remainingTime = maxTime;
        player.showBossBar(bossBar);
        this.runTaskTimer(JavaPlugin.getPlugin(DamageNullifierOnTeleportOrJoinPlugin.class), timeSteps, timeSteps);
    }

    @Override
    public void run() {
        remainingTime -= timeSteps;
        if (remainingTime > 0) {
            bossBar.progress(remainingTime / (float) maxTime);
        } else {
            cancel();
        }
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        player.hideBossBar(bossBar);
        super.cancel();
    }
}
