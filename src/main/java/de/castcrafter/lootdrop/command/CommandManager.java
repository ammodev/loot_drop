package de.castcrafter.lootdrop.command;

import de.castcrafter.lootdrop.command.commands.MakeSpecialCommand;
import de.castcrafter.lootdrop.command.commands.drops.DropsCommand;
import de.castcrafter.lootdrop.command.commands.event.EventCommand;
import de.castcrafter.lootdrop.command.commands.event.subevent.SubEventCommand;
import de.castcrafter.lootdrop.command.commands.event.subevent.subcommands.SubEventJoinCommand;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Command manager.
 */
public class CommandManager {

  private final List<CommandAPICommand> commands;

  /**
   * Instantiates a new Command manager.
   */
  public CommandManager() {
    this.commands = new ArrayList<>();

    this.commands.add(new MakeSpecialCommand("makespecial"));
    this.commands.add(new DropsCommand("drops"));

    this.commands.add(new EventCommand("event"));
    this.commands.add(new SubEventJoinCommand("join", SubEventCommand.INSTANCE));
  }

  /**
   * Register commands.
   */
  public void registerCommands() {
    commands.forEach(CommandAPICommand::register);
  }

  /**
   * Unregister commands.
   */
  public void unregisterCommands() {
    commands.stream().map(CommandAPICommand::getName).forEach(CommandAPI::unregister);
  }

  /**
   * Gets commands.
   *
   * @return the commands
   */
  public List<CommandAPICommand> getCommands() {
    return commands;
  }
}
