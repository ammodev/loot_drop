package de.castcrafter.lootdrop.command.commands.event.timer.subcommands;

import de.castcrafter.lootdrop.config.LootDropConfig;
import de.castcrafter.lootdrop.utils.Chat;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * The type Timer start command.
 */
public class TimerStartCommand extends CommandAPICommand {

  /**
   * Instantiates a new Timer start command.
   *
   * @param commandName the command name
   */
  public TimerStartCommand(String commandName) {
    super(commandName);

    withPermission("lootdrop.command.timer.start");

    withArguments(new IntegerArgument("year"));
    withArguments(new IntegerArgument("month"));
    withArguments(new IntegerArgument("day"));
    withArguments(new IntegerArgument("hour"));
    withArguments(new IntegerArgument("minute"));

    executesPlayer((player, args) -> {
      LootDropConfig config = LootDropConfig.INSTANCE;

      int year = args.getOrDefaultUnchecked("year", 2024);
      int month = args.getOrDefaultUnchecked("month", 1);
      int day = args.getOrDefaultUnchecked("day", 1);
      int hour = args.getOrDefaultUnchecked("hour", 0);
      int minute = args.getOrDefaultUnchecked("minute", 0);

      config.setStartTimestamp(
          ZonedDateTime.of(year, month, day, hour, minute, 0, 0, ZoneId.systemDefault()));
      config.saveConfig();

      Chat.sendMessage(player,
          Component.text("Der Start wurde erfolgreich gesetzt.", NamedTextColor.GREEN));
    });
  }
}
