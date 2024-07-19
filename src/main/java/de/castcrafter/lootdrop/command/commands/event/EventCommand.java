package de.castcrafter.lootdrop.command.commands.event;

import de.castcrafter.lootdrop.command.commands.event.config.ConfigCommand;
import de.castcrafter.lootdrop.command.commands.event.duel.DuelCommand;
import de.castcrafter.lootdrop.command.commands.event.mines.MinesCommand;
import de.castcrafter.lootdrop.command.commands.event.subevent.SubEventCommand;
import de.castcrafter.lootdrop.command.commands.event.timer.TimerCommand;
import dev.jorel.commandapi.CommandAPICommand;

/**
 * The type Event command.
 */
public class EventCommand extends CommandAPICommand {

  /**
   * Instantiates a new Event command.
   *
   * @param commandName the command name
   */
  public EventCommand(String commandName) {
    super(commandName);

    withPermission("castcrafter.event");

    withSubcommand(new TimerCommand("timer"));
    withSubcommand(new MinesCommand("mines"));
    withSubcommand(new SubEventCommand("sub-event"));
    withSubcommand(new DuelCommand("duel"));
    withSubcommand(new ConfigCommand("config"));
  }
}
