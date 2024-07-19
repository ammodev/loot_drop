package de.castcrafter.lootdrop.utils;

import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;

/**
 * The type Sound utils.
 */
public class SoundUtils {

  /**
   * Play sound.
   *
   * @param player the player
   * @param sound  the sound
   * @param volume the volume
   * @param pitch  the pitch
   */
  public static void playSound(HumanEntity player, Sound sound, float volume, float pitch) {
    player.playSound(
        net.kyori.adventure.sound.Sound.sound().type(sound).volume(volume).pitch(pitch).build(),
        net.kyori.adventure.sound.Sound.Emitter.self()
    );
  }
}
