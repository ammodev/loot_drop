package de.castcrafter.lootdrop.config;

import de.castcrafter.lootdrop.BukkitMain;
import de.castcrafter.lootdrop.config.drops.HourlyDrop;
import de.castcrafter.lootdrop.timer.LootDropTimer;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

/**
 * The type Loot drop config.
 */
@ConfigSerializable
public class LootDropConfig {

  private static final ComponentLogger LOGGER = ComponentLogger.logger(LootDropConfig.class);
  public static final LootDropConfig INSTANCE = new LootDropConfig();

  private transient YamlConfigurationLoader loader;
  private transient CommentedConfigurationNode node;

  private long startedTimestampSeconds;
  private long endTimestampSeconds;
  private transient LootDropTimer timer;

  private List<HourlyDrop> drops = new ArrayList<>();

  /**
   * Instantiates a new Loot drop config.
   */
  public LootDropConfig() {
  }

  /**
   * Load and start timer if exists.
   */
  public void loadAndStartTimerIfExistsInConfig() {
    if (timer == null) {
      setTimer(new LootDropTimer(
          Duration.ofSeconds(
              getEndTimestamp().toEpochSecond() - ZonedDateTime.now().toEpochSecond())));

      getTimer().start();
    }
  }

  /**
   * Load config loot drop config.
   */
  public void loadConfig() {
    loader = YamlConfigurationLoader.builder()
        .path(BukkitMain.getInstance().getDataFolder().toPath().resolve("config.yml"))
        .build();
    try {
      node = loader.load();

      if (node == null) {
        LOGGER.error("Could not load configuration");

        throw new IllegalStateException("Could not load configuration");
      }

      LootDropConfig fetchedConfig = node.get(LootDropConfig.class);

      if (fetchedConfig == null) {
        LOGGER.error("Could not load configuration");

        throw new IllegalStateException("Could not load configuration");
      }

      drops = fetchedConfig.drops;
      startedTimestampSeconds = fetchedConfig.startedTimestampSeconds;
      endTimestampSeconds = fetchedConfig.endTimestampSeconds;
    } catch (Exception exception) {
      LOGGER.error("Failed to load configuration", exception);
    }
  }

  /**
   * Save config.
   */
  public void saveConfig() {
    try {
      node.set(this);
      loader.save(node);
    } catch (Exception exception) {
      LOGGER.error("Failed to save configuration", exception);
    }
  }

  /**
   * Gets drops.
   *
   * @return the drops
   */
  public List<HourlyDrop> getDrops() {
    return drops;
  }

  /**
   * Gets drop.
   *
   * @param hour the hour
   * @return the drop
   */
  public HourlyDrop getDrop(int hour) {
    return drops.stream().filter(drop -> drop.getHour() == hour).findFirst().orElse(null);
  }

  /**
   * Gets started timestamp.
   *
   * @return the started timestamp
   */
  public ZonedDateTime getStartedTimestamp() {
    return ZonedDateTime.ofInstant(Instant.ofEpochSecond(startedTimestampSeconds),
        ZoneId.systemDefault());
  }

  /**
   * Sets started timestamp.
   *
   * @param startedTimestamp the started timestamp
   */
  public void setStartTimestamp(ZonedDateTime startedTimestamp) {
    this.startedTimestampSeconds = startedTimestamp.toEpochSecond();
  }

  /**
   * Sets end timestamp.
   *
   * @param endTimestamp the end timestamp
   */
  public void setEndTimestamp(ZonedDateTime endTimestamp) {
    this.endTimestampSeconds = endTimestamp.toEpochSecond();
  }

  /**
   * Gets end timestamp.
   *
   * @return the end timestamp
   */
  public ZonedDateTime getEndTimestamp() {
    return ZonedDateTime.ofInstant(Instant.ofEpochSecond(endTimestampSeconds),
        ZoneId.systemDefault());
  }

  /**
   * Gets timer.
   *
   * @return the timer
   */
  public LootDropTimer getTimer() {
    return timer;
  }

  /**
   * Sets timer.
   *
   * @param timer the timer
   */
  public void setTimer(LootDropTimer timer) {
    this.timer = timer;
  }
}
