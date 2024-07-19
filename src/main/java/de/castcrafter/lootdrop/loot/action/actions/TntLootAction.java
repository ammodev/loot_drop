package de.castcrafter.lootdrop.loot.action.actions;

import de.castcrafter.lootdrop.loot.action.LootAction;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.TNTPrimed;

/**
 * The type Tnt loot action.
 */
public class TntLootAction extends LootAction {

  public static final TntLootAction INSTANCE = new TntLootAction();

  /**
   * Instantiates a new Tnt loot action.
   */
  public TntLootAction() {
    super(Material.TNT);
  }

  @Override
  public void performAction(Location initialLocation, Location targetLocation) {
    targetLocation.getWorld().spawn(targetLocation, TNTPrimed.class, tntPrimed -> {
      tntPrimed.setFuseTicks(0);
    });
  }
}
