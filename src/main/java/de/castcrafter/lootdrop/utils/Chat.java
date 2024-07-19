package de.castcrafter.lootdrop.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

/**
 * The type Chat.
 */
public class Chat {

  /**
   * Prefix component.
   *
   * @return the component
   */
  public static Component prefix() {
    return Component.text(">> ", NamedTextColor.DARK_GRAY)
        .append(Component.text("Event", NamedTextColor.GOLD))
        .append(Component.text(" | ", NamedTextColor.DARK_GRAY));
  }

  /**
   * Send message.
   *
   * @param sender  the sender
   * @param message the message
   */
  public static void sendMessage(CommandSender sender, Component message) {
    sender.sendMessage(prefix().append(message));
  }

}
