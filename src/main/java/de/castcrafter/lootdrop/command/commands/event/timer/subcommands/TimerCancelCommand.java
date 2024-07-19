package de.castcrafter.lootdrop.command.commands.event.timer.subcommands;

import de.castcrafter.lootdrop.config.LootDropConfig;
import de.castcrafter.lootdrop.timer.LootDropTimer;
import de.castcrafter.lootdrop.utils.Chat;
import dev.jorel.commandapi.CommandAPICommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * The type Loot drop pause timer command.
 */
public class TimerCancelCommand extends CommandAPICommand {

  /**
   * Instantiates a new Loot drop cancel timer command.
   *
   * @param commandName the command name
   */
  public TimerCancelCommand(String commandName) {
    super(commandName);

    withPermission("lootdrop.command.timer.cancel");

    executesPlayer((player, args) -> {
      LootDropTimer timer = LootDropConfig.INSTANCE.getTimer();

      if (timer == null || !timer.isRunning()) {
        Chat.sendMessage(player, Component.text("Es läuft kein Timer!", NamedTextColor.RED));
        return;
      }

      timer.stop();
      Chat.sendMessage(player,
          Component.text("Der Timer wurde abgebrochen!", NamedTextColor.GREEN));
    });
  }
}
