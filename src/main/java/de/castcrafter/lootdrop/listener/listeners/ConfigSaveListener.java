package de.castcrafter.lootdrop.listener.listeners;

import de.castcrafter.lootdrop.config.LootDropConfig;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;

/**
 * The type Config save listener.
 */
public class ConfigSaveListener implements Listener {

  /**
   * On world save.
   *
   * @param event the event
   */
  @EventHandler
  public void onWorldSave(WorldSaveEvent event) {
    if (event.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
      LootDropConfig.INSTANCE.saveConfig();
    }
  }
}
