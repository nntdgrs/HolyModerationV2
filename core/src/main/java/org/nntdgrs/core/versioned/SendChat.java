package org.nntdgrs.core.versioned;

import net.labymod.api.reference.annotation.Referenceable;

@Referenceable
public interface SendChat {
  void send(String message);
}
