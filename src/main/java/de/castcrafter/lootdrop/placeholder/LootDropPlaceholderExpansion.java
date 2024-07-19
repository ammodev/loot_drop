package de.castcrafter.lootdrop.placeholder;

import de.castcrafter.lootdrop.config.LootDropConfig;
import de.castcrafter.lootdrop.duel.Duel;
import de.castcrafter.lootdrop.duel.DuelManager;
import de.castcrafter.lootdrop.duel.DuelVoteTimer;
import de.castcrafter.lootdrop.timer.LootDropTimer;
import java.time.ZonedDateTime;
import java.util.Arrays;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The type Loot drop placeholder expansion.
 */
public class LootDropPlaceholderExpansion extends PlaceholderExpansion {

  @Override
  public @NotNull String getAuthor() {
    return "Ammo";
  }

  @Override
  public @NotNull String getIdentifier() {
    return "lootdrop";
  }

  @Override
  public @NotNull String getVersion() {
    return "1.0.0";
  }

  @Override
  public boolean persist() {
    return true;
  }

  @Override
  public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
    if (player == null) {
      return null;
    }

    String[] paramsArray = params.split("_");

    if (paramsArray.length == 0) {
      return null;
    }

    if (params.equalsIgnoreCase("unique_joins")) {
      return String.format("%d", Bukkit.getOfflinePlayers().length);
    } else if (params.equalsIgnoreCase("current_players")) {
      return String.format("%d", Bukkit.getOnlinePlayers().size());
    } else if (paramsArray[0].equalsIgnoreCase("name") && paramsArray.length >= 2) {
      return Arrays.stream(Arrays.copyOfRange(paramsArray, 1, paramsArray.length))
          .reduce((a, b) -> a + "_" + b)
          .orElse("");
    } else if (params.equalsIgnoreCase("vote_timer")) {
      Duel runningDuel = DuelManager.INSTANCE.getRunningDuel();

      if (runningDuel == null || !runningDuel.isVoteOpen()) {
        return "Kein Vote";
      }

      DuelVoteTimer timer = runningDuel.getVoteTimer();

      if (timer == null) {
        return "Kein Timer";
      }

      return String.format(
          "%02d:%02d:%02d", timer.getCurrentSeconds() / 3600,
          (timer.getCurrentSeconds() % 3600) / 60,
          timer.getCurrentSeconds() % 60
      );
    } else if (params.equalsIgnoreCase("vote_player_one_name")) {
      Duel runningDuel = DuelManager.INSTANCE.getRunningDuel();

      if (runningDuel == null || !runningDuel.isVoteOpen()) {
        return "Kein Vote";
      }

      return runningDuel.getPlayerOne().getName();
    } else if (params.equalsIgnoreCase("vote_player_two_name")) {
      Duel runningDuel = DuelManager.INSTANCE.getRunningDuel();

      if (runningDuel == null || !runningDuel.isVoteOpen()) {
        return "Kein Vote";
      }

      return runningDuel.getPlayerTwo().getName();
    } else if (params.equalsIgnoreCase("vote_player_one_votes")) {
      Duel runningDuel = DuelManager.INSTANCE.getRunningDuel();

      if (runningDuel == null || !runningDuel.isVoteOpen()) {
        return "Kein Vote";
      }

      return String.valueOf(runningDuel.getVotes(runningDuel.getPlayerOne()));
    } else if (params.equalsIgnoreCase("vote_player_two_votes")) {
      Duel runningDuel = DuelManager.INSTANCE.getRunningDuel();

      if (runningDuel == null || !runningDuel.isVoteOpen()) {
        return "Kein Vote";
      }

      return String.valueOf(runningDuel.getVotes(runningDuel.getPlayerTwo()));
    } else if (params.equalsIgnoreCase("vote_player_one_percentage")) {
      Duel runningDuel = DuelManager.INSTANCE.getRunningDuel();

      if (runningDuel == null || !runningDuel.isVoteOpen()) {
        return "Kein Vote";
      }

      return String.valueOf(runningDuel.getVotesPercentage(runningDuel.getPlayerOne()));
    } else if (params.equalsIgnoreCase("vote_player_two_percentage")) {
      Duel runningDuel = DuelManager.INSTANCE.getRunningDuel();

      if (runningDuel == null || !runningDuel.isVoteOpen()) {
        return "Kein Vote";
      }

      return String.valueOf(runningDuel.getVotesPercentage(runningDuel.getPlayerTwo()));
    } else if (params.equalsIgnoreCase("timer")) {
      LootDropTimer timer = LootDropConfig.INSTANCE.getTimer();

      if (timer == null) {
        return "00:00:00";
      }

      if (LootDropConfig.INSTANCE.getEndTimestamp().isBefore(ZonedDateTime.now())) {
        return "00:00:00";
      }

      return String.format(
          "%02d:%02d:%02d", timer.getSeconds() / 3600, (timer.getSeconds() % 3600) / 60,
          timer.getSeconds() % 60
      );
    }

    return null;
  }
}
