package org.nntdgrs.core.events;

import net.labymod.api.Laby;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import org.nntdgrs.core.HolyModeration;
import java.util.Arrays;

public class PlayerTimeCheckEvent {
  private final HolyModeration holyModeration;

  public static PlayerTimeCheckEvent instance;

  public static Boolean currentPlayTimeCheck = false;
  private static Boolean sendInChat = false;
  public static Boolean tryPorverka = false;
  public static Boolean updatePlayerServ = false;

  private static String lastSession;
  public static String playerServ;

  private static int lastActivityMin;

  public PlayerTimeCheckEvent(HolyModeration holyModeration) {
    this.holyModeration = holyModeration;
    instance = this;
  }

  @Subscribe
  public void onChatReceiveEvent(ChatReceiveEvent event) {
    if (currentPlayTimeCheck) {
      if (event.chatMessage().getPlainText().startsWith("------------------PlayTimeAPI")) {
        event.setCancelled(true);
        if (sendInChat) holyModeration.sendChat().send(" ");
      } else if (event.chatMessage().getPlainText().startsWith("Активность")) {
        event.setCancelled(true);
        if (sendInChat) holyModeration.sendChat().send("§7[§bHM§7] §fАктивность игрока §l" + event.chatMessage().getPlainText().split(" ")[1]);
      } else if (event.chatMessage().getPlainText().startsWith("Общее время активности в игре:")) {
        event.setCancelled(true);
      } else if (event.chatMessage().getPlainText().startsWith("Текущая сессия:")) {
        event.setCancelled(true);
        lastSession = event.chatMessage().getPlainText().split(" ")[2];
        if (sendInChat) holyModeration.sendChat().send("§7[§bHM§7] §fНаходится на: §l" + event.chatMessage().getPlainText().split(" ")[2]);
      } else if (event.chatMessage().getPlainText().startsWith("Общее время активности в игре:")) {
        event.setCancelled(true);
      } else if (event.chatMessage().getPlainText().startsWith("Общее время в игре:")) {
        event.setCancelled(true);
      } else if (event.chatMessage().getPlainText().startsWith("Время бездействия:")) {
        event.setCancelled(true);
      } else if (event.chatMessage().getPlainText().startsWith("Последняя активность:")) {
        event.setCancelled(true);
        lastActivityMin = extractMinutesAsInt(event.chatMessage().getPlainText());
        if (sendInChat) {
          String[] lastActivityArray = Arrays.copyOfRange(event.chatMessage().getPlainText().split(" "), 2, event.chatMessage().getPlainText().split(" ").length);
          holyModeration.sendChat().send("§7[§bHM§7] §fПоследняя активность: " + String.join(" ", lastActivityArray));
        }
      } else if (event.chatMessage().getPlainText().startsWith("Последний вход на анархию:")) {
        event.setCancelled(true);
      } else if (event.chatMessage().getPlainText().startsWith("---------------------------------------------------")) {
        event.setCancelled(true);

        if (tryPorverka) {
          if (!lastSession.equals("(Оффлайн)") && lastActivityMin == 0) {
            Laby.labyAPI().minecraft().chatExecutor().chat("/hub");
            holyModeration.sendChat().send(" ");
            holyModeration.sendChat().send("§7[§bHM§7] §fИгрока §aможно §fпроверять.");
            holyModeration.sendChat().send("§7[§bHM§7] §fВы были перемещены в lobby. Заходите на §a" + lastSession);
            holyModeration.sendChat().send("§7[§bHM§7] §fПри заходе на указаную анархию, вы §aавтоматически§f вызовите его на §eпроверку§f.");
            holyModeration.sendChat().send(" ");
          } else {
            holyModeration.sendChat().send("§7[§bHM§7] §fИгрок §cAFK§f! Вызов на проверку §cневозможен§f!");
          }
        }

        if (!sendInChat) {
          currentPlayTimeCheck = false;
          return;
        }

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
        if (sendInChat) holyModeration.sendChat().send(" ");
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

  public static void sendPlayerTime(String nick) {
    Laby.labyAPI().minecraft().chatExecutor().chat("/ptime " + nick);
    currentPlayTimeCheck = true;
    sendInChat = true;
  }

  public static void sendPlayerTime(String nick, Boolean sendInChat) {
    Laby.labyAPI().minecraft().chatExecutor().chat("/ptime " + nick);
    currentPlayTimeCheck = true;
    sendInChat = false;
  }

  public void updatePlayerServer() {
    updatePlayerServ = true;
    Laby.labyAPI().minecraft().chatExecutor().chat("/find " + Laby.labyAPI().getName());
  }

  public void CheckCurrentAnarchy(String anarchy) {
    if (anarchy.equals(lastSession)) {
      Laby.labyAPI().minecraft().chatExecutor().chat("/frz " + MessageSendEvent.tryProverkaPlayer);
      tryPorverka = false;
      MessageSendEvent.tryProverkaPlayer = null;
    } else {
      holyModeration.sendChat().send("§7[§bHM§7] §fВы зашли не на ту §cанархию§f!");
    }
  }
}