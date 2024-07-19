package de.castcrafter.lootdrop.config.drops;

import java.util.Objects;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

/**
 * The type Hourly drop item stack enchantment.
 */
@ConfigSerializable
public class HourlyDropItemStackEnchantment {

  private String enchantmentNamespace = null;
  private String enchantment;
  private int level = 1;

  /**
   * Instantiates a new Hourly drop item stack enchantment.
   */
  public HourlyDropItemStackEnchantment() {
  }

  /**
   * Instantiates a new Hourly drop item stack enchantment.
   *
   * @param enchantmentNamespace the enchantment namespace
   * @param enchantment          the enchantment
   * @param level                the level
   */
  public HourlyDropItemStackEnchantment(String enchantmentNamespace, String enchantment,
      int level) {
    this.enchantmentNamespace = enchantmentNamespace;
    this.enchantment = enchantment;
    this.level = level;
  }

  /**
   * Gets enchantment.
   *
   * @return the enchantment
   */
  public Enchantment getEnchantment() {
    String namespace = enchantmentNamespace;

    if (namespace == null) {
      namespace = "minecraft";
    }

    return Registry.ENCHANTMENT.get(new NamespacedKey(namespace, enchantment));
  }

  /**
   * Gets enchantment string.
   *
   * @return the enchantment string
   */
  public String getEnchantmentString() {
    return enchantment;
  }

  /**
   * Gets enchantment namespace.
   *
   * @return the enchantment namespace
   */
  public String getEnchantmentNamespace() {
    return enchantmentNamespace;
  }

  /**
   * Gets level.
   *
   * @return the level
   */
  public int getLevel() {
    return level;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }

    HourlyDropItemStackEnchantment otherEnchantment = (HourlyDropItemStackEnchantment) other;

    return level == otherEnchantment.level &&
        Objects.equals(enchantmentNamespace, otherEnchantment.enchantmentNamespace) &&
        Objects.equals(enchantment, otherEnchantment.enchantment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(enchantmentNamespace, enchantment, level);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
