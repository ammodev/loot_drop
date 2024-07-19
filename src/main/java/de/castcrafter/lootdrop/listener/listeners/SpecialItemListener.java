package de.castcrafter.lootdrop.listener.listeners;

import de.castcrafter.lootdrop.utils.Chat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * The type Special item listener.
 */
public class SpecialItemListener implements Listener {

  /**
   * The serializer for plain text components.
   */
  public static final PlainTextComponentSerializer PLAIN_TEXT_COMPONENT_SERIALIZER =
      PlainTextComponentSerializer.plainText();

  public static final NamespacedKey SPECIAL_KEY = new NamespacedKey("lootdrop", "special_item");

  public static final NamespacedKey ANNOUNCED_KEY = new NamespacedKey("lootdrop",
      "special_item_announced");

  public static final NamespacedKey SPECIAL_KEY_FINDER = new NamespacedKey("lootdrop",
      "special_item_finder");

  public static final NamespacedKey SPECIAL_KEY_TIME = new NamespacedKey("lootdrop",
      "special_item_time");

  /**
   * Listens for special item pickups.
   *
   * @param event The event
   */
  @EventHandler
  public void onItemPickup(@NotNull EntityPickupItemEvent event) {
    if (!(event.getEntity() instanceof Player player)) {
      return;
    }

    final ItemStack itemStack = event.getItem().getItemStack();
    processItem(itemStack, player);
  }

  /**
   * Listens for special item clicks.
   *
   * @param event The event
   */
  @EventHandler
  public void onItemClick(@NotNull InventoryClickEvent event) {
    final ItemStack itemStack = event.getCurrentItem();

    if (!(event.getWhoClicked() instanceof Player player) || itemStack == null) {
      return;
    }

    final String inventoryName = PLAIN_TEXT_COMPONENT_SERIALIZER.serialize(event.getView().title());
    final String defaultName =
        PLAIN_TEXT_COMPONENT_SERIALIZER.serialize(
            event.getView().getTopInventory().getType().defaultTitle());

    if (!(inventoryName.equalsIgnoreCase(defaultName))) {
      return;
    }

    if (checkSpecial(itemStack).isNotSpecial()) {
      return;
    }

    processItem(itemStack, player);
  }

  /**
   * Listens for special item pickups.
   *
   * @param event The event
   */
  @EventHandler
  public void onAnvilUse(@NotNull PrepareAnvilEvent event) {
    final AnvilInventory inventory = event.getInventory();
    final ItemStack itemStack = inventory.getSecondItem();

    if (itemStack == null) {
      return;
    }

    if (checkSpecial(itemStack).isNotSpecial()) {
      return;
    }

    final ItemStack firstItem = inventory.getFirstItem();
    if (inventory.getResult() == null
        || firstItem != null && firstItem.getType() != Material.ENCHANTED_BOOK) {
      return;
    }

    final ItemStack resultItem = inventory.getResult();
    resultItem.editMeta(EnchantmentStorageMeta.class, meta -> {
      final PersistentDataContainer data = meta.getPersistentDataContainer();

      data.set(SPECIAL_KEY, PersistentDataType.BYTE, (byte) 1);
      data.set(ANNOUNCED_KEY, PersistentDataType.BYTE, (byte) 1);
    });

    event.setResult(resultItem);
  }

  /**
   * Listens for special item pickups.
   *
   * @param event The event
   */
  @EventHandler
  private void onHopperPickup(@NotNull InventoryPickupItemEvent event) {
    final ItemStack itemStack = event.getItem().getItemStack();
    final ItemMeta itemMeta = itemStack.getItemMeta();
    final PersistentDataContainer data = itemMeta.getPersistentDataContainer();

    if (checkSpecial(itemStack).isNotSpecial()) {
      return;
    }

    if (data.has(SPECIAL_KEY) || data.has(ANNOUNCED_KEY)) {
      return;
    }

    event.setCancelled(true);
  }

  /**
   * Checks if the given item is special.
   *
   * @param itemStack The item to check
   * @return The state of the item
   */
  public SpecialState checkSpecial(ItemStack itemStack) {
    if (itemStack == null) {
      return SpecialState.NOT_SPECIAL;
    }

    final ItemMeta itemMeta = itemStack.getItemMeta();
    if (itemMeta == null) {
      return SpecialState.NOT_SPECIAL;
    }

    final PersistentDataContainer data = itemMeta.getPersistentDataContainer();

    if (!data.has(SPECIAL_KEY)) {
      return SpecialState.NOT_SPECIAL;
    }

    if (data.has(ANNOUNCED_KEY)) {
      return SpecialState.SPECIAL_ANNOUNCED;
    }

    return SpecialState.SPECIAL_UNANNOUNCED;
  }

  /**
   * Processes the given item.
   *
   * @param itemStack The item to process
   * @param player    The player who has done something with the item
   */
  public void processItem(ItemStack itemStack, @NotNull Player player) {
    if (player.getGameMode().equals(GameMode.CREATIVE) || player.getGameMode()
        .equals(GameMode.SPECTATOR)) {
      return;
    }

    final ItemMeta meta = itemStack.getItemMeta();

    if (meta == null) {
      return;
    }

    final PersistentDataContainer data = meta.getPersistentDataContainer();

    if (checkSpecial(itemStack).isNotSpecial()) {
      return;
    }

    data.set(SPECIAL_KEY, PersistentDataType.BYTE, (byte) 1);

    // Check if item was already announced
    if (data.has(ANNOUNCED_KEY)) {
      return;
    }

    data.set(ANNOUNCED_KEY, PersistentDataType.BYTE, (byte) 1);
    data.set(
        SPECIAL_KEY_FINDER, PersistentDataType.STRING,
        PLAIN_TEXT_COMPONENT_SERIALIZER.serialize(
            player.displayName().colorIfAbsent(NamedTextColor.YELLOW))
    );
    data.set(SPECIAL_KEY_TIME, PersistentDataType.LONG, System.currentTimeMillis());

    Component displayNameComponent = meta.displayName();

    if (displayNameComponent == null) {
      displayNameComponent = Component.text("");
    }

    String itemStackDisplayName = PLAIN_TEXT_COMPONENT_SERIALIZER.serialize(displayNameComponent);

    if (itemStackDisplayName.isEmpty()) {
      itemStackDisplayName = PLAIN_TEXT_COMPONENT_SERIALIZER.serialize(
          Component.translatable(itemStack.getType().translationKey()));
    }

    if (itemStackDisplayName.isEmpty()) {
      itemStackDisplayName = itemStack.getType().name();
    }

    ZonedDateTime time = ZonedDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    String formattedTime = time.format(formatter);

    List<Component> loreList =
        new ArrayList<>(meta.lore() != null ? Objects.requireNonNull(meta.lore()) : List.of());

    if (!loreList.isEmpty()) {
      loreList.add(Component.empty());
      loreList.add(Component.text("==========================", NamedTextColor.DARK_GRAY));
    }

    loreList.add(Component.text("Gefunden durch: ", NamedTextColor.GRAY)
        .append(player.displayName().colorIfAbsent(NamedTextColor.YELLOW)));
    loreList.add(Component.text("Gefunden am: ", NamedTextColor.GRAY)
        .append(Component.text(formattedTime, NamedTextColor.YELLOW)));

    loreList = loreList.stream()
        .map(component -> component.decoration(TextDecoration.ITALIC, false)).toList();

    meta.lore(loreList);

    try {
      itemStack.setItemMeta(meta);
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Broadcast
    sendMessage(Component.text(""));
    sendMessage(Component.text("-------------------------------", NamedTextColor.GRAY));
    sendMessage(Component.text(""));

    sendMessage(player.displayName().colorIfAbsent(NamedTextColor.GOLD)
        .append(Component.text(" hat ", NamedTextColor.GREEN))
        .append(Component.text(itemStackDisplayName, NamedTextColor.GOLD))
        .append(Component.text(" gefunden!", NamedTextColor.GREEN)));

    sendMessage(Component.text(""));
    sendMessage(Component.text("-------------------------------", NamedTextColor.GRAY));
    sendMessage(Component.text(""));

    for (Player online : Bukkit.getOnlinePlayers()) {
      online.playSound(
          Sound.sound().type(org.bukkit.Sound.ENTITY_ENDER_DRAGON_GROWL).volume(.35f).pitch(.75f)
              .build(),
          Sound.Emitter.self()
      );
    }
  }

  /**
   * Send message.
   *
   * @param component the component
   */
  private void sendMessage(Component component) {
    Bukkit.broadcast(prefix().append(component));
  }

  /**
   * Prefix component.
   *
   * @return the component
   */
  private Component prefix() {
    return Chat.prefix();
  }

  /**
   * Represents the state of a special item
   */
  public enum SpecialState {
    /**
     * The item is not special
     */
    NOT_SPECIAL,

    /**
     * The item is special and was already announced
     */
    SPECIAL_ANNOUNCED,

    /**
     * The item is special and was not announced yet
     */
    SPECIAL_UNANNOUNCED;

    /**
     * Checks if the item is special
     *
     * @return True if the item is special
     */
    public boolean isSpecial() {
      return this == SPECIAL_ANNOUNCED || this == SPECIAL_UNANNOUNCED;
    }

    /**
     * Checks if the item was already announced
     *
     * @return True if the item was already announced
     */
    public boolean isAnnounced() {
      return this == SPECIAL_ANNOUNCED;
    }

    /**
     * Checks if the item was not announced yet
     *
     * @return True if the item was not announced yet
     */
    public boolean isUnannounced() {
      return this == SPECIAL_UNANNOUNCED;
    }

    /**
     * Checks if the item is not special
     *
     * @return True if the item is not special
     */
    public boolean isNotSpecial() {
      return this == NOT_SPECIAL;
    }
  }

}
