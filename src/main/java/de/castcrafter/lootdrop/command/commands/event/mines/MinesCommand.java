package de.castcrafter.lootdrop.command.commands.event.mines;

import de.castcrafter.lootdrop.command.commands.event.mines.meteor.MeteorCommand;
import de.castcrafter.lootdrop.command.commands.event.mines.seamine.SeamineCommand;
import dev.jorel.commandapi.CommandAPICommand;

/**
 * The type Mines command.
 */
public class MinesCommand extends CommandAPICommand {

  /**
   * Instantiates a new Mines command.
   *
   * @param commandName the command name
   */
  public MinesCommand(String commandName) {
    super(commandName);

    withPermission("lootdrop.command.mines");

    withSubcommand(new SeamineCommand("seamine"));
    withSubcommand(new MeteorCommand("meteor"));
  }
}
