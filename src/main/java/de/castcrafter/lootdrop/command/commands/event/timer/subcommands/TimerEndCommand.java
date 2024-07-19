package de.castcrafter.lootdrop.command.commands.event.timer.subcommands;

import de.castcrafter.lootdrop.config.LootDropConfig;
import de.castcrafter.lootdrop.timer.LootDropTimer;
import de.castcrafter.lootdrop.utils.Chat;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * The type Timer end command.
 */
public class TimerEndCommand extends CommandAPICommand {

  /**
   * Instantiates a new Timer end command.
   *
   * @param commandName the command name
   */
  public TimerEndCommand(String commandName) {
    super(commandName);

    withPermission("lootdrop.command.timer.end");

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

      ZonedDateTime endTime =
          ZonedDateTime.of(year, month, day, hour, minute, 0, 0, ZoneId.systemDefault());

      config.setEndTimestamp(endTime);

      long diff = endTime.toEpochSecond() - ZonedDateTime.now().toEpochSecond();

      LootDropTimer timer = config.getTimer();
      if (timer != null) {
        timer.setDuration(Duration.ofSeconds(diff));
        Chat.sendMessage(player, Component.text(
            "Der Timer wurde erfolgreich auf das neue Ende umgestellt!",
            NamedTextColor.GREEN
        ));
      }

      Chat.sendMessage(player, Component.text(
          "Das Ende des Timers wurde erfolgreich gesetzt!",
          NamedTextColor.GREEN
      ));

      config.saveConfig();
    });
  }
}
