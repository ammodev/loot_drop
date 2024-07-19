package de.castcrafter.lootdrop.utils;

import java.util.Arrays;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * The type Item utils.
 */
public class ItemUtils {

  /**
   * Gets item stack.
   *
   * @param material    the material
   * @param amount      the amount
   * @param durability  the durability
   * @param displayName the display name
   * @param lore        the lore
   * @return the item stack
   */
  public static ItemStack getItemStack(
      Material material, int amount, int durability, Component displayName,
      Component... lore
  ) {
    ItemStack itemStack = new ItemStack(material, amount);
    ItemMeta itemMeta = itemStack.getItemMeta();

    if (itemMeta instanceof Damageable damageable) {
      damageable.setDamage(durability);
    }

    itemMeta.displayName(displayName.decoration(TextDecoration.ITALIC, false));

    if (lore != null && lore.length > 0) {
      itemMeta.lore(
          Arrays.stream(lore).map(component -> component.decoration(TextDecoration.ITALIC, false))
              .toList());
    }

    itemStack.setItemMeta(itemMeta);

    return itemStack;
  }

  /**
   * Gets player head.
   *
   * @param player      the player
   * @param displayName the display name
   * @param lore        the lore
   * @return the player head
   */
  public static ItemStack getPlayerHead(OfflinePlayer player, Component displayName,
      Component... lore) {
    ItemStack playerHead = getItemStack(Material.PLAYER_HEAD, 1, 0, displayName, lore);
    ItemMeta itemMeta = playerHead.getItemMeta();

    if (!(itemMeta instanceof SkullMeta skullMeta)) {
      throw new IllegalStateException("ItemMeta is not a SkullMeta");
    }

    skullMeta.setOwningPlayer(player);
    playerHead.setItemMeta(skullMeta);

    return playerHead;
  }
}
