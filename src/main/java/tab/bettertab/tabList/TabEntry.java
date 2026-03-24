package tab.bettertab.tabList;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.numbers.NumberFormat;
import net.minecraft.network.chat.numbers.StyledFormat;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.GameType;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.ReadOnlyScoreInfo;
import net.minecraft.world.scores.ScoreHolder;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import tab.bettertab.Tools;
import tab.bettertab.config.BetterTabConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabEntry {
    private Minecraft client;
    public boolean validEntry = true;

    private final boolean renderHead = BetterTabConfig.CONFIG.instance().renderHeads;
    private final boolean renderPing = BetterTabConfig.CONFIG.instance().renderPing;
    private final boolean useNumericPing = BetterTabConfig.CONFIG.instance().numericalPing;

    public int totalWidth;
    public int totalHeight;

    public Component name;
    private List<FormattedCharSequence> lines;
    public int textWidth = 0;
    public int textHeight = 0;
    private int maxColumnWidth;
    private int textStartX = 0;

    private Identifier headTexture;
    private int iconY = 0;
    private GameType gameMode;
    private List<Identifier> badges = new ArrayList<>();

    private String pingText;
    private int pingWidth = 0;
    private int pingColor;
    private Identifier pingTexture;

    private boolean renderScore = false;
    private int scoreLength = 0;
    private Component scoreText;

    private boolean lineEntry = false;

    public TabEntry(Minecraft client, PlayerInfo entry, int maxColumnWidth, Scoreboard scoreboard, Objective objective, boolean lineEntry) {
        if (lineEntry) {
            this.totalWidth = maxColumnWidth;
            this.textHeight = client.font.lineHeight;
            this.totalHeight = textHeight + 2;
            this.lineEntry = true;
            return;
        }
        this.client = client;
        this.maxColumnWidth = maxColumnWidth;
        Font textRenderer = client.font;
        this.gameMode = entry.getGameMode();

        name = Tools.getPlayerName(entry);
        if (name == null || name.getString().isEmpty()) {
            validEntry = false;
            return;
        }
        lines = textRenderer.split(name, maxColumnWidth);
        textHeight = textRenderer.wordWrapHeight(name, maxColumnWidth) + (lines.size() > 1 ? (lines.size() - 1) * 2 : 0);
        textWidth = Collections.max(lines.stream().map(textRenderer::width).toList());

        int badgeWidth = 0;
        if (BetterTabConfig.CONFIG.instance().renderBadges) {
            badges = BadgeManager.getBadges(entry);
            badgeWidth = badges.size() * 10;
        }

        if (BetterTabConfig.CONFIG.instance().renderScoreboardNumber && objective != null && entry.getGameMode() != GameType.SPECTATOR) {
            ScoreHolder scoreHolder = ScoreHolder.fromGameProfile(entry.getProfile());
            ReadOnlyScoreInfo readableScoreboardScore = scoreboard.getPlayerScoreInfo(scoreHolder, objective);
            if (readableScoreboardScore != null) {
                if (objective.getRenderType() == ObjectiveCriteria.RenderType.HEARTS) {
                    scoreText = Component.literal(String.format(BetterTabConfig.CONFIG.instance().healthFormat, readableScoreboardScore.value())).withStyle(ChatFormatting.RED);
                } else {
                    NumberFormat numberFormat = objective.numberFormatOrDefault(StyledFormat.PLAYER_LIST_DEFAULT);
                    scoreText = ReadOnlyScoreInfo.safeFormatValue(readableScoreboardScore, numberFormat);
                }

                renderScore = true;
                scoreLength = client.font.width(scoreText);
            }
        }

        iconY = (textHeight - 8) / 2 + 1;
        if (renderHead) {
            headTexture = entry.getSkin().body().texturePath();
        }

        if (renderPing) {
            if (useNumericPing) {
                pingText = String.format(BetterTabConfig.CONFIG.instance().numericFormat, entry.getLatency());
                pingWidth = textRenderer.width(pingText);
                pingColor = Tools.numericalColorizer(entry.getLatency());
            } else {
                pingWidth = 10;
                if (entry.getLatency() < 0) {
                    pingTexture = Identifier.withDefaultNamespace("icon/ping_unknown");
                } else if (entry.getLatency() < 150) {
                    pingTexture = Identifier.withDefaultNamespace("icon/ping_5");
                } else if (entry.getLatency() < 300) {
                    pingTexture = Identifier.withDefaultNamespace("icon/ping_4");
                } else if (entry.getLatency() < 600) {
                    pingTexture = Identifier.withDefaultNamespace("icon/ping_3");
                } else if (entry.getLatency() < 1000) {
                    pingTexture = Identifier.withDefaultNamespace("icon/ping_2");
                } else {
                    pingTexture = Identifier.withDefaultNamespace("icon/ping_1");
                }
            }
        }

        textStartX = 2 + (renderHead ? 9 : 0) + badgeWidth;

        totalWidth = (2 + textWidth + badgeWidth + (renderHead ? 2 + 8 + 1 : 0) + ((scoreLength > 0 || pingWidth > 0) ? 5 : 0) + scoreLength + pingWidth + 2);
        totalHeight = (2 + textHeight);
    }

    public void render(GuiGraphics context, int x1, int y1, int columnWidth) {
        context.fill(x1, y1, x1 + columnWidth, y1 + textHeight + 1, BetterTabConfig.CONFIG.instance().cellColor.getRGB());

        if (lineEntry) {
            context.fill(x1 + 2, y1 + (textHeight + 1) / 2, x1 + columnWidth - 2, y1 + (textHeight + 1) / 2 + 1, BetterTabConfig.CONFIG.instance().emptyLineColor.getRGB());
            return;
        }
        x1 += 2;
        int iconX = x1;
        for (Identifier badge : badges) {
            context.blit(RenderPipelines.GUI_TEXTURED, badge, iconX, y1 + iconY, 0, 0, 8, 8, 8, 8);
            iconX += 10;
        }

        if (renderHead) {
            PlayerFaceRenderer.draw(context, headTexture, iconX, y1 + iconY, 8, true, false, -1);
        }

        context.drawWordWrap(client.font, name, x1 + textStartX, y1 + 2 + (lines.size() > 1 ? lines.size() - 1 : 0), maxColumnWidth, gameMode == GameType.SPECTATOR ? BetterTabConfig.CONFIG.instance().spectatorColor.getRGB() : BetterTabConfig.CONFIG.instance().nameColor.getRGB(), true);

        if (renderScore) {
            context.drawString(client.font, scoreText, x1 + columnWidth - pingWidth - scoreLength - 6, y1 + 2, 0xFFFFFFFF);
        }

        if (renderPing) {
            if (useNumericPing) {
                context.drawString(client.font, pingText, x1 + columnWidth - pingWidth - 3, y1 + 2, pingColor);
            } else {
                context.blitSprite(RenderPipelines.GUI_TEXTURED, pingTexture, x1 + columnWidth - 14, y1 + 2, 10, 8);
            }
        }
    }
}
