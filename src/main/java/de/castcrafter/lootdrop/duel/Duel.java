package de.castcrafter.lootdrop.duel;

import de.castcrafter.lootdrop.Main;
import de.castcrafter.lootdrop.gui.duel.DuelGui;
import de.castcrafter.lootdrop.utils.Chat;
import de.castcrafter.lootdrop.utils.SoundUtils;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * The type Duel.
 */
public class Duel {

  private final Player playerOne;
  private final Player playerTwo;

  private final Map<Player, Player> playerVotes;
  private boolean voteOpen;

  private final List<ItemStack> rewards;

  private final DuelVoteTimer voteTimer;

  /**
   * Instantiates a new Duel.
   *
   * @param playerOne    the player one
   * @param playerTwo    the player two
   * @param rewards      the rewards
   * @param voteDuration the vote duration
   */
  public Duel(Player playerOne, Player playerTwo, List<ItemStack> rewards, int voteDuration) {
    this.playerOne = playerOne;
    this.playerTwo = playerTwo;

    this.voteOpen = true;
    this.playerVotes = new HashMap<>();

    this.rewards = rewards.stream().map(ItemStack::clone).toList();

    this.voteTimer = new DuelVoteTimer(this, voteDuration);
    this.voteTimer.start();
  }

  /**
   * End vote.
   */
  public void endVote() {
    this.voteOpen = false;

    new BukkitRunnable() {
      @Override
      public void run() {
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
          Chat.sendMessage(onlinePlayer, Component.text(
              "Das Duell wird in wenigen Sekunden gestartet. Bitte warte kurz.",
              NamedTextColor.GRAY
          ));

          SoundUtils.playSound(onlinePlayer, Sound.ENTITY_ENDER_DRAGON_GROWL, .5f, 1f);
          DuelGui.closeInventory(onlinePlayer, false);
        });
      }
    }.runTaskLater(Main.getInstance(), 1L);
  }

  /**
   * Add vote duel vote state.
   *
   * @param voter  the voter
   * @param player the player
   * @return the duel vote state
   */
  public DuelVoteState addVote(Player voter, Player player) {
    if (!voteOpen) {
      return DuelVoteState.VOTE_CLOSED;
    }

    if (playerVotes.containsKey(voter)) {
      return DuelVoteState.ALREADY_VOTED;
    }

    playerVotes.put(voter, player);

    return DuelVoteState.SUCCESS;
  }

  /**
   * Gets votes.
   *
   * @param player the player
   * @return the votes
   */
  public int getVotes(Player player) {
    return (int) playerVotes.values().stream().filter(player::equals).count();
  }

  /**
   * Gets votes percentage.
   *
   * @param player the player
   * @return the votes percentage
   */
  public int getVotesPercentage(Player player) {
    int votes = getVotes(player);

    if (votes == 0) {
      return 0;
    }

    return (int) ((double) votes / playerVotes.size() * 100);
  }

  /**
   * Gets player one.
   *
   * @return the player one
   */
  public Player getPlayerOne() {
    return playerOne;
  }

  /**
   * Gets player two.
   *
   * @return the player two
   */
  public Player getPlayerTwo() {
    return playerTwo;
  }

  /**
   * Is vote open boolean.
   *
   * @return the boolean
   */
  public boolean isVoteOpen() {
    return voteOpen;
  }

  /**
   * Open duel gui.
   *
   * @param playSound the play sound
   */
  public void openDuelGui(boolean playSound) {
    Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
      new DuelGui(this).show(onlinePlayer);

      if (playSound) {
        SoundUtils.playSound(onlinePlayer, Sound.ENTITY_ENDER_DRAGON_GROWL, .5f, .75f);
      }
    });
  }

  /**
   * Finish duel.
   *
   * @param state the state
   */
  @SuppressWarnings("DuplicatedCode")
  public void finishDuel(DuelFinishState state) {
    if (voteTimer != null) {
      voteTimer.stop();
    }

    Title title = getWinningTitle(state);

    new BukkitRunnable() {
      @Override
      public void run() {
        Bukkit.getOnlinePlayers()
            .forEach(onlinePlayer -> DuelGui.closeInventory(onlinePlayer, false));
      }
    }.runTaskLater(Main.getInstance(), 1L);

    Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
      SoundUtils.playSound(onlinePlayer, Sound.ENTITY_ENDER_DRAGON_GROWL, .5f, .75f);

      printDuelEndMessage(onlinePlayer, state);

      if (title != null) {
        onlinePlayer.showTitle(title);
      }
    });

    new BukkitRunnable() {
      @Override
      public void run() {
        awardRewards(state);

        DuelManager.INSTANCE.setRunningDuel(null);
      }
    }.runTaskLater(Main.getInstance(), 20 * 4);
  }

  /**
   * Award rewards.
   *
   * @param state the state
   */
  private void awardRewards(DuelFinishState state) {
    if (!(state.isPlayerOneWon() || state.isPlayerTwoWon() || state.isDraw())) {
      return;
    }

    List<Player> awardingRewards = new ArrayList<>();
    boolean split = false;

    if (state.isPlayerOneWon() || state.isPlayerTwoWon()) {
      Player winningPlayer = state.isPlayerOneWon() ? playerOne : playerTwo;
      awardingRewards.addAll(playerVotes.entrySet().stream()
          .filter(entry -> entry.getValue().equals(winningPlayer))
          .map(Map.Entry::getKey)
          .toList());
    } else if (state.isDraw()) {
      awardingRewards.addAll(playerVotes.keySet());
      split = true;
    }

    final boolean finalSplit = split;
    awardingRewards.forEach(player -> {
      if (player == null || !player.isOnline()) {
        return;
      }

      AtomicInteger totalDroppedCount = new AtomicInteger(0);
      rewards.forEach(reward -> {
        ItemStack clone = reward.clone();

        if (clone.getMaxStackSize() > 1) {
          int splitAmount = clone.getAmount() / (finalSplit ? 2 : 1);

          clone.setAmount(Math.max(1, splitAmount));
        }

        HashMap<Integer, ItemStack> notAddedItems = player.getInventory().addItem(clone);

        if (!notAddedItems.isEmpty()) {
          notAddedItems.values()
              .forEach(itemStack -> player.getWorld().dropItem(player.getLocation(), itemStack,
                  item -> {
                    item.setCanMobPickup(false);
                    item.setCanPlayerPickup(true);
                    item.setOwner(
                        player.getUniqueId());
                    item.setUnlimitedLifetime(true);
                  }
              ));

          totalDroppedCount.set(totalDroppedCount.get() +
              notAddedItems.values().stream().mapToInt(ItemStack::getAmount).sum());
        }
      });

      int totalDroppedCountInt = totalDroppedCount.get();
      if (totalDroppedCountInt > 0) {
        Chat.sendMessage(player, Component.text(
                "Dein Inventar war voll, daher wurde" + (totalDroppedCountInt == 1 ? " " : "n "),
                NamedTextColor.RED
            ).append(Component.text(
                totalDroppedCount + " Item" + (totalDroppedCountInt == 1 ? "" : "s"),
                NamedTextColor.YELLOW
            ))
            .append(Component.text(
                " auf den Boden geworfen. Nur du kannst diese" +
                    (totalDroppedCountInt == 1 ? "s" : "") +
                    " Item " + (totalDroppedCountInt == 1 ? "" : "s") +
                    "einsammeln.",
                NamedTextColor.RED
            )));
      }

      Chat.sendMessage(player,
          Component.text("Vielen Dank für deine Teilnahme!", NamedTextColor.GRAY));
    });
  }

  /**
   * Print duel end message.
   *
   * @param player the player
   * @param state  the state
   */
  private void printDuelEndMessage(Player player, DuelFinishState state) {
    Chat.sendMessage(player, Component.empty());
    Chat.sendMessage(player,
        Component.text("-------------------------------", NamedTextColor.GRAY));
    Chat.sendMessage(player, Component.empty());
    Chat.sendMessage(player, Component.text("Das Duell wurde beendet", NamedTextColor.GOLD));
    Chat.sendMessage(player, Component.empty());

    if (state.isPlayerOneWon() || state.isPlayerTwoWon()) {
      Player winningPlayer = state.isPlayerOneWon() ? playerOne : playerTwo;

      Chat.sendMessage(
          player, Component.text(winningPlayer.getName() + " hat das Duell gewonnen.",
              NamedTextColor.GREEN));
    } else if (state.isDraw()) {
      Chat.sendMessage(player,
          Component.text("Das Duell endete unentschieden.", NamedTextColor.GRAY));
    } else if (state.isForceStop()) {
      Chat.sendMessage(player,
          Component.text("Es werden keine Gewinne verteilt, da das Duell manuell beendet " +
              "wurde.", NamedTextColor.GRAY));
    } else if (state.isNobodyWon()) {
      Chat.sendMessage(player,
          Component.text("Niemand hat das Duell gewonnen, daher werden keine Gewinne " +
              "verteilt.", NamedTextColor.GRAY));
    } else if (state.isPlayerLeft()) {
      Chat.sendMessage(player, Component.text(
          "Ein Spieler hat das Duell verlassen, daher wird das Duell beendet.",
          NamedTextColor.GRAY
      ));
    }
    String playerOneName = playerOne.getName();
    String playerTwoName = playerTwo.getName();

    int playerOneVotes = getVotes(playerOne);
    int playerTwoVotes = getVotes(playerTwo);

    int playerOneVotesPercentage = getVotesPercentage(playerOne);
    int playerTwoVotesPercentage = getVotesPercentage(playerTwo);

    Chat.sendMessage(player, Component.empty());
    Chat.sendMessage(player, Component.text("Abgegebene Stimmen:", NamedTextColor.GRAY));
    Chat.sendMessage(player, Component.text(" - " + playerOneName + ": " + playerOneVotes + " (" +
        playerOneVotesPercentage + "%)", NamedTextColor.GREEN));
    Chat.sendMessage(player, Component.text(" - " + playerTwoName + ": " + playerTwoVotes + " (" +
        playerTwoVotesPercentage + "%)", NamedTextColor.GREEN));

    Chat.sendMessage(player, Component.empty());
    Chat.sendMessage(player, Component.text("-----------------------------", NamedTextColor.GRAY));
    Chat.sendMessage(player, Component.empty());
  }

  /**
   * Gets winning title.
   *
   * @param state the state
   * @return the winning title
   */
  private Title getWinningTitle(DuelFinishState state) {
    String playerName = "ERROR";

    switch (state) {
      case PLAYER_ONE_WON -> playerName = playerOne.getName();
      case PLAYER_TWO_WON -> playerName = playerTwo.getName();
      case NOBODY_WON -> playerName = "Niemand";
    }

    Component titleComponent = null;
    Component subtitleComponent = null;

    if (state.isWon()) {
      titleComponent = Component.text(playerName, NamedTextColor.GOLD, TextDecoration.BOLD);
      subtitleComponent = Component.text("hat das Duell gewonnen", NamedTextColor.GRAY);
    } else if (state.isDraw()) {
      titleComponent = Component.text("Unentschieden", NamedTextColor.GOLD, TextDecoration.BOLD);
      subtitleComponent = Component.text("Niemand hat das Duell gewonnen", NamedTextColor.GRAY);
    } else if (state.isForceStop()) {
      titleComponent = Component.text("Das Duell wurde beendet", NamedTextColor.GOLD,
          TextDecoration.BOLD);
      subtitleComponent = Component.text("Niemand hat das Duell gewonnen", NamedTextColor.GRAY);
    } else if (state.isPlayerLeft()) {
      titleComponent = Component.text("Das Duell wurde beendet", NamedTextColor.GOLD,
          TextDecoration.BOLD);
      subtitleComponent = Component.text("Ein Spieler hat das Duell verlassen",
          NamedTextColor.GRAY);
    }

    if (titleComponent != null) {
      Title.Times times =
          Title.Times.times(Duration.ofMillis(150), Duration.ofSeconds(4), Duration.ofMillis(150));

      return Title.title(titleComponent, subtitleComponent, times);
    }

    return null;
  }

  /**
   * Gets vote timer.
   *
   * @return the vote timer
   */
  public DuelVoteTimer getVoteTimer() {
    return voteTimer;
  }
}
