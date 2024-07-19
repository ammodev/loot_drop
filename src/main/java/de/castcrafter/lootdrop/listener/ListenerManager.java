package de.castcrafter.lootdrop.listener;

import de.castcrafter.lootdrop.BukkitMain;
import de.castcrafter.lootdrop.listener.listeners.ConfigSaveListener;
import de.castcrafter.lootdrop.listener.listeners.SeamineListener;
import de.castcrafter.lootdrop.listener.listeners.SpecialItemListener;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The type Listener manager.
 */
public class ListenerManager {

  /**
   * Register listeners.
   */
  public void registerListeners() {
    JavaPlugin plugin = BukkitMain.getInstance();
    PluginManager pluginManager = Bukkit.getPluginManager();

    pluginManager.registerEvents(new SeamineListener(), plugin);
    pluginManager.registerEvents(new SpecialItemListener(), plugin);
    pluginManager.registerEvents(new ConfigSaveListener(), plugin);
  }

  /**
   * Unregister listeners.
   */
  public void unregisterListeners() {
    HandlerList.unregisterAll(BukkitMain.getInstance());
  }

}
