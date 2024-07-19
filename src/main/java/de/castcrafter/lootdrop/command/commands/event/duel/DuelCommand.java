package de.castcrafter.lootdrop.command.commands.event.duel;

import de.castcrafter.lootdrop.command.commands.event.duel.subcommands.DuelCreateCommand;
import de.castcrafter.lootdrop.command.commands.event.duel.subcommands.DuelResultCommand;
import de.castcrafter.lootdrop.command.commands.event.duel.subcommands.DuelStopCommand;
import de.castcrafter.lootdrop.command.commands.event.duel.subcommands.DuelVoteCommand;
import dev.jorel.commandapi.CommandAPICommand;

/**
 * The type Duel command.
 */
public class DuelCommand extends CommandAPICommand {

  /**
   * Instantiates a new Duel command.
   *
   * @param commandName the command name
   */
  public DuelCommand(String commandName) {
    super(commandName);

    withPermission("lootdrop.command.duel");

    withSubcommand(new DuelCreateCommand("create"));
    withSubcommand(new DuelStopCommand("force-stop"));
    withSubcommand(new DuelResultCommand("result"));
    withSubcommand(new DuelVoteCommand("vote"));
  }
}
