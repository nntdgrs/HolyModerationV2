package org.nntdgrs.v1_20_1;

import javax.inject.Singleton;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.nntdgrs.core.versioned.SendChat;

@Singleton
@Implements(SendChat.class)
public class VersionedSendChat implements SendChat {
  @Override
  public void send(String message) {
    Minecraft.getInstance().gui.getChat().addMessage(Component.nullToEmpty(message));
  }
}
