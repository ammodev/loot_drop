package de.castcrafter.lootdrop.config.drops;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

/**
 * The type Hourly drop item stack.
 */
@ConfigSerializable
public class HourlyDropItemStack {

  private static final ComponentLogger LOGGER = ComponentLogger.logger(HourlyDropItemStack.class);

  public static final LegacyComponentSerializer SECTION_SERIALIZER =
      LegacyComponentSerializer.builder().extractUrls().hexColors().character('§').build();

  public static final LegacyComponentSerializer AND_SERIALIZER =
      LegacyComponentSerializer.builder().extractUrls().hexColors().character('&').build();

  private String material = "STONE";
  private int amount = 1;
  private String displayName = null;
  private List<String> lore = null;
  private boolean unbreakable = false;

  private List<HourlyDropItemStackEnchantment> enchantments = null;

  private List<HourlyDropItemStackPotionEffect> potionEffects = null;
  private String potionColor = null;

  /**
   * Instantiates a new Hourly drop item stack.
   */
  public HourlyDropItemStack() {
  }

  /**
   * Instantiates a new Hourly drop item stack.
   *
   * @param material      the material
   * @param amount        the amount
   * @param displayName   the display name
   * @param lore          the lore
   * @param enchantments  the enchantments
   * @param unbreakable   the unbreakable
   * @param potionColor   the potion color
   * @param potionEffects the potion effects
   */
  public HourlyDropItemStack(
      String material, int amount, String displayName, List<String> lore,
      List<HourlyDropItemStackEnchantment> enchantments,
      boolean unbreakable,
      String potionColor,
      List<HourlyDropItemStackPotionEffect> potionEffects
  ) {
    this.material = material;
    this.amount = amount;
    this.displayName = displayName;
    this.lore = lore;
    this.enchantments = enchantments;
    this.unbreakable = unbreakable;

    this.potionColor = potionColor;
    this.potionEffects = potionEffects;
  }

  /**
   * Gets material.
   *
   * @return the material
   */
  public String getMaterialName() {
    return material;
  }

  /**
   * Gets amount.
   *
   * @return the amount
   */
  public int getAmount() {
    return amount;
  }

  /**
   * Gets color.
   *
   * @return the color
   */
  public Color getPotionColor() {
    Color bukkitColor = null;

    if (potionColor != null) {
      ColorMap colorMap = ColorMap.getByColorName(potionColor);

      if (colorMap != null) {
        bukkitColor = colorMap.getColor();
      } else {
        try {
          bukkitColor = Color.fromRGB(Integer.parseInt(potionColor, 16));
        } catch (NumberFormatException exception) {
          LOGGER.error("Could not parse color " + potionColor + " for itemstack " + this,
              exception);
        }
      }
    }

    return bukkitColor;
  }

  /**
   * Gets display name.
   *
   * @return the display name
   */
  public final Component getDisplayNameComponent() {
    return SECTION_SERIALIZER.deserialize(
        AND_SERIALIZER.serialize(AND_SERIALIZER.deserialize(displayName)));
  }

  /**
   * Gets lore.
   *
   * @return the lore
   */
  public final List<Component> getLoreComponentList() {
    List<Component> loreList = new ArrayList<>();

    for (String loreLine : lore) {
      loreList.add(
          SECTION_SERIALIZER.deserialize(
              AND_SERIALIZER.serialize(AND_SERIALIZER.deserialize(loreLine))));
    }

    return loreList;
  }

  /**
   * To item stack item stack.
   *
   * @return the item stack
   */
  public ItemStack toItemStack() {
    ItemStack itemStack = new ItemStack(Material.valueOf(material.toUpperCase()), amount);
    ItemMeta itemMeta = itemStack.getItemMeta();

    if (displayName != null) {
      itemMeta.displayName(getDisplayNameComponent().decoration(TextDecoration.ITALIC, false));
    }

    if (lore != null && !lore.isEmpty()) {
      itemMeta.lore(getLoreComponentList().stream().map(component -> component.decoration(
          TextDecoration.ITALIC,
          false
      )).toList());
    }

    if (enchantments != null && !enchantments.isEmpty()) {
      for (HourlyDropItemStackEnchantment enchantment : enchantments) {
        Enchantment bukkitEnchantment = enchantment.getEnchantment();

        if (bukkitEnchantment != null) {
          itemMeta.addEnchant(bukkitEnchantment, enchantment.getLevel(), true);
        }
      }
    }

    if (potionEffects != null && !potionEffects.isEmpty()
        && itemMeta instanceof PotionMeta potionMeta) {
      Color color = getPotionColor();
      if (color != null) {
        potionMeta.setColor(color);
      }

      for (HourlyDropItemStackPotionEffect potionEffect : potionEffects) {
        potionMeta.addCustomEffect(potionEffect.getPotionEffect(), true);
      }
    }

    if (itemMeta != null && unbreakable) {
      itemMeta.setUnbreakable(true);
    }

    itemStack.setItemMeta(itemMeta);

    return itemStack;
  }

  /**
   * Gets display name.
   *
   * @return the display name
   */
  public String getDisplayName() {
    return displayName;
  }

  /**
   * Gets lore.
   *
   * @return the lore
   */
  public List<String> getLore() {
    return lore;
  }

  /**
   * Gets enchantments.
   *
   * @return the enchantments
   */
  public List<HourlyDropItemStackEnchantment> getEnchantments() {
    return enchantments;
  }

  /**
   * Is unbreakable boolean.
   *
   * @return the boolean
   */
  public boolean isUnbreakable() {
    return unbreakable;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (other == null || getClass() != other.getClass()) {
      return false;
    }

    HourlyDropItemStack otherItemStack = (HourlyDropItemStack) other;

    return amount == otherItemStack.amount && Objects.equals(material, otherItemStack.material) &&
        Objects.equals(displayName, otherItemStack.displayName) &&
        Objects.equals(lore, otherItemStack.lore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(material, amount, displayName, lore);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
