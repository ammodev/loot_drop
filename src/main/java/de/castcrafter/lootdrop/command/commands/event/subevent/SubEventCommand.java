package de.castcrafter.lootdrop.command.commands.event.subevent;

import de.castcrafter.lootdrop.command.commands.event.subevent.subcommands.SubEventCreateCommand;
import de.castcrafter.lootdrop.command.commands.event.subevent.subcommands.SubEventJoinCommand;
import de.castcrafter.lootdrop.command.commands.event.subevent.subcommands.SubEventStartCommand;
import de.castcrafter.lootdrop.command.commands.event.subevent.subcommands.SubEventStopCommand;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.Location;

/**
 * The type Sub event command.
 */
public class SubEventCommand extends CommandAPICommand {

  public static final SubEventCommand INSTANCE = new SubEventCommand("subevent");

  private boolean eventStarted;
  private Location eventLocation;

  /**
   * Instantiates a new Sub event command.
   *
   * @param commandName the command name
   */
  public SubEventCommand(String commandName) {
    super(commandName);

    withPermission("lootdrop.command.subevent");

    withSubcommand(new SubEventStartCommand("start", this));
    withSubcommand(new SubEventStopCommand("stop", this));
    withSubcommand(new SubEventCreateCommand("create", this));
    withSubcommand(new SubEventJoinCommand("join", this));
  }

  /**
   * Gets event location.
   *
   * @return the event location
   */
  public Location getEventLocation() {
    return eventLocation;
  }

  /**
   * Is event started boolean.
   *
   * @return the boolean
   */
  public boolean isEventStarted() {
    return eventStarted;
  }

  /**
   * Sets event location.
   *
   * @param eventLocation the event location
   */
  public void setEventLocation(Location eventLocation) {
    this.eventLocation = eventLocation;
  }

  /**
   * Sets event started.
   *
   * @param eventStarted the event started
   */
  public void setEventStarted(boolean eventStarted) {
    this.eventStarted = eventStarted;
  }
}
