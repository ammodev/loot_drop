package de.castcrafter.lootdrop.loot.action;

import org.bukkit.Location;
import org.bukkit.Material;

/**
 * The type Loot action.
 */
public abstract class LootAction {

  private final Material executingMaterial;

  /**
   * Instantiates a new Loot action.
   *
   * @param executingMaterial the executing material
   */
  public LootAction(Material executingMaterial) {
    this.executingMaterial = executingMaterial;
  }

  /**
   * Perform action internal.
   *
   * @param initialLocation the initial location
   * @param targetLocation  the target location
   * @param amount          the amount
   */
  public void performActionInternal(Location initialLocation, Location targetLocation, int amount) {
    for (int i = 0; i < amount; i++) {
      performAction(initialLocation, targetLocation);
    }
  }

  /**
   * Perform action.
   *
   * @param initialLocation the initial location
   * @param targetLocation  the target location
   */
  public abstract void performAction(Location initialLocation, Location targetLocation);

  /**
   * Gets executing material.
   *
   * @return the executing material
   */
  public Material getExecutingMaterial() {
    return executingMaterial;
  }
}
