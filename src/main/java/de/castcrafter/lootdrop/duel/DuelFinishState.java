package de.castcrafter.lootdrop.duel;

/**
 * The enum Duel finish state.
 */
public enum DuelFinishState {

  FORCE_STOP,
  DRAW,
  PLAYER_ONE_WON,
  PLAYER_TWO_WON,
  PLAYER_LEFT,
  NOBODY_WON;

  /**
   * Is won boolean.
   *
   * @return the boolean
   */
  public boolean isWon() {
    return this == PLAYER_ONE_WON || this == PLAYER_TWO_WON || this == NOBODY_WON;
  }

  /**
   * Is draw boolean.
   *
   * @return the boolean
   */
  public boolean isDraw() {
    return this == DRAW;
  }

  /**
   * Is force stop boolean.
   *
   * @return the boolean
   */
  public boolean isForceStop() {
    return this == FORCE_STOP;
  }

  /**
   * Is nobody won boolean.
   *
   * @return the boolean
   */
  public boolean isNobodyWon() {
    return this == NOBODY_WON;
  }

  /**
   * Is player one won boolean.
   *
   * @return the boolean
   */
  public boolean isPlayerOneWon() {
    return this == PLAYER_ONE_WON;
  }

  /**
   * Is player two won boolean.
   *
   * @return the boolean
   */
  public boolean isPlayerTwoWon() {
    return this == PLAYER_TWO_WON;
  }

  /**
   * Is player left boolean.
   *
   * @return the boolean
   */
  public boolean isPlayerLeft() {
    return this == PLAYER_LEFT;
  }
}
