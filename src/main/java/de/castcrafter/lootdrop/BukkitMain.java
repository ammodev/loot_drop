package de.castcrafter.lootdrop;

import de.castcrafter.lootdrop.command.CommandManager;
import de.castcrafter.lootdrop.config.LootDropConfig;
import de.castcrafter.lootdrop.listener.ListenerManager;
import de.castcrafter.lootdrop.placeholder.LootDropPlaceholderExpansion;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The type BukkitMain.
 */
public class BukkitMain extends JavaPlugin {

  private CommandManager commandManager;
  private ListenerManager listenerManager;

  /**
   * Gets instance.
   *
   * @return the instance
   */
  public static BukkitMain getInstance() {
    return getPlugin(BukkitMain.class);
  }

  @Override
  public void onLoad() {
    LootDropConfig.INSTANCE.loadConfig();

    commandManager = new CommandManager();
    listenerManager = new ListenerManager();
  }

  @Override
  public void onEnable() {
    commandManager.registerCommands();
    listenerManager.registerListeners();

    LootDrop.INSTANCE.onEnable();

    LootDropConfig.INSTANCE.loadAndStartTimerIfExistsInConfig();

    new LootDropPlaceholderExpansion().register();
  }

  @Override
  public void onDisable() {
    LootDrop.INSTANCE.onDisable();
    commandManager.unregisterCommands();
    listenerManager.unregisterListeners();
  }
}
