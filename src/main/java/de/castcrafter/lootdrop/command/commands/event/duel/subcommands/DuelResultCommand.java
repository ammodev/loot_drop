package de.castcrafter.lootdrop.command.commands.event.duel.subcommands;

import de.castcrafter.lootdrop.duel.Duel;
import de.castcrafter.lootdrop.duel.DuelFinishState;
import de.castcrafter.lootdrop.duel.DuelManager;
import de.castcrafter.lootdrop.utils.Chat;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

/**
 * The type Duel result command.
 */
public class DuelResultCommand extends CommandAPICommand {

  /**
   * Instantiates a new Duel result command.
   *
   * @param commandName the command name
   */
  public DuelResultCommand(String commandName) {
    super(commandName);

    withPermission("lootdrop.command.duel.result");

    withArguments(
        new StringArgument("winningPlayer").replaceSuggestions(
            ArgumentSuggestions.stringCollection(info -> {
              List<String> suggestions = new ArrayList<>();
              suggestions.add("none");
              suggestions.add("draw");

              Duel duel = DuelManager.INSTANCE.getRunningDuel();

              if (duel == null) {
                return suggestions;
              }

              suggestions.add(duel.getPlayerOne().getName());
              suggestions.add(duel.getPlayerTwo().getName());

              return suggestions;
            })));

    executesPlayer((player, args) -> {
      Duel duel = DuelManager.INSTANCE.getRunningDuel();

      if (duel == null) {
        Chat.sendMessage(player, Component.text(
            "Es läuft aktuell kein Duell, für das du abstimmen kannst!",
            NamedTextColor.RED
        ));

        return;
      }

      if (duel.getVoteTimer() != null && duel.getVoteTimer().isRunning()) {
        Chat.sendMessage(player, Component.text(
            "Die Abstimmung läuft noch. Bitte warte, bis die Abstimmung beendet ist!",
            NamedTextColor.RED
        ));

        return;
      }

      Player playerOne = duel.getPlayerOne();
      Player playerTwo = duel.getPlayerTwo();

      final String playerOneName = playerOne.getName();
      String playerTwoName = playerTwo.getName();

      String finishStateString = args.getOrDefaultUnchecked("winningPlayer", "");
      DuelFinishState state;

      if (finishStateString.equalsIgnoreCase("none")) {
        state = DuelFinishState.NOBODY_WON;
      } else if (finishStateString.equalsIgnoreCase("draw")) {
        state = DuelFinishState.DRAW;
      } else if (finishStateString.equalsIgnoreCase(playerOneName)) {
        state = DuelFinishState.PLAYER_ONE_WON;
      } else if (finishStateString.equalsIgnoreCase(playerTwoName)) {
        state = DuelFinishState.PLAYER_TWO_WON;
      } else {
        Chat.sendMessage(player, Component.text(
            "Die Spieler sind " + playerOneName + " und " + playerTwoName, NamedTextColor.RED
        ));

        return;
      }

      DuelManager.INSTANCE.getRunningDuel().finishDuel(state);
    });
  }
}
