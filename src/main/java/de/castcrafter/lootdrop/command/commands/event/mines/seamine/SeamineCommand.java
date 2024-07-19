package de.castcrafter.lootdrop.command.commands.event.mines.seamine;

import static org.bukkit.inventory.EquipmentSlot.CHEST;
import static org.bukkit.inventory.EquipmentSlot.FEET;
import static org.bukkit.inventory.EquipmentSlot.HAND;
import static org.bukkit.inventory.EquipmentSlot.HEAD;
import static org.bukkit.inventory.EquipmentSlot.LEGS;
import static org.bukkit.inventory.EquipmentSlot.OFF_HAND;

import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * The type Seamine command.
 */
public class SeamineCommand extends CommandAPICommand {

  /**
   * Instantiates a new Seamine command.
   *
   * @param commandName the command name
   */
  public SeamineCommand(String commandName) {
    super(commandName);

    withPermission("lootdrop.command.mines.seamine");

    executesPlayer((player, args) -> {
      summonMine(player.getLocation());
    });
  }

  /**
   * Summon mine boolean.
   *
   * @param location the location
   * @return the boolean
   */
  private boolean summonMine(Location location) {
    // Round the coordinates to the nearest block
    System.out.println("Summoning mine at: " + location);
    double x = Math.floor(location.getX()) + 0.5;
    double y = Math.floor(location.getY());
    double z = Math.floor(location.getZ()) + 0.5;
    Location roundedLocation = new Location(location.getWorld(), x, y, z);

    ItemStack heartOfTheSea = new ItemStack(Material.HEART_OF_THE_SEA);
    ItemMeta meta = heartOfTheSea.getItemMeta();
    if (meta != null) {
      meta.setCustomModelData(2);
      heartOfTheSea.setItemMeta(meta);
    }

    ArmorStand armorStand = roundedLocation.getWorld().spawn(roundedLocation, ArmorStand.class);
    armorStand.setInvisible(true);
    armorStand.setGravity(false);
    armorStand.addScoreboardTag("seamine");
    armorStand.setDisabledSlots(HEAD, CHEST, FEET, HAND, OFF_HAND, LEGS);
    armorStand.getEquipment().setHelmet(heartOfTheSea);

    // Create chains from the armor stand down to the first solid block
    Location chainLocation = armorStand.getLocation().clone();
    chainLocation.setX(Math.floor(chainLocation.getX()));
    chainLocation.setZ(Math.floor(chainLocation.getZ()));
    while (!chainLocation.getBlock().getType().isSolid()) {
      chainLocation.getBlock().setType(Material.CHAIN);
      chainLocation.subtract(0, 1, 0);
    }

    return true;
  }
}
