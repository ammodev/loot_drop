package de.castcrafter.lootdrop.command.commands.drops;

import de.castcrafter.lootdrop.command.commands.drops.subcommands.DropResetCommand;
import de.castcrafter.lootdrop.gui.drops.DropsGui;
import dev.jorel.commandapi.CommandAPICommand;

/**
 * The type Drops command.
 */
public class DropsCommand extends CommandAPICommand {

  /**
   * Instantiates a new Drops command.
   *
   * @param commandName the command name
   */
  public DropsCommand(String commandName) {
    super(commandName);

    withPermission("lootdrop.command.drops");

    withSubcommand(new DropResetCommand("reset"));

    executesPlayer((player, args) -> {
      new DropsGui(player).show(player);
    });
  }
}
