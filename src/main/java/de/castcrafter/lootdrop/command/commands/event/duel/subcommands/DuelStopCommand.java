package de.castcrafter.lootdrop.command.commands.event.duel.subcommands;

import de.castcrafter.lootdrop.duel.DuelFinishState;
import de.castcrafter.lootdrop.duel.DuelManager;
import de.castcrafter.lootdrop.utils.Chat;
import dev.jorel.commandapi.CommandAPICommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * The type Duel stop command.
 */
public class DuelStopCommand extends CommandAPICommand {

  /**
   * Instantiates a new Duel stop command.
   *
   * @param commandName the command name
   */
  public DuelStopCommand(String commandName) {
    super(commandName);

    withPermission("lootdrop.command.duel.stop");

    executesPlayer((player, args) -> {
      if (DuelManager.INSTANCE.getRunningDuel() == null) {
        Chat.sendMessage(player, Component.text(
            "Aktuell läuft kein Duell, welches du beenden könntest!", NamedTextColor.RED
        ));

        return;
      }

      DuelManager.INSTANCE.getRunningDuel().finishDuel(DuelFinishState.FORCE_STOP);
    });
  }
}
