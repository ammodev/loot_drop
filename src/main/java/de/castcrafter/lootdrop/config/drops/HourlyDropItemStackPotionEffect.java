package de.castcrafter.lootdrop.config.drops;

import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

/**
 * The type Hourly drop item stack potion effect.
 */
@ConfigSerializable
@SuppressWarnings("FieldMayBeFinal")
public class HourlyDropItemStackPotionEffect {

  private static final ComponentLogger LOGGER = ComponentLogger.logger(
      HourlyDropItemStackPotionEffect.class);

  private String potionEffectNamespace = null;
  private String potionEffect;
  private int duration;
  private int amplifier;
  private boolean ambient = false;
  private boolean particles = false;
  private boolean icon = true;

  /**
   * Instantiates a new Hourly drop item stack potion effect.
   */
  public HourlyDropItemStackPotionEffect() {
  }

  /**
   * Instantiates a new Hourly drop item stack potion effect.
   *
   * @param potionEffect the potion effect
   * @param duration     the duration
   * @param amplifier    the amplifier
   * @param ambient      the ambient
   * @param particles    the particles
   * @param icon         the icon
   */
  public HourlyDropItemStackPotionEffect(
      String potionEffect, int duration, int amplifier, boolean ambient, boolean particles,
      boolean icon
  ) {
    this.potionEffect = potionEffect;
    this.duration = duration;
    this.amplifier = amplifier;
    this.ambient = ambient;
    this.particles = particles;
    this.icon = icon;
  }

  /**
   * Gets potion effect.
   *
   * @return the potion effect
   */
  public PotionEffect getPotionEffect() {
    return new PotionEffect(getPotionEffectType(), duration, amplifier, ambient, particles, icon);
  }

  /**
   * Gets potion effect type.
   *
   * @return the potion effect type
   */
  public PotionEffectType getPotionEffectType() {
    String namespace = potionEffectNamespace;

    if (namespace == null) {
      namespace = "minecraft";
    }

    return Registry.POTION_EFFECT_TYPE.get(new NamespacedKey(namespace, potionEffect));
  }

  /**
   * Gets duration.
   *
   * @return the duration
   */
  public int getDuration() {
    return duration;
  }

  /**
   * Gets amplifier.
   *
   * @return the amplifier
   */
  public int getAmplifier() {
    return amplifier;
  }

  /**
   * Is ambient boolean.
   *
   * @return the boolean
   */
  public boolean isAmbient() {
    return ambient;
  }

  /**
   * Is particles boolean.
   *
   * @return the boolean
   */
  public boolean isParticles() {
    return particles;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
