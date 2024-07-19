package de.castcrafter.lootdrop.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

/**
 * The type Time utils.
 */
public class TimeUtils {

  /**
   * Format seconds component.
   *
   * @param seconds the seconds
   * @return the component
   */
  public static Component formatSeconds(int seconds) {
    return formatSeconds(seconds, NamedTextColor.YELLOW, NamedTextColor.YELLOW);
  }

  /**
   * Format seconds component.
   *
   * @param seconds     the seconds
   * @param timeColor   the time color
   * @param spacerColor the spacer color
   * @return the component
   */
  public static Component formatSeconds(int seconds, TextColor timeColor, TextColor spacerColor) {
    int minutes = seconds / 60;
    int remainingSeconds = seconds % 60;

    String format = "%02d:%02d";
    String replaced = String.format(format, minutes, remainingSeconds);

    Component component = Component.text(replaced, timeColor);
    component = component.replaceText(
        TextReplacementConfig.builder().match(":")
            .replacement(Component.text(":").color(spacerColor)).build());

    return component.color(timeColor);
  }

}
