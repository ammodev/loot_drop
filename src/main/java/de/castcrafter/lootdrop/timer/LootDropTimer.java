package de.castcrafter.lootdrop.timer;

import de.castcrafter.lootdrop.Main;
import de.castcrafter.lootdrop.config.LootDropConfig;
import de.castcrafter.lootdrop.utils.Chat;
import de.castcrafter.lootdrop.utils.SoundUtils;
import java.time.Duration;
import java.time.ZonedDateTime;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * The type Loot drop timer.
 */
public class LootDropTimer extends BukkitRunnable {

  private static final ComponentLogger LOGGER = ComponentLogger.logger(LootDropTimer.class);

  private long maxSeconds;
  private long seconds;

  /**
   * Instantiates a new Loot drop timer.
   *
   * @param duration the duration
   */
  public LootDropTimer(Duration duration) {
    setDuration(duration);
  }

  /**
   * Sets duration.
   *
   * @param duration the duration
   */
  public void setDuration(Duration duration) {
    this.maxSeconds = duration.getSeconds();
    this.seconds = this.maxSeconds + 1;
  }

  @Override
  public void run() {
    seconds--;

    ZonedDateTime currentTime = ZonedDateTime.now();
    ZonedDateTime startTime = LootDropConfig.INSTANCE.getStartedTimestamp();

    long secondsSinceStart = Duration.between(startTime, currentTime).getSeconds();

    if (secondsSinceStart % 3600 == 0) {
      long hours = secondsSinceStart / 3600;

      for (Player player : Bukkit.getOnlinePlayers()) {
        Chat.sendMessage(
            player,
            Component.text("Die Stunde ", NamedTextColor.GREEN).append(Component.text(
                hours,
                NamedTextColor.GOLD
            )).append(Component.text(" Trades sind geöffnet!", NamedTextColor.GREEN))
        );

        SoundUtils.playSound(player, Sound.ENTITY_CHICKEN_EGG, 1, 1);
      }
    }

    if (seconds <= 0) {
      this.stop();
    }
  }

  /**
   * Stop.
   */
  public void stop() {
    try {
      if (!this.isCancelled()) {
        this.cancel();
      } else {
        LOGGER.error("Could not stop timer, already cancelled");
      }
    } catch (Exception ignored) {
      LOGGER.error("Could not stop timer, already cancelled");
    }
  }

  /**
   * Start.
   */
  public void start() {
    if (this.isRunning()) {
      LOGGER.error("Could not start timer, already running");
      return;
    }

    this.runTaskTimer(Main.getInstance(), 0, 20L);
  }

  /**
   * Gets seconds.
   *
   * @return the seconds
   */
  public long getSeconds() {
    return seconds;
  }

  /**
   * Gets max seconds.
   *
   * @return the max seconds
   */
  public long getMaxSeconds() {
    return maxSeconds;
  }

  /**
   * Is running boolean.
   *
   * @return the boolean
   */
  public boolean isRunning() {
    try {
      return !this.isCancelled();
    } catch (Exception ignored) {
      return false;
    }
  }
}
