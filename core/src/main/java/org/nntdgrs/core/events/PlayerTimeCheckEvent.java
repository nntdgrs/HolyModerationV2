package org.nntdgrs.core.events;

import net.labymod.api.Laby;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import org.nntdgrs.core.HolyModeration;
import java.util.Arrays;

public class PlayerTimeCheckEvent {
  private final HolyModeration holyModeration;

  public static Boolean currentPlayTimeCheck = false;

  private String lastSession;
  private int lastActivityMin;

  public PlayerTimeCheckEvent(HolyModeration holyModeration) {
    this.holyModeration = holyModeration;
  }

  @Subscribe
  public void onChatReceiveEvent(ChatReceiveEvent event) {
    if (currentPlayTimeCheck) {
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
        lastActivityMin = extractMinutesAsInt(event.chatMessage().getPlainText());
        String[] lastActivityArray = Arrays.copyOfRange(event.chatMessage().getPlainText().split(" "), 2, event.chatMessage().getPlainText().split(" ").length);
        holyModeration.sendChat().send("§7[§bHM§7] §fПоследняя активность: " + String.join(" ", lastActivityArray));
      } else if (event.chatMessage().getPlainText().startsWith("Последний вход на анархию:")) {
        event.setCancelled(true);
      } else if (event.chatMessage().getPlainText().startsWith("---------------------------------------------------")) {
        event.setCancelled(true);

        if (lastSession.equals("(Оффлайн)")) {
          holyModeration.sendChat().send("§7[§bHM§7] §fНа проверку вызывать §cНЕЛЬЗЯ§f! Игрок §cОФФЛАЙН§f!");
          lastSession = "";
          lastActivityMin = -1;
          currentPlayTimeCheck = false;
          return;
        }

        if (lastActivityMin > 0 && lastActivityMin < 10) {
          holyModeration.sendChat().send("§7[§bHM§7] §fНа проверку вызывать §eне рекомендуется§f!");
          lastSession = "";
          lastActivityMin = -1;
          currentPlayTimeCheck = false;
        } else if (lastActivityMin > 9) {
          holyModeration.sendChat().send("§7[§bHM§7] §fНа проверку вызывать §cНЕЛЬЗЯ§f! Игрок §cAFK§f!");
          lastSession = "";
          lastActivityMin = -1;
          currentPlayTimeCheck = false;
        } else if (lastActivityMin == 0) {
          holyModeration.sendChat().send("§7[§bHM§7] §fНа проверку вызывать §aМОЖНО§f! Игрок находится на §a" + lastSession + "§f!");
          lastSession = "";
          lastActivityMin = -1;
          currentPlayTimeCheck = false;
        }

        holyModeration.sendChat().send(" ");
      } else if (event.chatMessage().getPlainText().isEmpty()) {
        event.setCancelled(true);
      }
    }
  }

  public static int extractMinutesAsInt(String input) {
    // Регулярное выражение для поиска числа перед "м."
    String regex = "(\\d+)м\\.";
    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
    java.util.regex.Matcher matcher = pattern.matcher(input);

    if (matcher.find()) {
      String minutesStr = matcher.group(1); // Получаем найденное число в виде строки
      return Integer.parseInt(minutesStr); // Преобразуем строку в int
    } else {
      return 0; // Возвращаем 0, если число не найдено
    }
  }
}