package org.nntdgrs.core;

import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.property.ConfigProperty;

@ConfigName("settings")
public class Configuration extends AddonConfig {

  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  @SwitchSetting
  private ConfigProperty<Boolean> statisticEnabled = new ConfigProperty<>(true);

  public ConfigProperty<Boolean> statisticEnabled() {
    return statisticEnabled;
  }

  @TextFieldSetting
  private ConfigProperty<String> vkLink = new ConfigProperty<>("https://vk.com/");

  public ConfigProperty<String> vkLink() { return this.vkLink;}

  @SwitchSetting
  private ConfigProperty<Boolean> autoAnyDeskEnabled = new ConfigProperty<>(true);

  public ConfigProperty<Boolean> autoAnyDeskEnabled() { return this.autoAnyDeskEnabled; }
}
