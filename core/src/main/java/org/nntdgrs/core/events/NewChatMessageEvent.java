package org.nntdgrs.core.events;

import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import org.nntdgrs.core.Configuration;
import org.nntdgrs.core.widgets.HolyModerationWidget;

public class NewChatMessageEvent {

  private final Configuration config;

  public NewChatMessageEvent(Configuration config) {
    this.config = config;
  }

  @Subscribe
  public void onChatReceiveEvent(ChatReceiveEvent event) {
    if (config.autoAnyDeskEnabled().get()) {
      if (!event.chatMessage().getPlainText().isEmpty()) {
        String message = event.chatMessage().getPlainText();

        if (HolyModerationWidget.currentCheckPlayerName == null) return;

        if (message.contains(HolyModerationWidget.currentCheckPlayerName)) {
          if (message.startsWith("[" + HolyModerationWidget.currentCheckPlayerName + " ->")) {
            String msgText = message.split("я]", 0)[1].replace(" ", "");

            if (CheckCorrectLong(msgText) && msgText.length() >= 9 && msgText.length() <= 11) {
              ClickEvent.copyToClipboard(msgText);
            }
          }
          if (message.contains(":")) {
            String chatText = message.split(":")[1].replace(" ", "");

            if (CheckCorrectLong(chatText) && chatText.length() >= 9 && chatText.length() <= 11) {
              ClickEvent.copyToClipboard(chatText);
            }
          }
        }
      }
    }
    if (HolyModerationWidget.currentRevise) {
      if (event.chatMessage().getPlainText().startsWith("[PMS]")) {
        event.setCancelled(true);
      } else if (event.chatMessage().getPlainText().equals("Игрок не замьючен!")) {
        event.setCancelled(true);
      } else if (event.chatMessage().getPlainText().equals("Перемещение на logo")) {
        event.setCancelled(true);
      }
    }
  }

  public static boolean CheckCorrectLong(String value) {
    long newValue = 0;
    {
      try {
        newValue = Long.parseLong(value);
        return (true);
      } catch (NumberFormatException e) {
        return (false);
      }
    }
  }
}
