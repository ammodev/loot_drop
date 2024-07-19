package de.castcrafter.lootdrop.gui.button;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import de.castcrafter.lootdrop.utils.ItemUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * The type Player head button.
 */
public class PlayerHeadButton extends GuiItem {

  /**
   * Instantiates a new Player head button.
   *
   * @param player      the player
   * @param action      the action
   * @param displayName the display name
   * @param lore        the lore
   */
  public PlayerHeadButton(
      OfflinePlayer player, PlayerHeadButtonClicked action,
      Component displayName, Component... lore
  ) {
    super(ItemUtils.getPlayerHead(player, displayName, lore),
        (event) -> action.onClick(event, player.getPlayer()));
  }

  /**
   * The interface Player head button clicked.
   */
  @FunctionalInterface
  public interface PlayerHeadButtonClicked {

    /**
     * On click.
     *
     * @param event         the event
     * @param clickedPlayer the clicked player
     */
    void onClick(InventoryClickEvent event, Player clickedPlayer);
  }
}
