package de.castcrafter.lootdrop.command.commands.event.timer;

import de.castcrafter.lootdrop.command.commands.event.timer.subcommands.TimerCancelCommand;
import de.castcrafter.lootdrop.command.commands.event.timer.subcommands.TimerCreateCommand;
import de.castcrafter.lootdrop.command.commands.event.timer.subcommands.TimerEndCommand;
import de.castcrafter.lootdrop.command.commands.event.timer.subcommands.TimerStartCommand;
import dev.jorel.commandapi.CommandAPICommand;

/**
 * The type Timer command.
 */
public class TimerCommand extends CommandAPICommand {

  /**
   * Instantiates a new Timer command.
   *
   * @param commandName the command name
   */
  public TimerCommand(String commandName) {
    super(commandName);

    withPermission("lootdrop.command.timer");

    withSubcommand(new TimerStartCommand("start"));
    withSubcommand(new TimerEndCommand("end"));
    withSubcommand(new TimerCreateCommand("create"));
    withSubcommand(new TimerCancelCommand("cancel"));
  }
}
