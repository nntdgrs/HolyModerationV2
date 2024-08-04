package org.nntdgrs.core.events;

import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import org.nntdgrs.core.HolyModeration;
import java.util.Arrays;

public class PlayerTimeCheckEvent {
  private final HolyModeration holyModeration;

  public static Boolean currentPlayTimeCheck = false;

  private static String lastSession;
  public static String playerServ;

  private static int lastActivityMin;

  public PlayerTimeCheckEvent(HolyModeration holyModeration) {
    this.holyModeration = holyModeration;
  }

  @Subscribe
  public void onChatReceiveEvent(ChatReceiveEvent event) {
    if (currentPlayTimeCheck && !TryProvaEvent.currentTry) {
      if (event.chatMessage().getPlainText().startsWith("------------------PlayTimeAPI")) {
        event.setCancelled(true);
        holyModeration.sendChat().send(" ");
      } else if (event.chatMessage().getPlainText().startsWith("Активность")) {
        event.setCancelled(true);
        holyModeration.sendChat().send("§7[§bHM§7] §fАктивность игрока §l" + event.chatMessage().getPlainText().split(" ")[1]);
      } else if (event.chatMessage().getPlainText().startsWith("Общее время активности в игре:")) {
        event.setCancelled(true);
      } else if (event.chatMessage().getPlainText().startsWith("Текущая сессия:")) {
        event.setCancelled(true);
        lastSession = event.chatMessage().getPlainText().split(" ")[2];
        holyModeration.sendChat().send("§7[§bHM§7] §fНаходится на: §l" + event.chatMessage().getPlainText().split(" ")[2]);
      } else if (event.chatMessage().getPlainText().startsWith("Общее время активности в игре:")) {
        event.setCancelled(true);
      } else if (event.chatMessage().getPlainText().startsWith("Общее время в игре:")) {
        event.setCancelled(true);
      } else if (event.chatMessage().getPlainText().startsWith("Время бездействия:")) {
        event.setCancelled(true);
      } else if (event.chatMessage().getPlainText().startsWith("Последняя активность:")) {
        event.setCancelled(true);
        lastActivityMin = TryProvaEvent.extractMinutesAsInt(event.chatMessage().getPlainText());
        String[] lastActivityArray = Arrays.copyOfRange(event.chatMessage().getPlainText().split(" "), 2, event.chatMessage().getPlainText().split(" ").length);
        holyModeration.sendChat().send("§7[§bHM§7] §fПоследняя активность: " + String.join(" ", lastActivityArray));
      } else if (event.chatMessage().getPlainText().startsWith("Последний вход на анархию:")) {
        event.setCancelled(true);
      } else if (event.chatMessage().getPlainText().startsWith("---------------------------------------------------")) {
        event.setCancelled(true);

        if (lastSession.equals("(Оффлайн)")) {
          holyModeration.sendChat().send("§7[§bHM§7] §fНа проверку вызывать §cНЕЛЬЗЯ§f! Игрок §cОФФЛАЙН§f!");
          currentPlayTimeCheck = false;
          return;
        }

        if (lastActivityMin > 0 && lastActivityMin < 10) {
          holyModeration.sendChat().send("§7[§bHM§7] §fНа проверку вызывать §eне рекомендуется§f!");
          currentPlayTimeCheck = false;
        } else if (lastActivityMin > 9) {
          holyModeration.sendChat().send("§7[§bHM§7] §fНа проверку вызывать §cНЕЛЬЗЯ§f! Игрок §cAFK§f!");
          currentPlayTimeCheck = false;
        } else if (lastActivityMin == 0) {
          holyModeration.sendChat().send("§7[§bHM§7] §fНа проверку вызывать §aМОЖНО§f! Игрок находится на §a" + lastSession + "§f!");
          currentPlayTimeCheck = false;
        }
        holyModeration.sendChat().send(" ");
      } else if (event.chatMessage().getPlainText().isEmpty()) {
        event.setCancelled(true);
      }
    }
  }
}