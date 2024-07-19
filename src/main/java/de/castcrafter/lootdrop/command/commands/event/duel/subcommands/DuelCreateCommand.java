package de.castcrafter.lootdrop.command.commands.event.duel.subcommands;

import de.castcrafter.lootdrop.duel.Duel;
import de.castcrafter.lootdrop.duel.DuelManager;
import de.castcrafter.lootdrop.utils.Chat;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.TimeArgument;
import java.util.Arrays;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * The type Duel create command.
 */
public class DuelCreateCommand extends CommandAPICommand {

  /**
   * Instantiates a new Duel create command.
   *
   * @param commandName the command name
   */
  public DuelCreateCommand(String commandName) {
    super(commandName);

    withPermission("lootdrop.command.duel.create");

    withArguments(new PlayerArgument("playerOne"));
    withArguments(new PlayerArgument("playerTwo"));

    withArguments(new TimeArgument("voteDuration"));

    executesPlayer((player, args) -> {
      if (DuelManager.INSTANCE.getRunningDuel() != null) {
        Chat.sendMessage(player, Component.text(
            "Es läuft bereits ein Duell. Bitte beende dieses, bevor du ein " +
                "neues starten kannst!", NamedTextColor.RED
        ));

        return;
      }

      Player playerOne = args.getUnchecked("playerOne");
      Player playerTwo = args.getUnchecked("playerTwo");

      int voteDuration = args.getOrDefaultUnchecked("voteDuration", 0) / 20;
      List<ItemStack> rewards =
          Arrays.stream(player.getInventory().getStorageContents())
              .filter(itemStack -> itemStack != null && !itemStack.getType().equals(
                  Material.AIR)).toList();

      Duel runningDuel = new Duel(playerOne, playerTwo, rewards, voteDuration);
      DuelManager.INSTANCE.setRunningDuel(runningDuel);

      runningDuel.openDuelGui(true);
    });
  }
}
