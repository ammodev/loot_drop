package de.castcrafter.lootdrop.meteor;

import de.castcrafter.lootdrop.Main;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.persistence.PersistentDataType;

/**
 * The type Meteor registry.
 */
public class MeteorRegistry implements Listener {

  public static final MeteorRegistry INSTANCE = new MeteorRegistry();
  private final List<Meteor> meteors;

  /**
   * Instantiates a new Meteor registry.
   */
  public MeteorRegistry() {
    Bukkit.getPluginManager().registerEvents(this, Main.getInstance());

    this.meteors = new ArrayList<>();
  }

  /**
   * Fetch meteors from world.
   *
   * @param location the location
   */
  public void fetchMeteorsFromSurrounding(Location location) {
    for (Entity entity : location.getNearbyEntitiesByType(ArmorStand.class, 50)) {
      if (!(entity instanceof ArmorStand armorStand)) {
        continue;
      }

      if (!armorStand.getPersistentDataContainer()
          .has(Meteor.METEOR_KEY, PersistentDataType.BOOLEAN)) {
        continue;
      }

      Meteor meteor = getByArmorStand(armorStand);

      if (meteor == null) {
        meteor = new Meteor(armorStand);
        meteor.register();
      }
    }
  }

  /**
   * Gets by armor stand.
   *
   * @param armorStand the armor stand
   * @return the by armor stand
   */
  public Meteor getByArmorStand(ArmorStand armorStand) {
    return meteors.stream()
        .filter(meteor -> meteor.getMeteorHolder().equals(armorStand))
        .findFirst()
        .orElse(null);
  }

  /**
   * Add meteor.
   *
   * @param meteor the meteor
   */
  public void addMeteor(Meteor meteor) {
    meteors.add(meteor);
  }

  /**
   * Remove meteor.
   *
   * @param meteor the meteor
   */
  public void removeMeteor(Meteor meteor) {
    meteors.remove(meteor);
  }

  /**
   * On player move.
   *
   * @param event the event
   */
  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    if (!event.hasChangedBlock()) {
      return;
    }

    fetchMeteorsFromSurrounding(event.getPlayer().getLocation());
  }
}
