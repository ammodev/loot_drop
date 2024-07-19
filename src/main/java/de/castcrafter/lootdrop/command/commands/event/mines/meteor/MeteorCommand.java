package de.castcrafter.lootdrop.command.commands.event.mines.meteor;

import de.castcrafter.lootdrop.meteor.Meteor;
import de.castcrafter.lootdrop.utils.Chat;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * The type Meteor command.
 */
public class MeteorCommand extends CommandAPICommand {

  /**
   * Instantiates a new Meteor command.
   *
   * @param commandName the command name
   */
  public MeteorCommand(String commandName) {
    super(commandName);

    withPermission("lootdrop.command.mines.meteor");

    withOptionalArguments(new BooleanArgument("swirl"));

    executesPlayer((player, args) -> {
      boolean swirl = args.getOrDefaultUnchecked("swirl", false);

      Meteor meteor = new Meteor(player.getLocation(), swirl, false);
      meteor.spawnHolder();
      meteor.register();

      Chat.sendMessage(player, Component.text("Meteor spawned!", NamedTextColor.GREEN));
    });
  }
}
