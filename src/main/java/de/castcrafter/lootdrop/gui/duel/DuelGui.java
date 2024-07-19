package de.castcrafter.lootdrop.gui.duel;

import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import de.castcrafter.lootdrop.BukkitMain;
import de.castcrafter.lootdrop.duel.Duel;
import de.castcrafter.lootdrop.duel.DuelVoteState;
import de.castcrafter.lootdrop.gui.button.PlayerHeadButton;
import de.castcrafter.lootdrop.utils.Chat;
import de.castcrafter.lootdrop.utils.ItemUtils;
import de.castcrafter.lootdrop.utils.SoundUtils;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * The type Duel gui.
 */
public class DuelGui extends ChestGui {

  private final Duel duel;
  private final StaticPane pane;

  /**
   * Instantiates a new Duel gui.
   *
   * @param duel the duel
   */
  public DuelGui(Duel duel) {
    super(5, ComponentHolder.of(
        Component.text(
            "Duel: " + duel.getPlayerOne().getName() + " vs " + duel.getPlayerTwo().getName())));

    this.duel = duel;

    setOnGlobalClick(event -> event.setCancelled(true));
    setOnGlobalDrag(event -> event.setCancelled(true));

    OutlinePane topOutlinePane = new OutlinePane(0, 0, 9, 1);
    OutlinePane bottomOutlinePane = new OutlinePane(0, 4, 9, 1);

    topOutlinePane.addItem(
        new GuiItem(ItemUtils.getItemStack(Material.BLACK_STAINED_GLASS_PANE, 1, 0,
            Component.text(" ")
        )));
    topOutlinePane.setRepeat(true);

    bottomOutlinePane.addItem(
        new GuiItem(ItemUtils.getItemStack(Material.BLACK_STAINED_GLASS_PANE, 1, 0,
            Component.text(" ")
        )));
    bottomOutlinePane.setRepeat(true);

    addPane(topOutlinePane);
    addPane(bottomOutlinePane);

    this.pane = new StaticPane(0, 0, 9, 5);
    addPane(pane);

    update();
  }

  @Override
  public void update() {
    addPlayerCastButton(duel.getPlayerOne(), 2, 2);
    addPlayerCastButton(duel.getPlayerTwo(), 6, 2);

    super.update();
  }

  /**
   * Add player cast button.
   *
   * @param toCast the to cast
   * @param x      the x
   * @param y      the y
   */
  @SuppressWarnings("SameParameterValue")
  private void addPlayerCastButton(Player toCast, int x, int y) {
    pane.addItem(new PlayerHeadButton(
        toCast,
        (event, clickedPlayer) -> castVote((Player) event.getWhoClicked(), toCast),
        Component.text(toCast.getName(), NamedTextColor.GOLD),
        Component.text(""),
        Component.text("Klicke, um für diesen Spieler", NamedTextColor.GRAY),
        Component.text("abzustimmen.", NamedTextColor.GRAY)
    ), x, y);
  }

  /**
   * Cast vote.
   *
   * @param voter  the voter
   * @param player the player
   */
  private void castVote(Player voter, Player player) {
    DuelVoteState voteState = duel.addVote(voter, player);

    switch (voteState) {
      case VOTE_CLOSED:
        Chat.sendMessage(voter,
            Component.text("Die Abstimmung wurde beendet.", NamedTextColor.RED));
        playErrorClickSound(voter);
        break;
      case ALREADY_VOTED:
        Chat.sendMessage(voter, Component.text("Du hast bereits abgestimmt.", NamedTextColor.RED));
        playErrorClickSound(voter);
        break;
      case SUCCESS:
        Chat.sendMessage(voter, Component.text("Du hast für ", NamedTextColor.GREEN)
            .append(Component.text(player.getName(), NamedTextColor.YELLOW))
            .append(Component.text(" abgestimmt.", NamedTextColor.GREEN)));
        playSuccessClickSound(voter);
        break;
    }

    closeInventory(voter, true);
    update();
  }

  /**
   * Play success click sound.
   *
   * @param player the player
   */
  private void playSuccessClickSound(Player player) {
    SoundUtils.playSound(player, org.bukkit.Sound.BLOCK_NOTE_BLOCK_PLING, .5f, 1f);
  }

  /**
   * Play error click sound.
   *
   * @param player the player
   */
  private void playErrorClickSound(HumanEntity player) {
    player.playSound(
        Sound.sound().type(org.bukkit.Sound.BLOCK_NOTE_BLOCK_BASS).volume(.5f).build(),
        Sound.Emitter.self()
    );
  }

  /**
   * Close inventory.
   *
   * @param player the player
   */
  public static void closeInventory(HumanEntity player, boolean callRunnable) {
    if (!callRunnable) {
      player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
      return;
    }

    new BukkitRunnable() {
      @Override
      public void run() {
        player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
      }
    }.runTaskLater(BukkitMain.getInstance(), 1);
  }

}
