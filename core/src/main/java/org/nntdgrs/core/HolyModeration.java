package org.nntdgrs.core;

import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;
import org.nntdgrs.core.events.MessageSendEvent;
import org.nntdgrs.core.events.NewChatMessageEvent;
import org.nntdgrs.core.events.OnServerJoinEvent;
import org.nntdgrs.core.events.PlayerTimeCheckEvent;
import org.nntdgrs.core.events.TryProvaEvent;
import org.nntdgrs.core.generated.DefaultReferenceStorage;
import org.nntdgrs.core.versioned.SendChat;
import org.nntdgrs.core.widgets.HolyModerationWidget;

@AddonMain
public class HolyModeration extends LabyAddon<Configuration> {
  private SendChat sendChat;

  @Override
  protected void enable() {
    this.registerSettingCategory();

    sendChat = ((DefaultReferenceStorage) this.referenceStorageAccessor()).sendChat();

    this.labyAPI().eventBus().registerListener(new MessageSendEvent(this));
    this.labyAPI().eventBus().registerListener(new NewChatMessageEvent(this.configuration(), this));
    this.labyAPI().eventBus().registerListener(new PlayerTimeCheckEvent(this));
    this.labyAPI().eventBus().registerListener(new OnServerJoinEvent());
    this.labyAPI().eventBus().registerListener(new TryProvaEvent(this));

    this.labyAPI().hudWidgetRegistry().register(new HolyModerationWidget(this.configuration()));

    this.logger().info("[HolyModeration V2] Mod successfully loaded! Developer: @ninetydyygres");
  }

  @Override
  protected Class<Configuration> configurationClass() {
    return Configuration.class;
  }

  public SendChat sendChat() {
    return sendChat;
  }
}
