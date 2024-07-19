package de.castcrafter.lootdrop.command.commands.event.timer.subcommands;

import de.castcrafter.lootdrop.config.LootDropConfig;
import de.castcrafter.lootdrop.timer.LootDropTimer;
import de.castcrafter.lootdrop.utils.Chat;
import dev.jorel.commandapi.CommandAPICommand;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * The type Timer create command.
 */
public class TimerCreateCommand extends CommandAPICommand {

  /**
   * Instantiates a new Timer create command.
   *
   * @param commandName the command name
   */
  public TimerCreateCommand(String commandName) {
    super(commandName);

    withPermission("lootdrop.command.timer.create");

    executesPlayer((player, args) -> {
      LootDropConfig config = LootDropConfig.INSTANCE;
      LootDropTimer oldTimer = config.getTimer();

      if (oldTimer != null && oldTimer.isRunning()) {
        Chat.sendMessage(player, Component.text("Es läuft bereits ein Timer!", NamedTextColor.RED));
        return;
      }

      ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
      ZonedDateTime endTime = LootDropConfig.INSTANCE.getEndTimestamp();
      long diff = endTime.toEpochSecond() - now.toEpochSecond();

      LootDropTimer timer = new LootDropTimer(Duration.ofSeconds(diff));
      timer.start();

      config.setTimer(timer);
      config.saveConfig();
    });
  }
}
