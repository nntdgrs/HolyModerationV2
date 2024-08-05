package org.nntdgrs.core.events;

import net.labymod.api.Laby;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import org.nntdgrs.core.HolyModeration;

public class TryProvaEvent {
  private final HolyModeration holyModeration;

  public static TryProvaEvent instance;

  private int lastTryPlayerActivityMin = -1;

  public static Boolean currentTry = false;
  public static String tryPlayer = "";

  public static String lastTryPlayerSession = "";
  public static String moderLastSession = "";
  public static Boolean tryPlayerFind = false;

  private Boolean tryPlayerTimeCheck = false;
  private static Boolean updateModerServer = false;

  public TryProvaEvent(HolyModeration holyModeration) {
    this.holyModeration = holyModeration;
    instance = this;
  }

  @Subscribe
  public void onChatReceiveEvent(ChatReceiveEvent event) {
    if (currentTry) {
      if (event.chatMessage().getPlainText().startsWith("Игрок " + tryPlayer)) {
        event.setCancelled(true);
        if (event.chatMessage().getPlainText().split(" ").length == 6) {
          tryPlayerTimeCheck = true;
          lastTryPlayerSession = event.chatMessage().getPlainText().split(" ")[5];
          Laby.labyAPI().minecraft().chatExecutor().chat("/playtime " + tryPlayer, false);
        }
      } else if (event.chatMessage().getPlainText().startsWith("Игрок " + Laby.labyAPI().getName())) {
        event.setCancelled(true);
        if (event.chatMessage().getPlainText().split(" ").length == 6) {
          if (updateModerServer) {
            CheckCurrentAnarchy(event.chatMessage().getPlainText().split(" ")[5]);
          } else {
            moderLastSession = event.chatMessage().getPlainText().split(" ")[5];
            tryPlayerFind = true;
            Laby.labyAPI().minecraft().chatExecutor().chat("/find " + tryPlayer, false);
          }
        }
      } else if (event.chatMessage().getPlainText().startsWith("Игрок оффлайн") && tryPlayerFind) {
        event.setCancelled(true);
        tryPlayerFind = false;
        holyModeration.sendChat().send("§7[§bHM§7] §fИгрок §cНЕ В СЕТИ§f! Вызов на проверку §cневозможен§f!");
        currentTry = false;
        tryPlayer = null;
        moderLastSession = null;
        lastTryPlayerSession = null;
        tryPlayerTimeCheck = false;
        lastTryPlayerActivityMin = 0;
        updateModerServer = false;
      }

      if (tryPlayerTimeCheck) {
        if (event.chatMessage().getPlainText().startsWith("------------------PlayTimeAPI")) {
          event.setCancelled(true);
        } else if (event.chatMessage().getPlainText().startsWith("Активность")) {
          event.setCancelled(true);
        } else if (event.chatMessage().getPlainText().startsWith("Общее время активности в игре:")) {
          event.setCancelled(true);
        } else if (event.chatMessage().getPlainText().startsWith("Текущая сессия:")) {
          event.setCancelled(true);
        } else if (event.chatMessage().getPlainText().startsWith("Общее время активности в игре:")) {
          event.setCancelled(true);
        } else if (event.chatMessage().getPlainText().startsWith("Общее время в игре:")) {
          event.setCancelled(true);
        } else if (event.chatMessage().getPlainText().startsWith("Время бездействия:")) {
          event.setCancelled(true);
        } else if (event.chatMessage().getPlainText().startsWith("Последняя активность:")) {
          lastTryPlayerActivityMin = extractMinutesAsInt(event.chatMessage().getPlainText());
          event.setCancelled(true);
        } else if (event.chatMessage().getPlainText().startsWith("Последний вход на анархию:")) {
          event.setCancelled(true);
        } else if (event.chatMessage().getPlainText().startsWith("---------------------------------------------------")) {
          event.setCancelled(true);
        }
        tryPlayerTimeCheck = false;
        if (lastTryPlayerActivityMin == 0 && !lastTryPlayerSession.equals("(Оффлайн)")) {
          Laby.labyAPI().minecraft().chatExecutor().chat("/hub", false);
          holyModeration.sendChat().send(" ");
          holyModeration.sendChat().send("§7[§bHM§7] §fИгрока §aможно §fпроверять.");
          holyModeration.sendChat().send("§7[§bHM§7] §fВы были перемещены в lobby. Заходите на §a" + lastTryPlayerSession);
          holyModeration.sendChat().send("§7[§bHM§7] §fПри заходе на указаную анархию, вы §aавтоматически§f вызовите его на §eпроверку§f.");
          holyModeration.sendChat().send("§7[§bHM§7] §fЕсли вы §cпередумали §fего проверять, пропишите §e§l.canceltry§f!");
          holyModeration.sendChat().send(" ");
        } else if (lastTryPlayerActivityMin > 0) {
          holyModeration.sendChat().send("§7[§bHM§7] §fИгрок §cAFK§f! Вызов на проверку §cневозможен§f!");
          currentTry = false;
          tryPlayer = null;
          moderLastSession = null;
          lastTryPlayerSession = null;
          tryPlayerTimeCheck = false;
          lastTryPlayerActivityMin = 0;
          updateModerServer = false;
        } else if (lastTryPlayerSession.equals("(Оффлайн)")) {
          holyModeration.sendChat().send("§7[§bHM§7] §fИгрок §cНЕ В СЕТИ§f! Вызов на проверку §cневозможен§f!");
          currentTry = false;
          tryPlayer = null;
          moderLastSession = null;
          lastTryPlayerSession = null;
          tryPlayerTimeCheck = false;
          lastTryPlayerActivityMin = 0;
          updateModerServer = false;
        }
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

  public void CheckCurrentAnarchy(String anarchy) {
    if (anarchy.equals(lastTryPlayerSession)) {
      Laby.labyAPI().minecraft().chatExecutor().chat("/frz " + tryPlayer);
      currentTry = false;
      tryPlayer = null;
      moderLastSession = null;
      lastTryPlayerSession = null;
      tryPlayerTimeCheck = false;
      lastTryPlayerActivityMin = 0;
      updateModerServer = false;
    } else {
      holyModeration.sendChat().send("§7[§bHM§7] §fВы зашли не на ту §cанархию§f! Игрок находится на §a" + lastTryPlayerSession);
      holyModeration.sendChat().send("§7[§bHM§7] §fВы зашли не на ту §cанархию§f! Игрок находится на §a" + lastTryPlayerSession);
      holyModeration.sendChat().send("§7[§bHM§7] §fВы зашли не на ту §cанархию§f! Игрок находится на §a" + lastTryPlayerSession);
    }
  }

  public static void UpdateModerServer() {
    updateModerServer = true;
    Laby.labyAPI().minecraft().chatExecutor().chat("/find " + Laby.labyAPI().getName());
  }
}