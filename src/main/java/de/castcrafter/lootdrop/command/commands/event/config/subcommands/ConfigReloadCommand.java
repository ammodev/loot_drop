package de.castcrafter.lootdrop.command.commands.event.config.subcommands;

import de.castcrafter.lootdrop.config.LootDropConfig;
import de.castcrafter.lootdrop.utils.Chat;
import dev.jorel.commandapi.CommandAPICommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * The type Loot drop reload config command.
 */
public class ConfigReloadCommand extends CommandAPICommand {

  /**
   * Instantiates a new Loot drop reload config command.
   *
   * @param commandName the command name
   */
  public ConfigReloadCommand(String commandName) {
    super(commandName);

    withPermission("lootdrop.command.config.reload");

    withSubcommand(
        new CommandAPICommand("confirm").withPermission("lootdrop.command.config.reload.confirm")
            .executesPlayer((player, args) -> {
              LootDropConfig config = LootDropConfig.INSTANCE;
              config.loadConfig();

              Chat.sendMessage(
                  player, Component.text("Config reloaded!",
                      NamedTextColor.GREEN
                  ));
            }));

    executesPlayer((player, args) -> {
      Chat.sendMessage(player, Component.text(
          "Bist du sicher, dass du die Config neu laden möchtest? Dies " +
              "setzt ebenso alle aktuellen Fortschritte der Spieler zurück. Um " +
              "diese zu speichern, führe zuerst /save-all aus!",
          NamedTextColor.RED
      ));
      Chat.sendMessage(player, Component.text(
          "Nutze /config reload confirm, um die Config neu zu laden!",
          NamedTextColor.RED
      ));
    });
  }
}
