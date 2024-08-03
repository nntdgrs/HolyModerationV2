package org.nntdgrs.core.widgets;

import net.labymod.api.Laby;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.util.TextFormat;
import net.labymod.api.util.bounds.area.RectangleAreaPosition;
import org.nntdgrs.core.Configuration;
import org.nntdgrs.core.HolyModeration;

public class HolyModerationWidget extends TextHudWidget<TextHudWidgetConfig> {
  private final Configuration config;

  public static long freezingTimer = 0L;
  public static String currentCheckPlayerName = null;
  public static Boolean currentRevise = false;

  private TextLine mutesLine;

  public HolyModerationWidget(Configuration config) {
    super("holymoderation");
    this.config = config;
  }

  @Override
  public void initializePreConfigured(TextHudWidgetConfig config) {
    super.initializePreConfigured(config);

    config.setEnabled(true);
    config.setAreaIdentifier(RectangleAreaPosition.TOP_LEFT);
    config.setX(-2);
    config.setY(2);
    config.setParentToTailOfChainIn(RectangleAreaPosition.TOP_LEFT);
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);

    this.mutesLine = super.createLine("HolyModeration", "");
  }

  @Override
  public void onTick() {
    ClientPlayer clientPlayer = this.labyAPI.minecraft().getClientPlayer();
    if (clientPlayer != null && config.statisticEnabled().get()) {
      updateWidgetLine(true);
    } else {
      updateWidgetLine(false);
    }
  }

  private void updateWidgetLine(boolean state) {
    String formattedTime = String.format("%02d:%02d",
        (System.currentTimeMillis() / 1000L - freezingTimer) / 60L,
        (System.currentTimeMillis() / 1000L - freezingTimer) % 60L);

    this.mutesLine.updateAndFlush(TextFormat.SNAKE_CASE.toUpperCamelCase(
        "Текущая проверка: " + (!currentRevise ? "Отсутствует" : currentCheckPlayerName + " | " + formattedTime)));
    this.mutesLine.setVisible(state);
  }

  public static void ReviseEnd() {
    Laby.labyAPI().minecraft().chatExecutor().chat("/freezing " + currentCheckPlayerName, false);
    Laby.labyAPI().minecraft().chatExecutor().chat("/prova", false);
    currentCheckPlayerName = null;
    currentRevise = false;
    freezingTimer = 0L;
  }
}
