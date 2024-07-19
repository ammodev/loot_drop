package de.castcrafter.lootdrop.loot.action;

import de.castcrafter.lootdrop.loot.action.actions.TntLootAction;

/**
 * The enum Loot actions.
 */
public enum LootActions {

  TNT(TntLootAction.INSTANCE);

  private final LootAction action;

  /**
   * Instantiates a new Loot actions.
   *
   * @param action the action
   */
  LootActions(LootAction action) {
    this.action = action;
  }

  /**
   * Gets action.
   *
   * @return the action
   */
  public LootAction getAction() {
    return action;
  }
}
