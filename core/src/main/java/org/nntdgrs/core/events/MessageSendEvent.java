package org.nntdgrs.core.events;

import net.labymod.api.Laby;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatMessageSendEvent;
import org.nntdgrs.core.HolyModeration;
import org.nntdgrs.core.widgets.HolyModerationWidget;
import java.util.Arrays;
import java.util.Objects;

public class MessageSendEvent {

  private final HolyModeration holyModeration;

  public MessageSendEvent(HolyModeration holyModeration) {
    this.holyModeration = holyModeration;
  }

  @Subscribe
  public void onChatMessageSendEvent(ChatMessageSendEvent event) {
    if (event.getMessage().startsWith("/frz") || event.getMessage().startsWith("/freeze")) {
      event.setCancelled(true);

      if (event.getMessage().split(" ").length < 2) {
        holyModeration.sendChat().send("§7[§bHolyModeration§7] §fИспользуйте: /freeze <ник>");
        return;
      }

      String freezingPlayer = event.getMessage().split(" ")[1];

      if (HolyModerationWidget.currentRevise) {
        if (Objects.equals(freezingPlayer, HolyModerationWidget.currentCheckPlayerName)) {
          HolyModerationWidget.ReviseEnd();
        }
      } else {
        Laby.labyAPI().minecraft().chatExecutor().chat("/warp logo", false);
        Laby.labyAPI().minecraft().chatExecutor().chat("/prova", false);
        Laby.labyAPI().minecraft().chatExecutor()
            .chat("/freezing " + freezingPlayer, false);
        Laby.labyAPI().minecraft().chatExecutor().chat("/w " + freezingPlayer
            + " &e&lЭто проверка на читы, у тебя есть 7 минут, что бы скинуть свой &c&lAnyDesk &eмне в &c&lЛИЧНЫЕ СООБЩЕНИЯ", false);
        Laby.labyAPI().minecraft().chatExecutor().chat("/w " + freezingPlayer
            + " &e&lЕсли у тебя есть &c&lчиты/запрещённые моды, программы и тому подобное, &e&lто ты можешь признаться в этом, срок наказания сократится с &c&l30 &e&lдо &c&l20 дней", false);
        Laby.labyAPI().minecraft().chatExecutor().chat("/w " + freezingPlayer
            + " &e&lТебе необходимо установить &c&lAnyDesk&e&l, для установки тебе нужно зайти в браузер и написать в поиске &c&l\"AnyDesk com\"&e&l, после чего нажать&c&l\"Скачать\"&e&l и отправить &c&lкод (Это рабочее место) &e&lмне в &c&lЛИЧНЫЕ СООБЩЕНИЯ", false);
        Laby.labyAPI().minecraft().chatExecutor()
            .chat("/checkmute " + freezingPlayer, false);

        holyModeration.sendChat().send("§7[§bHolyModeration§7] §fВы успешно вызвали игрока " + freezingPlayer + " на проверку. Не забудьте установить статус \"Проверка\" в журнале :)");

        HolyModerationWidget.freezingTimer = System.currentTimeMillis() / 1000L;
        HolyModerationWidget.currentRevise = true;
        HolyModerationWidget.currentCheckPlayerName = freezingPlayer;
      }
    }
    if (event.getMessage().startsWith("/unfrz") || event.getMessage().startsWith("/unfreeze")) {
      event.setCancelled(true);

      if (HolyModerationWidget.currentRevise) {
        HolyModerationWidget.ReviseEnd();
        holyModeration.sendChat().send("§7[§bHolyModeration§7] §fИгрок успешно разморожен!");
      } else {
        holyModeration.sendChat().send("§7[§bHolyModeration§7] §fВы не на проверке!");
      }
    }
    if (event.getMessage().startsWith("/sban")) {
      event.setCancelled(true);

      if (!HolyModerationWidget.currentRevise) {
        holyModeration.sendChat().send("§7[§bHolyModeration§7] §7Вы не проводите проверку!");
        return;
      }

      if (holyModeration.configuration().vkLink().get().length() > 15 && holyModeration.configuration().vkLink().get().startsWith("https://vk.com/")) {
        String[] splitedMessage = event.getMessage().split(" ");

        if (splitedMessage.length > 2) {
          if (CheckCorrectInt(splitedMessage[1])) {
            String[] reasonArray = Arrays.copyOfRange(splitedMessage, 2, splitedMessage.length);

            Banip(HolyModerationWidget.currentCheckPlayerName, splitedMessage[1], String.join(" ", reasonArray), holyModeration.configuration().vkLink().get());
            HolyModerationWidget.ReviseEnd();
          } else {
            holyModeration.sendChat().send("§7[§bHolyModeration§7] §fИспользуйте: /sban <время> <причина>. Пример: /sban 30 Призание");
          }
        } else {
          holyModeration.sendChat().send("§7[§bHolyModeration§7] §fИспользуйте: /sban <время> <причина>. Пример: /sban 30 Призание");
        }
      } else {
        holyModeration.sendChat().send("§7[§bHolyModeration§7] §7У вас не установлена ссылка на VK! Поменяйте её через настройки, либо .vk <ссылка>");
      }
    }
    if (event.getMessage().startsWith(".vk")) {
      event.setCancelled(true);
      String[] splitedMessage = event.getMessage().split(" ");

      if (splitedMessage.length > 1 && splitedMessage[1].startsWith("https://vk.com/")) {
        holyModeration.configuration().vkLink().set(splitedMessage[1]);
        holyModeration.sendChat().send("§7[§bHolyModeration§7] §fВы установили ссылку на свой вк: " + splitedMessage[1]);
      } else {
        holyModeration.sendChat().send("§7[§bHolyModeration§7] §fИспользуйте: .vk <ссылка> | Пример: https://vk.com/ninetydegreess");
      }
    }
    if (event.getMessage().startsWith("/ptime")) {
      event.setCancelled(true);
      String[] spltMessage = event.getMessage().split(" ");

      if (spltMessage.length == 2) {
        PlayerTimeCheckEvent.currentPlayTimeCheck = true;
        Laby.labyAPI().minecraft().chatExecutor().chat("/playtime " + spltMessage[1], false);
      } else {
        holyModeration.sendChat().send("§7[§bHolyModeration§7] §fИспользуйте: /ptime <nick>");
      }
    }
  }

  public static boolean CheckCorrectInt(String value) {
    int newValue = 0;
    {
      try {
        newValue = Integer.parseInt(value);
        return (true);
      } catch (NumberFormatException e) {
        return (false);
      }
    }
  }

  public static void Banip(String nick, String time, String reason, String vk) {
    Laby.labyAPI().minecraft().chatExecutor().chat("/banip " + nick + " " + time + "d 2.4 (" + reason + ") | Вопросы? " + vk + " -s", false);
  }
}
