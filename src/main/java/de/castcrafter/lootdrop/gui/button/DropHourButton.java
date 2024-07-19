package de.castcrafter.lootdrop.gui.button;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import de.castcrafter.lootdrop.config.LootDropConfig;
import de.castcrafter.lootdrop.config.drops.HourlyDrop;
import de.castcrafter.lootdrop.gui.drops.DropGui;
import de.castcrafter.lootdrop.gui.drops.DropsGui;
import de.castcrafter.lootdrop.utils.Chat;
import de.castcrafter.lootdrop.utils.SoundUtils;
import io.th0rgal.oraxen.api.OraxenItems;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * The type Drop hour button.
 */
public class DropHourButton extends GuiItem {

  private static final ComponentLogger LOGGER = ComponentLogger.logger(DropHourButton.class);

  /**
   * Form item stack item stack.
   *
   * @param hourlyDrop the hourly drop
   * @return the item stack
   */
  private static ItemStack formItemStack(Player forPlayer, HourlyDrop hourlyDrop) {
    int currentHourSinceStart =
        DropsGui.getCurrentHourSinceStart(LootDropConfig.INSTANCE.getStartedTimestamp(),
            ZonedDateTime.now());
    boolean playerOpenedDrop = !hourlyDrop.canPlayerUse(forPlayer.getUniqueId());

    String oraxenName;
    if (currentHourSinceStart < hourlyDrop.getHour()) {
      oraxenName = "icon_daily_gift_unclaimed";
    } else {
      oraxenName = "icon_daily_gift_ready";
    }

    if (playerOpenedDrop) {
      oraxenName = "icon_daily_gift_claimed";
    }

    int hour = hourlyDrop.getHour();

    ItemStack itemStack = OraxenItems.getItemById(oraxenName).build().clone();
    ItemMeta itemMeta = itemStack.getItemMeta();

    if (itemMeta == null) {
      LOGGER.error("ItemMeta is null for hour " + hour);
      return itemStack;
    }

    itemMeta.displayName(
        Component.text("Stunde " + hour,
            playerOpenedDrop ? NamedTextColor.RED : NamedTextColor.GREEN,
            TextDecoration.BOLD,
            TextDecoration.UNDERLINED
        ).decoration(TextDecoration.ITALIC, false));

    List<Component> loreList = new ArrayList<>();
    loreList.add(Component.empty());

    if (playerOpenedDrop) {
      loreList.add(Component.text("Du hast diese Stunde bereits geöffnet", NamedTextColor.RED));
    } else {
      loreList.add(
          Component.text("Klicke hier, um", NamedTextColor.GRAY));
      loreList.add(
          Component.text("die Belohnungen für", NamedTextColor.GRAY));
      loreList.add(
          Component.text("Stunde " + hour + " zu öffnen", NamedTextColor.GRAY));
    }

    itemMeta.lore(
        loreList.stream().map(component -> component.decoration(TextDecoration.ITALIC, false))
            .toList());
    itemStack.setItemMeta(itemMeta);

    return itemStack;
  }

  /**
   * Instantiates a new Drop hour button.
   *
   * @param hourlyDrop the hourly drop
   */
  public DropHourButton(DropsGui dropsGui, Player forPlayer, HourlyDrop hourlyDrop) {
    super(formItemStack(forPlayer, hourlyDrop), event -> {
          int currentHour = DropsGui.getCurrentHourSinceStart(
              LootDropConfig.INSTANCE.getStartedTimestamp(),
              ZonedDateTime.now()
          );

          HumanEntity humanEntity = event.getWhoClicked();

          if (currentHour < hourlyDrop.getHour()) {
            Chat.sendMessage(humanEntity, Component.text(
                "Diese Stunde ist noch nicht erreicht",
                NamedTextColor.RED
            ));
            SoundUtils.playSound(humanEntity, Sound.BLOCK_NOTE_BLOCK_BASS, .5f, 1f);
            return;
          }

          if (!hourlyDrop.canPlayerUse(humanEntity.getUniqueId())) {
            Chat.sendMessage(
                humanEntity,
                Component.text("Du hast diese Stunde bereits geöffnet", NamedTextColor.RED));

            SoundUtils.playSound(humanEntity, Sound.BLOCK_NOTE_BLOCK_BASS, .5f, 1f);
            return;
          }

          new DropGui(dropsGui, humanEntity, hourlyDrop).show(humanEntity);
        }
    );
  }
}
