package org.nntdgrs.core.events;

import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.SubServerSwitchEvent;

public class OnServerJoinEvent {
  @Subscribe
  public void onSubServerSwitchEvent(SubServerSwitchEvent event) {
    if (TryProvaEvent.currentTry) {
      TryProvaEvent.UpdateModerServer();
    }
  }
}
