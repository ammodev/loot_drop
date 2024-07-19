package de.castcrafter.lootdrop.command.commands.event.subevent.subcommands;

import de.castcrafter.lootdrop.Messages;
import de.castcrafter.lootdrop.command.commands.event.subevent.SubEventCommand;
import de.castcrafter.lootdrop.utils.Chat;
import dev.jorel.commandapi.CommandAPICommand;

/**
 * The type Event create sub command.
 */
public class SubEventCreateCommand extends CommandAPICommand {

  /**
   * Instantiates a new Event create sub command.
   *
   * @param commandName  the command name
   * @param eventCommand the event command
   */
  public SubEventCreateCommand(String commandName, final SubEventCommand eventCommand) {
    super(commandName);

    withPermission("lootdrop.command.subevent.create");

    executesPlayer((player, args) -> {
      eventCommand.setEventLocation(player.getLocation());

      Chat.sendMessage(player, Messages.eventHasBeenCreatedComponent());
    });
  }
}
