package de.castcrafter.lootdrop.config.drops;

import java.util.Arrays;
import org.bukkit.Color;

/**
 * The enum Color map.
 */
public enum ColorMap {

  WHITE(Color.WHITE, "white"),
  SILVER(Color.SILVER, "silver"),
  GRAY(Color.GRAY, "gray"),
  BLACK(Color.BLACK, "black"),
  RED(Color.RED, "red"),
  MAROON(Color.MAROON, "maroon"),
  YELLOW(Color.YELLOW, "yellow"),
  OLIVE(Color.OLIVE, "olive"),
  LIME(Color.LIME, "lime"),
  GREEN(Color.GREEN, "green"),
  AQUA(Color.AQUA, "aqua"),
  TEAL(Color.TEAL, "teal"),
  BLUE(Color.BLUE, "blue"),
  NAVY(Color.NAVY, "navy"),
  FUCHSIA(Color.FUCHSIA, "fuchsia"),
  PURPLE(Color.PURPLE, "purple"),
  ORANGE(Color.ORANGE, "orange");

  private final Color color;
  private final String colorName;

  /**
   * Instantiates a new Color map.
   *
   * @param color     the color
   * @param colorName the color name
   */
  ColorMap(Color color, String colorName) {
    this.color = color;
    this.colorName = colorName;
  }

  /**
   * Gets by bukkit color.
   *
   * @param color the color
   * @return the by bukkit color
   */
  public static ColorMap getByBukkitColor(Color color) {
    return Arrays.stream(values())
        .filter(value -> value.getColor().equals(color))
        .findFirst()
        .orElse(null);
  }

  /**
   * Gets by color name.
   *
   * @param colorName the color name
   * @return the by color name
   */
  public static ColorMap getByColorName(String colorName) {
    return Arrays.stream(values())
        .filter(value -> value.getColorName().equalsIgnoreCase(colorName))
        .findFirst()
        .orElse(null);
  }

  /**
   * Gets color.
   *
   * @return the color
   */
  public Color getColor() {
    return color;
  }

  /**
   * Gets color name.
   *
   * @return the color name
   */
  public String getColorName() {
    return colorName;
  }
}
