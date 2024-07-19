package de.castcrafter.lootdrop.listener.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * The type Seamine listener.
 */
public class SeamineListener implements Listener {

  /**
   * On player approach mine.
   *
   * @param event the event
   */
  @EventHandler
  public void onPlayerApproachMine(PlayerMoveEvent event) {
    if (!event.hasChangedBlock()) {
      return;
    }

    Player player = event.getPlayer();
    if (player.getGameMode() == GameMode.SURVIVAL) {
      double range = 5.0; // Set the range

      for (Entity entity : player.getNearbyEntities(range, range, range)) {
        if (entity instanceof ArmorStand armorStand) {
          if (armorStand.getScoreboardTags().contains("seamine")) {
            armorStand.getWorld().spawn(armorStand.getLocation(), TNTPrimed.class).setFuseTicks(1);
            armorStand.getNearbyEntities(5, 5, 5).forEach(nearby -> {
              if (!(nearby instanceof Player nearbyPlayer)) {
                return;
              }

              nearbyPlayer.damage(8);
            });

            armorStand.remove();

            break;
          }
        }
      }
    }
  }
}