package de.castcrafter.lootdrop.command.commands.event.subevent.subcommands;

import de.castcrafter.lootdrop.Messages;
import de.castcrafter.lootdrop.command.commands.event.subevent.SubEventCommand;
import de.castcrafter.lootdrop.utils.Chat;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.Bukkit;

/**
 * The type Event start sub command.
 */
public class SubEventStopCommand extends CommandAPICommand {

  /**
   * Instantiates a new Event start sub command.
   *
   * @param commandName  the command name
   * @param eventCommand the event command
   */
  public SubEventStopCommand(String commandName, final SubEventCommand eventCommand) {
    super(commandName);

    withPermission("lootdrop.command.subevent.stop");

    executes((sender, args) -> {
      if (!eventCommand.isEventStarted()) {
        Chat.sendMessage(sender, Messages.noEventStartedComponent());
        return;
      }

      eventCommand.setEventStarted(false);
      eventCommand.setEventLocation(null);

      Bukkit.broadcast(Chat.prefix().append(Messages.eventHasBeenStoppedComponent()));
    });
  }
}
