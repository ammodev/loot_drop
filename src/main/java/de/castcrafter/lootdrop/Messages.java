package de.castcrafter.lootdrop;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * The type Messages.
 */
public class Messages {

  /**
   * No event started component component.
   *
   * @return the component
   */
  public static Component noEventStartedComponent() {
    return Component.text("Derzeit findet kein Event statt.", NamedTextColor.RED);
  }

  /**
   * No event created component component.
   *
   * @return the component
   */
  public static Component noEventCreatedComponent() {
    return Component.text("Es wurde noch kein Event erstellt.", NamedTextColor.RED);
  }

  /**
   * Only players can execute command component component.
   *
   * @return the component
   */
  public static Component onlyPlayersCanExecuteCommandComponent() {
    return Component.text("Nur Spieler können diesen Befehl ausführen.", NamedTextColor.RED);
  }

  /**
   * No permission component component.
   *
   * @return the component
   */
  public static Component noPermissionComponent() {
    return Component.text("Du besitzt keine Rechte, um diesen Befehl auszuführen.",
        NamedTextColor.RED);
  }

  /**
   * You have been teleported to event location component component.
   *
   * @return the component
   */
  public static Component youHaveBeenTeleportedToEventLocationComponent() {
    return Component.text("Du wurdest zum Event teleportiert.", NamedTextColor.GREEN);
  }

  /**
   * Event has been started component component.
   *
   * @return the component
   */
  public static Component eventHasBeenStartedComponent() {
    TextComponent.Builder builder = Component.text();

    builder.appendNewline();
    builder.append(Component.text("Ein neues Event wurde gestartet! ", NamedTextColor.GREEN));
    builder.append(Component.text("\u2320", NamedTextColor.GREEN)
        .clickEvent(ClickEvent.runCommand("/join"))
        .hoverEvent(Component.text("Klicke um am Event teilzunehmen.")));
    builder.appendNewline();

    return builder.build();
  }

  /**
   * Event has been created component component.
   *
   * @return the component
   */
  public static Component eventHasBeenCreatedComponent() {
    return Component.text("Das Event wurde an deiner Position erstellt.", NamedTextColor.GREEN);
  }

  /**
   * Event has been stopped component component.
   *
   * @return the component
   */
  public static Component eventHasBeenStoppedComponent() {
    return Component.text("Das Event wurde beendet.", NamedTextColor.GREEN);
  }

  /**
   * You need an item in your hand component component.
   *
   * @return the component
   */
  public static Component youNeedAnItemInYourHandComponent() {
    return Component.text("§cDu musst ein Item in deiner Hand halten!", NamedTextColor.RED);
  }

  /**
   * Item has no meta component component.
   *
   * @return the component
   */
  public static Component itemHasNoMetaComponent() {
    return Component.text("§cDas Item hat keine Metadaten!", NamedTextColor.RED);
  }

  /**
   * Item is now special component component.
   *
   * @return the component
   */
  public static Component itemIsNowSpecialComponent() {
    return Component.text("§aDas Item ist nun ein spezielles Item!", NamedTextColor.GREEN);
  }
}
