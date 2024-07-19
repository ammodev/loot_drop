package de.castcrafter.lootdrop.duel;

import java.time.Duration;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DuelVotesDisplay implements DuelVoteTimer.SubTimer {

  public static final DuelVotesDisplay INSTANCE = new DuelVotesDisplay();

  private DuelVotesDisplay() {
  }

  @Override
  public void run(Duel duel, int currentSeconds) {
    final Player playerOne = duel.getPlayerOne();
    final Player playerTwo = duel.getPlayerTwo();

    final int playerOneVotes = duel.getVotes(playerOne);
    final int playerTwoVotes = duel.getVotes(playerTwo);

    final TextColor playerOneColor = getColor(playerOneVotes, playerTwoVotes);
    final TextColor playerTwoColor = getColor(playerTwoVotes, playerOneVotes);

    final TextComponent.Builder text = Component.text();

    text.append(Component.text(playerOne.getName(), NamedTextColor.GRAY));
    text.appendSpace();
    text.append(Component.text(playerOneVotes, playerOneColor));
    text.appendSpace();
    text.append(Component.text("-", NamedTextColor.DARK_GRAY));
    text.appendSpace();
    text.append(Component.text(playerTwoVotes, playerTwoColor));
    text.appendSpace();
    text.append(Component.text(playerTwo.getName(), NamedTextColor.GRAY));

    final Title title = Title.title(Component.empty(), text.build(),
        Times.times(Duration.ZERO, Duration.ofSeconds(2), Duration.ZERO));

    Bukkit.getOnlinePlayers().forEach(p -> p.showTitle(title));
  }

  private TextColor getColor(int votes, int votesOther) {
    if (votes == votesOther) {
      return NamedTextColor.YELLOW;
    } else if (votes > votesOther) {
      return NamedTextColor.GREEN;
    } else {
      return NamedTextColor.RED;
    }
  }
}
