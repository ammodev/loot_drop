package de.castcrafter.lootdrop.command.commands.event.subevent.subcommands;

import de.castcrafter.lootdrop.Messages;
import de.castcrafter.lootdrop.command.commands.event.subevent.SubEventCommand;
import de.castcrafter.lootdrop.utils.Chat;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.Location;

/**
 * The type Event join command.
 */
public class SubEventJoinCommand extends CommandAPICommand {

  /**
   * Instantiates a new Event join command.
   *
   * @param commandName  the command name
   * @param eventCommand the event command
   */
  public SubEventJoinCommand(String commandName, SubEventCommand eventCommand) {
    super(commandName);

    withPermission("lootdrop.command.subevent.join");

    executesPlayer((player, args) -> {
      Location eventLocation = eventCommand.getEventLocation();
      boolean eventStarted = eventCommand.isEventStarted();

      if (eventLocation == null) {
        Chat.sendMessage(player, Messages.noEventStartedComponent());
        return;
      }

      if (!eventStarted) {
        Chat.sendMessage(player, Messages.noEventStartedComponent());
        return;
      }

      player.teleportAsync(eventLocation).thenAcceptAsync(v -> {
        Chat.sendMessage(player, Messages.youHaveBeenTeleportedToEventLocationComponent());
      });
    });
  }
}
