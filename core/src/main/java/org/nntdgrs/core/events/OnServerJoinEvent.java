package org.nntdgrs.core.events;

import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.ServerJoinEvent;

public class OnServerJoinEvent {
  @Subscribe
  public void onServerJoinEvent(ServerJoinEvent event) {
    System.out.println("Loh 123");

    if (PlayerTimeCheckEvent.tryPorverka = true) {
      PlayerTimeCheckEvent.instance.updatePlayerServer();
    }
  }
}
