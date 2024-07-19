package de.castcrafter.lootdrop.meteor;

import de.castcrafter.lootdrop.Main;
import io.th0rgal.oraxen.api.OraxenItems;
import java.util.UUID;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * The type Meteor.
 */
public class Meteor implements Listener {

  public static final double METEOR_FOLLOW_VELOCITY = .1;
  public static final double METEOR_SWIRL_VELOCITY = .25;

  public static final int FOLLOW_DISTANCE_START = 30;
  public static final int FOLLOW_DISTANCE_END = 40;

  public static final int EXPLOSION_DISTANCE = 2;
  public static final int EXPLOSION_DAMAGE = 8;
  public static final int SWIRL_EXPLOSION_DISTANCE = 20;

  public static final NamespacedKey METEOR_KEY = new NamespacedKey("lootdrop", "meteor");
  public static final NamespacedKey METEOR_SWIRL_KEY = new NamespacedKey("lootdrop",
      "meteor_should_swirl");
  public static final NamespacedKey METEOR_IS_SWIRL_KEY = new NamespacedKey("lootdrop",
      "meteor_is_swirl");
  public static final NamespacedKey METEOR_TARGET_KEY = new NamespacedKey("lootdrop",
      "meteor_target");

  public static final ItemStack METEOR_ITEM_STACK = OraxenItems.getItemById("meteor").build();
  public static final ItemStack METEOR_SWIRL_ITEM_STACK = OraxenItems.getItemById("meteor_swirl")
      .build();

  private final boolean shouldSwirl;
  private boolean didSwirl = false;

  private final boolean isSwirlMeteor;

  private final Location meteorSpawnLocation;
  private ArmorStand meteorHolder;

  private Entity currentTarget;
  private BukkitRunnable runnable;

  /**
   * Instantiates a new Meteor.
   *
   * @param armorStand the armor stand
   */
  public Meteor(ArmorStand armorStand) {
    this.meteorHolder = armorStand;
    this.shouldSwirl = meteorHolder.getPersistentDataContainer()
        .getOrDefault(METEOR_SWIRL_KEY, PersistentDataType.BOOLEAN, false);
    this.isSwirlMeteor = meteorHolder.getPersistentDataContainer()
        .getOrDefault(METEOR_IS_SWIRL_KEY, PersistentDataType.BOOLEAN, false);
    this.meteorSpawnLocation = armorStand.getLocation();

    String targetUuid = meteorHolder.getPersistentDataContainer()
        .get(METEOR_TARGET_KEY, PersistentDataType.STRING);
    if (targetUuid != null) {
      this.currentTarget = Bukkit.getPlayer(UUID.fromString(targetUuid));

      if (this.currentTarget == null) {
        despawnHolder();
        unregister();
      }
    }
  }

  /**
   * Instantiates a new Meteor.
   *
   * @param meteorLocation the meteor location
   * @param swirl          the swirl
   * @param isSwirlMeteor  the is swirl meteor
   */
  public Meteor(Location meteorLocation, boolean swirl, boolean isSwirlMeteor) {
    this.meteorSpawnLocation = meteorLocation;
    this.shouldSwirl = swirl;
    this.isSwirlMeteor = isSwirlMeteor;
  }

  /**
   * Create runnable.
   */
  public void createRunnable() {
    this.runnable = new BukkitRunnable() {
      @Override
      public void run() {
        tick();
      }
    };

    this.runnable.runTaskTimer(Main.getInstance(), 0, 1L);
  }

  /**
   * Stop runnable.
   */
  public void stopRunnable() {
    if (this.runnable != null) {
      this.runnable.cancel();
    }
  }

  /**
   * Register.
   */
  public void register() {
    MeteorRegistry.INSTANCE.addMeteor(this);
    createRunnable();

    Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
  }

  /**
   * Unregister.
   */
  public void unregister() {
    MeteorRegistry.INSTANCE.removeMeteor(this);
    stopRunnable();

    HandlerList.unregisterAll(this);
  }

  /**
   * Spawn holder.
   */
  public void spawnHolder() {
    if (meteorHolder != null) {
      meteorHolder.remove();
    }

    ItemStack itemStack = isSwirlMeteor ? METEOR_SWIRL_ITEM_STACK : METEOR_ITEM_STACK;

    meteorHolder = meteorSpawnLocation.getWorld()
        .spawn(meteorSpawnLocation, ArmorStand.class, armorStand -> {
          armorStand.setBasePlate(false);
          armorStand.setGravity(false);
          armorStand.setVisible(false);

          armorStand.getPersistentDataContainer().set(METEOR_KEY, PersistentDataType.BOOLEAN, true);
          armorStand.getEquipment().setHelmet(itemStack);
          armorStand.setDisabledSlots(EquipmentSlot.CHEST, EquipmentSlot.FEET, EquipmentSlot.HAND,
              EquipmentSlot.HEAD, EquipmentSlot.LEGS, EquipmentSlot.OFF_HAND
          );
        });
  }

  /**
   * Despawn holder.
   */
  public void despawnHolder() {
    if (meteorHolder == null) {
      return;
    }

    meteorHolder.remove();
  }

  /**
   * Tick.
   */
  public void tick() {
    if (isSwirlMeteor && currentTarget != null) {
      moveTowardsTarget();
      return;
    }

    if (!checkTargetRange()) {
      currentTarget = null;
    }

    if (currentTarget == null) {
      setNewTarget();
    }

    moveTowardsTarget();
  }

  /**
   * On quit.
   *
   * @param event the event
   */
  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    checkAndDespawnOnEvent(event.getPlayer());
  }

  /**
   * On world switch.
   *
   * @param event the event
   */
  @EventHandler
  public void onWorldSwitch(PlayerChangedWorldEvent event) {
    checkAndDespawnOnEvent(event.getPlayer());
  }

  /**
   * On death.
   *
   * @param event the event
   */
  @EventHandler
  public void onDeath(PlayerDeathEvent event) {
    checkAndDespawnOnEvent(event.getEntity());
  }

  /**
   * Check and despawn on event.
   *
   * @param player the player
   */
  private void checkAndDespawnOnEvent(Player player) {
    if (currentTarget != null && currentTarget.equals(player)) {
      despawnHolder();
      unregister();
    }
  }

  /**
   * Sets new target.
   */
  private void setNewTarget() {
    for (Entity entity : meteorHolder.getNearbyEntities(
        FOLLOW_DISTANCE_START, FOLLOW_DISTANCE_START, FOLLOW_DISTANCE_START)) {
      if (entity instanceof Player player && player.isOnline() &&
          (player.getGameMode().equals(GameMode.SURVIVAL) ||
              player.getGameMode().equals(GameMode.ADVENTURE))) {

        setCurrentTarget(player);
        return;
      }
    }
  }

  /**
   * Move towards.
   */
  private void moveTowardsTarget() {
    if (currentTarget == null) {
      return;
    }

    if (shouldSwirl && !didSwirl && checkSwirlExplosionDistanceToTarget()) {
      didSwirl = true;

      Location currentLocation = meteorHolder.getLocation().clone();

      meteorHolder.getWorld()
          .spawnParticle(Particle.EXPLOSION_LARGE, meteorHolder.getLocation(), 1);
      for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
        onlinePlayer.playSound(Sound.sound().type(org.bukkit.Sound.ENTITY_GENERIC_EXPLODE).build());
      }

      despawnHolder();
      unregister();

      for (Entity entity : meteorHolder.getNearbyEntities(
          SWIRL_EXPLOSION_DISTANCE, SWIRL_EXPLOSION_DISTANCE, SWIRL_EXPLOSION_DISTANCE)) {
        if (!(entity instanceof LivingEntity livingEntity)) {
          continue;
        }

        if (livingEntity instanceof ArmorStand) {
          continue;
        }

        if (!(livingEntity instanceof Player)) {
//					continue;
        }

        Meteor meteor = new Meteor(currentLocation, false, true);
        meteor.spawnHolder();
        meteor.setCurrentTarget(entity);
        meteor.register();
      }

      return;
    }

    if (!shouldSwirl && checkExplosionDistanceToTarget()) {
      for (Entity entity : meteorHolder.getNearbyEntities(
          EXPLOSION_DISTANCE, EXPLOSION_DISTANCE, EXPLOSION_DISTANCE)) {
        if (!(entity instanceof LivingEntity livingEntity)) {
          continue;
        }

        if (livingEntity instanceof ArmorStand) {
          continue;
        }

        if (!(livingEntity instanceof Player)) {
//					continue;
        }

        livingEntity.damage(EXPLOSION_DAMAGE);
      }

      meteorHolder.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, meteorHolder.getLocation(), 1);
      for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
        onlinePlayer.playSound(Sound.sound().type(org.bukkit.Sound.ENTITY_GENERIC_EXPLODE).build());
      }

      despawnHolder();
      unregister();

      return;
    }

    double velocity = shouldSwirl ? METEOR_SWIRL_VELOCITY : METEOR_FOLLOW_VELOCITY;

    meteorHolder.teleportAsync(meteorHolder.getLocation().clone().add(
        new Vector(
            (currentTarget.getLocation().getX() - meteorHolder.getLocation().getX()) *
                velocity,
            (currentTarget.getLocation().getY() - meteorHolder.getLocation().getY()) *
                velocity,
            (currentTarget.getLocation().getZ() - meteorHolder.getLocation().getZ()) *
                velocity
        )
    ));

    int setback = 5;
    Location locationMinusOneForce = meteorHolder.getLocation().clone().subtract(
        new Vector(
            (currentTarget.getLocation().getX() - meteorHolder.getLocation().getX()) *
                (setback * velocity),
            (currentTarget.getLocation().getY() - meteorHolder.getLocation().getY()) *
                (setback * velocity),
            (currentTarget.getLocation().getZ() - meteorHolder.getLocation().getZ()) *
                (setback * velocity)
        )
    );
    Particle.FLAME.builder().count(0).location(locationMinusOneForce).receivers(30).spawn();
  }

  /**
   * Check swirl explosion distance to target boolean.
   *
   * @return the boolean
   */
  private boolean checkSwirlExplosionDistanceToTarget() {
    return currentTarget.getWorld().equals(meteorHolder.getWorld()) &&
        meteorHolder.getLocation().distance(currentTarget.getLocation())
            <= SWIRL_EXPLOSION_DISTANCE;
  }

  /**
   * Check explosion distance to target boolean.
   *
   * @return the boolean
   */
  private boolean checkExplosionDistanceToTarget() {
    return currentTarget.getWorld().equals(meteorHolder.getWorld()) &&
        meteorHolder.getLocation().distance(currentTarget.getLocation()) <= EXPLOSION_DISTANCE;
  }

  /**
   * Check target range boolean.
   *
   * @return the boolean
   */
  private boolean checkTargetRange() {
    if (currentTarget instanceof Player player) {
      if (!player.isOnline() || !(player.getGameMode().equals(GameMode.SURVIVAL) ||
          player.getGameMode().equals(GameMode.ADVENTURE))) {
        return false;
      }
    }

    return currentTarget != null && currentTarget.getWorld().equals(meteorHolder.getWorld()) &&
        meteorHolder.getLocation().distance(currentTarget.getLocation()) <= FOLLOW_DISTANCE_END;
  }

  /**
   * Gets meteor holder.
   *
   * @return the meteor holder
   */
  public ArmorStand getMeteorHolder() {
    return meteorHolder;
  }

  /**
   * Is swirl boolean.
   *
   * @return the boolean
   */
  public boolean isShouldSwirl() {
    return shouldSwirl;
  }

  /**
   * Gets meteor spawn location.
   *
   * @return the meteor spawn location
   */
  public Location getMeteorSpawnLocation() {
    return meteorSpawnLocation;
  }

  /**
   * Gets current target.
   *
   * @return the current target
   */
  public Entity getCurrentTarget() {
    return currentTarget;
  }

  /**
   * Gets runnable.
   *
   * @return the runnable
   */
  public BukkitRunnable getRunnable() {
    return runnable;
  }

  /**
   * Sets current target.
   *
   * @param currentTarget the current target
   */
  public void setCurrentTarget(Entity currentTarget) {
    this.currentTarget = currentTarget;

    meteorHolder.getPersistentDataContainer().set(METEOR_TARGET_KEY, PersistentDataType.STRING,
        currentTarget.getUniqueId().toString()
    );
  }

  /**
   * Is did swirl boolean.
   *
   * @return the boolean
   */
  public boolean isDidSwirl() {
    return didSwirl;
  }

  /**
   * Is swirl meteor boolean.
   *
   * @return the boolean
   */
  public boolean isSwirlMeteor() {
    return isSwirlMeteor;
  }
}
