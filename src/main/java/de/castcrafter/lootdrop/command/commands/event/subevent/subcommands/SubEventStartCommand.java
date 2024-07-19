package de.castcrafter.lootdrop.command.commands.event.subevent.subcommands;

import de.castcrafter.lootdrop.Messages;
import de.castcrafter.lootdrop.command.commands.event.subevent.SubEventCommand;
import de.castcrafter.lootdrop.utils.Chat;
import de.castcrafter.lootdrop.utils.SoundUtils;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * The type Event start sub command.
 */
public class SubEventStartCommand extends CommandAPICommand {

  /**
   * Instantiates a new Event start sub command.
   *
   * @param commandName  the command name
   * @param eventCommand the event command
   */
  public SubEventStartCommand(String commandName, final SubEventCommand eventCommand) {
    super(commandName);

    withPermission("lootdrop.command.subevent.start");

    executes((sender, args) -> {
      if (eventCommand.getEventLocation() == null) {
        Chat.sendMessage(sender, Messages.noEventCreatedComponent());
        return;
      }

      eventCommand.setEventStarted(true);

      Bukkit.broadcast(Chat.prefix().append(Messages.eventHasBeenStartedComponent()));

      for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
        SoundUtils.playSound(onlinePlayer, Sound.ENTITY_ENDER_DRAGON_GROWL, .5f, .75f);
      }
    });
  }
}
