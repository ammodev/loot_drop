package de.castcrafter.lootdrop.config.drops;

import de.castcrafter.lootdrop.config.LootDropConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.inventory.MerchantRecipe;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

/**
 * The type Hourly drop.
 */
@ConfigSerializable
public class HourlyDrop {

  private int hour = 0;
  private List<HourlyDropRecipe> recipes = new ArrayList<>();

  /**
   * Instantiates a new Hourly drop.
   */
  public HourlyDrop() {
  }

  /**
   * Instantiates a new Hourly drop.
   *
   * @param hour    the hour
   * @param recipes the recipes
   */
  public HourlyDrop(int hour, List<HourlyDropRecipe> recipes) {
    this.hour = hour;
    this.recipes = recipes;
  }

  /**
   * Gets by hour.
   *
   * @param hour the hour
   * @return the by hour
   */
  public static HourlyDrop getByHour(int hour) {
    return LootDropConfig.INSTANCE.getDrops().stream().filter(drop -> drop.getHour() == hour)
        .findFirst()
        .orElse(null);
  }

  /**
   * Gets by merchant recipe.
   *
   * @param merchantRecipe the merchant recipe
   * @return the by merchant recipe
   */
  public HourlyDropRecipe findByMerchantRecipe(MerchantRecipe merchantRecipe) {
    return recipes.stream().filter(recipe -> recipe.matchesMerchantRecipe(merchantRecipe))
        .findFirst().orElse(null);
  }

  /**
   * Reset player.
   *
   * @param uuid the uuid
   */
  public void resetPlayer(UUID uuid) {
    recipes.forEach(recipe -> recipe.resetPlayer(uuid));
  }

  /**
   * Reset all players.
   */
  public void resetAllPlayers() {
    recipes.forEach(HourlyDropRecipe::resetAllPlayers);
  }

  /**
   * Can player use boolean.
   *
   * @param uuid the uuid
   * @return the boolean
   */
  public boolean canPlayerUse(UUID uuid) {
    return recipes.stream().anyMatch(recipe -> recipe.canPlayerUse(uuid));
  }

  /**
   * Gets hour.
   *
   * @return the hour
   */
  public int getHour() {
    return hour;
  }

  /**
   * Gets recipes.
   *
   * @return the recipes
   */
  public List<HourlyDropRecipe> getRecipes() {
    return recipes;
  }

  /**
   * Gets merchant recipes.
   *
   * @param uuid the uuid
   * @return the merchant recipes
   */
  public List<MerchantRecipe> getMerchantRecipes(UUID uuid) {
    List<MerchantRecipe> merchantRecipes = new ArrayList<>();

    for (HourlyDropRecipe recipe : recipes) {
      merchantRecipes.add(recipe.toRecipe(uuid));
    }

    return merchantRecipes;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (other == null || getClass() != other.getClass()) {
      return false;
    }

    HourlyDrop otherDrop = (HourlyDrop) other;

    return hour == otherDrop.hour && Objects.equals(recipes, otherDrop.recipes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hour, recipes);
  }
}
