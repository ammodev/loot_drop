package de.castcrafter.lootdrop.command.commands.event.config;

import de.castcrafter.lootdrop.command.commands.event.config.subcommands.ConfigReloadCommand;
import dev.jorel.commandapi.CommandAPICommand;

/**
 * The type Config command.
 */
public class ConfigCommand extends CommandAPICommand {

  /**
   * Instantiates a new Config command.
   *
   * @param commandName the command name
   */
  public ConfigCommand(String commandName) {
    super(commandName);

    withPermission("lootdrop.command.config");

    withSubcommand(new ConfigReloadCommand("reload"));
  }
}
