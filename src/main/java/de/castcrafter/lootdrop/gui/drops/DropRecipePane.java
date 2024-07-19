package de.castcrafter.lootdrop.gui.drops;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import de.castcrafter.lootdrop.config.drops.HourlyDropRecipe;
import de.castcrafter.lootdrop.utils.Chat;
import de.castcrafter.lootdrop.utils.SoundUtils;
import io.th0rgal.oraxen.api.OraxenItems;
import java.util.Map;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * The type Hourly drop recipe pane.
 */
public class DropRecipePane extends StaticPane {

  private final DropGui gui;
  private final HourlyDropRecipe dropRecipe;

  private final ItemStack firstItem;
  private final ItemStack secondItem;
  private final ItemStack resultItem;

  /**
   * Instantiates a new Hourly drop recipe pane.
   *
   * @param player the player
   * @param gui    the gui
   * @param recipe the recipe
   */
  public DropRecipePane(HumanEntity player, DropGui gui, HourlyDropRecipe recipe, int y) {
    super(0, y, 9, 1);

    this.gui = gui;
    this.dropRecipe = recipe;

    firstItem = recipe.getFirstItem() != null ? recipe.getFirstItem().toItemStack() : null;
    secondItem = recipe.getSecondItem() != null ? recipe.getSecondItem().toItemStack() : null;
    resultItem = recipe.getResultItem() != null ? recipe.getResultItem().toItemStack() : null;

    if (itemNotNull(firstItem)) {
      addItem(new GuiItem(firstItem.clone()), 0, 0);
    }

    if (itemNotNull(firstItem) && itemNotNull(secondItem)) {
      addItem(getPlusItem(), 1, 0);
    }

    if (itemNotNull(secondItem)) {
      addItem(new GuiItem(secondItem.clone()), 2, 0);
    }

    if ((itemNotNull(firstItem) || itemNotNull(secondItem)) && itemNotNull(resultItem)) {
      addItem(getEqualItem(player), 3, 0);
    }

    if (itemNotNull(resultItem)) {
      addItem(new GuiItem(resultItem.clone()), 4, 0);
    }

    if (itemNotNull(resultItem) && (itemNotNull(firstItem) || itemNotNull(secondItem)) &&
        dropRecipe.canPlayerUse(player.getUniqueId()) && canCraft(player).equals(
        CanCraftResult.CAN_CRAFT)) {
      addItem(getCraftItem(), 6, 0);
    }
  }

  /**
   * Item not null boolean.
   *
   * @param itemStack the item stack
   * @return the boolean
   */
  private boolean itemNotNull(ItemStack itemStack) {
    return itemStack != null && !itemStack.getType().equals(Material.AIR);
  }

  /**
   * Gets plus item.
   *
   * @return the plus item
   */
  private GuiItem getPlusItem() {
    ItemStack plusItem = OraxenItems.getItemById("daily_reward_plus").build();
    ItemMeta plusItemMeta = plusItem.getItemMeta();

    if (plusItemMeta != null) {
      plusItemMeta.displayName(Component.text("+", NamedTextColor.GOLD).decoration(
          TextDecoration.ITALIC,
          false
      ));
    }

    plusItem.setItemMeta(plusItemMeta);

    return new GuiItem(plusItem);
  }

  /**
   * Gets equal item.
   *
   * @param openedForPlayer the opened for player
   * @return the equal item
   */
  private GuiItem getEqualItem(HumanEntity openedForPlayer) {
    boolean canUse = dropRecipe.canPlayerUse(openedForPlayer.getUniqueId());
    boolean canCraft = canCraft(openedForPlayer).equals(CanCraftResult.CAN_CRAFT);
    boolean isEqual = canUse && canCraft;

    ItemStack equalItem =
        OraxenItems.getItemById(isEqual ? "daily_reward_equal" : "daily_reward_equal_slash")
            .build();
    ItemMeta equalItemMeta = equalItem.getItemMeta();

    if (equalItemMeta != null) {
      equalItemMeta.displayName(Component.text("=", NamedTextColor.GOLD).decoration(
          TextDecoration.ITALIC,
          false
      ));
    }

    equalItem.setItemMeta(equalItemMeta);

    return new GuiItem(equalItem);
  }


  /**
   * Gets craft item.
   *
   * @return the craft item
   */
  private GuiItem getCraftItem() {
    ItemStack equalItem = OraxenItems.getItemById("daily_reward_craft").build();
    ItemMeta equalItemMeta = equalItem.getItemMeta();

    if (equalItemMeta != null) {
      equalItemMeta.displayName(Component.text("Craft", NamedTextColor.GOLD).decoration(
          TextDecoration.ITALIC,
          false
      ));
    }

    equalItem.setItemMeta(equalItemMeta);

    return new GuiItem(equalItem, event -> {
      HumanEntity player = event.getWhoClicked();
      PlayerInventory inventory = player.getInventory();

      if (!dropRecipe.canPlayerUse(player.getUniqueId())) {
        SoundUtils.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, .5f, 1f);
        Chat.sendMessage(player,
            Component.text("Du hast diesen Trade bereits genutzt!", NamedTextColor.RED));
        return;
      }

      CanCraftResult canCraftResult = canCraft(player);

      if (canCraftResult.equals(CanCraftResult.FIRST_ITEM_MISSING)) {
        SoundUtils.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, .5f, 1f);
        Chat.sendMessage(player,
            Component.text("Du hast nicht genügend vom ersten Item!", NamedTextColor.RED));
        return;
      }

      if (canCraftResult.equals(CanCraftResult.SECOND_ITEM_MISSING)) {
        SoundUtils.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, .5f, 1f);
        Chat.sendMessage(
            player, Component.text("Du hast nicht genügend vom zweiten Item!", NamedTextColor.RED));
        return;
      }

      if (itemNotNull(firstItem)) {
        inventory.removeItem(firstItem);
      }

      if (itemNotNull(secondItem)) {
        inventory.removeItem(secondItem);
      }

      if (!itemNotNull(resultItem)) {
        return;
      }

      Map<Integer, ItemStack> notAdded = inventory.addItem(resultItem);

      if (!notAdded.isEmpty()) {
        notAdded.values()
            .forEach(item -> player.getWorld().dropItem(player.getLocation(), item, itemEntity -> {
              itemEntity.setPickupDelay(0);
              itemEntity.setOwner(player.getUniqueId());
            }));

        int amountDropped = notAdded.values().stream().mapToInt(ItemStack::getAmount).sum();
        Chat.sendMessage(
            player,
            Component.text("Achtung: ", NamedTextColor.RED, TextDecoration.BOLD)
                .append(Component.text(amountDropped, NamedTextColor.YELLOW)
                    .decoration(TextDecoration.BOLD, false))
                .append(Component.text(
                    " Items wurden fallengelassen, da dein Inventar voll war! Nur du kannst " +
                        "diese Items aufheben. Sie werden dennoch normal despawnen.",
                    NamedTextColor.RED
                ).decoration(TextDecoration.BOLD, false))
        );
      }

      SoundUtils.playSound(player, Sound.ENTITY_CHICKEN_EGG, .5f, 1f);

      dropRecipe.increasePlayerUses(player.getUniqueId());
      gui.update();
    });
  }

  /**
   * Can craft boolean.
   *
   * @param player the player
   * @return the boolean
   */
  private CanCraftResult canCraft(HumanEntity player) {
    PlayerInventory inventory = player.getInventory();

    CanCraftResult result = CanCraftResult.CAN_CRAFT;
    CanCraftResult firstItemResult = CanCraftResult.CAN_CRAFT;
    CanCraftResult secondItemResult = CanCraftResult.CAN_CRAFT;

    if (itemNotNull(firstItem)) {
      firstItemResult =
          inventory.containsAtLeast(firstItem, firstItem.getAmount()) ? CanCraftResult.CAN_CRAFT :
              CanCraftResult.FIRST_ITEM_MISSING;
    }

    if (itemNotNull(secondItem)) {
      secondItemResult =
          inventory.containsAtLeast(secondItem, secondItem.getAmount()) ? CanCraftResult.CAN_CRAFT :
              CanCraftResult.SECOND_ITEM_MISSING;
    }

    if (firstItemResult == CanCraftResult.FIRST_ITEM_MISSING) {
      result = CanCraftResult.FIRST_ITEM_MISSING;
    } else if (secondItemResult == CanCraftResult.SECOND_ITEM_MISSING) {
      result = CanCraftResult.SECOND_ITEM_MISSING;
    }

    return result;
  }

  /**
   * The enum Can craft result.
   */
  public enum CanCraftResult {
    FIRST_ITEM_MISSING,
    SECOND_ITEM_MISSING,
    CAN_CRAFT
  }
}
