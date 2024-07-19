package de.castcrafter.lootdrop.duel;

/**
 * The type Duel manager.
 */
public class DuelManager {

  public static final DuelManager INSTANCE = new DuelManager();

  private Duel runningDuel;

  /**
   * Instantiates a new Duel manager.
   */
  private DuelManager() {
  }

  /**
   * Gets running duel.
   *
   * @return the running duel
   */
  public Duel getRunningDuel() {
    return runningDuel;
  }

  /**
   * Sets running duel.
   *
   * @param runningDuel the running duel
   */
  public void setRunningDuel(Duel runningDuel) {
    this.runningDuel = runningDuel;
  }
}
