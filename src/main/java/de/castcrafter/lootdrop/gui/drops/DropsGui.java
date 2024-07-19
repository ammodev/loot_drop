package de.castcrafter.lootdrop.gui.drops;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.component.PagingButtons;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import de.castcrafter.lootdrop.config.LootDropConfig;
import de.castcrafter.lootdrop.config.drops.HourlyDrop;
import de.castcrafter.lootdrop.gui.button.DropHourButton;
import io.th0rgal.oraxen.api.OraxenItems;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * The type Drops gui.
 */
public class DropsGui extends ChestGui {

  private final Player openedForPlayer;

  /**
   * Gets current hour since start.
   *
   * @param startTime   the start time
   * @param currentTime the current time
   * @return the current hour since start
   */
  public static int getCurrentHourSinceStart(ZonedDateTime startTime, ZonedDateTime currentTime) {
    return (int) ((currentTime.toEpochSecond() - startTime.toEpochSecond()) / 3600);
  }

  /**
   * Instantiates a new Drops gui.
   *
   * @param openedForPlayer the opened for player
   */
  public DropsGui(Player openedForPlayer) {
    super(6, "<shift:-8><glyph:daily_rewards_ui>");

    this.openedForPlayer = openedForPlayer;

    setOnGlobalClick(event -> event.setCancelled(true));
    setOnGlobalDrag(event -> event.setCancelled(true));

    PaginatedPane paginatedPane = new PaginatedPane(1, 2, 7, 3);

    List<GuiItem> items = new ArrayList<>();
    List<HourlyDrop> drops = LootDropConfig.INSTANCE.getDrops();

    for (HourlyDrop hourlyDrop : drops) {
      items.add(getHourGuiItem(hourlyDrop));
    }

    paginatedPane.populateWithGuiItems(items);

    PagingButtons pagingButtons = new PagingButtons(Slot.fromXY(0, 3), 9, paginatedPane);
    setBackItem(pagingButtons);
    setForwardItem(pagingButtons);

    addPane(paginatedPane);
    addPane(pagingButtons);

    update();
  }

  /**
   * Sets back item.
   *
   * @param buttons the buttons
   */
  public static void setBackItem(PagingButtons buttons) {
    ItemStack previous = OraxenItems.getItemById("daily_reward_previous").build();
    ItemMeta itemMeta = previous.getItemMeta();

    if (itemMeta != null) {
      itemMeta.displayName(Component.text("Zurück", NamedTextColor.GOLD).decoration(
          TextDecoration.ITALIC,
          false
      ));
      previous.setItemMeta(itemMeta);
    }

    buttons.setBackwardButton(
        new GuiItem(previous));
  }

  /**
   * Sets forward item.
   *
   * @param buttons the buttons
   */
  public static void setForwardItem(PagingButtons buttons) {
    ItemStack next = OraxenItems.getItemById("daily_reward_next").build();
    ItemMeta itemMeta = next.getItemMeta();

    if (itemMeta != null) {
      itemMeta.displayName(Component.text("Weiter", NamedTextColor.GOLD).decoration(
          TextDecoration.ITALIC,
          false
      ));
      next.setItemMeta(itemMeta);
    }

    buttons.setForwardButton(
        new GuiItem(next));
  }

  /**
   * Create items pane paginated pane.
   *
   * @param hourlyDrop the hourly drop
   * @return the paginated pane
   */
  public GuiItem getHourGuiItem(HourlyDrop hourlyDrop) {
    return new DropHourButton(this, openedForPlayer, hourlyDrop);
  }
}
