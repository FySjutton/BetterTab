package tab.bettertab.tabList;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.scoreboard.*;
import net.minecraft.scoreboard.number.NumberFormat;
import net.minecraft.scoreboard.number.StyledNumberFormat;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameMode;
import tab.bettertab.Tools;
import tab.bettertab.config.BetterTabConfig;

import java.util.Collections;
import java.util.List;

public class TabEntry {
    private final MinecraftClient client;

    public int totalWidth;
    public int totalHeight;

    public Text name;
    public int textWidth = 0;
    public int textHeight = 0;
    private int maxColumnWidth;
    private int textStartX;

    private final boolean renderHead = BetterTabConfig.CONFIG.instance().renderHeads;
    private Identifier headTexture;
    private int iconY;

    private List<Identifier> badges;

    private boolean useNumericPing = false;
    private String pingText;
    private int pingWidth = 10;
    private int pingColor = 0xFFFFFFFF;

    private Identifier pingTexture;

    private boolean renderScore = false;
    private Text scoreText;
    private int scoreLength = 0;

    public TabEntry(MinecraftClient client, PlayerListEntry entry, int maxColumnWidth, Scoreboard scoreboard, ScoreboardObjective objective) {
        this.client = client;
        this.maxColumnWidth = maxColumnWidth;
        TextRenderer textRenderer = client.textRenderer;

        name = Tools.getPlayerName(entry);

        List<OrderedText> lines = textRenderer.wrapLines(name, maxColumnWidth);
        textHeight = textRenderer.getWrappedLinesHeight(name, maxColumnWidth);
        textWidth = Collections.max(lines.stream().map(textRenderer::getWidth).toList());

        badges = BadgeManager.getBadges(entry);
        int badgeWidth = badges.size() * 10;

        if (objective != null && entry.getGameMode() != GameMode.SPECTATOR) {
            ScoreHolder scoreHolder = ScoreHolder.fromProfile(entry.getProfile());
            ReadableScoreboardScore readableScoreboardScore = scoreboard.getScore(scoreHolder, objective);
            if (readableScoreboardScore != null) {

                if (objective.getRenderType() != ScoreboardCriterion.RenderType.HEARTS) {
                    NumberFormat numberFormat = objective.getNumberFormatOr(StyledNumberFormat.YELLOW);
                    scoreText = ReadableScoreboardScore.getFormattedScore(readableScoreboardScore, numberFormat);
                    renderScore = true;
                    scoreLength = client.textRenderer.getWidth(scoreText);
                }
            }
        }

        if (renderHead) {
            headTexture = entry.getSkinTextures().texture();
        }
        iconY = (textHeight - 8) / 2 + 1;

        textStartX = 2  + (renderHead ? 9 : 0);

        if (useNumericPing) {
            pingText = String.valueOf(entry.getLatency()) + "ms";
            pingWidth = textRenderer.getWidth(pingText);
        } else {
            if (entry.getLatency() < 0) {
                pingTexture = Identifier.ofVanilla("icon/ping_unknown");
            } else if (entry.getLatency() < 150) {
                pingTexture = Identifier.ofVanilla("icon/ping_5");
            } else if (entry.getLatency() < 300) {
                pingTexture = Identifier.ofVanilla("icon/ping_4");
            } else if (entry.getLatency() < 600) {
                pingTexture = Identifier.ofVanilla("icon/ping_3");
            } else if (entry.getLatency() < 1000) {
                pingTexture = Identifier.ofVanilla("icon/ping_2");
            } else {
                pingTexture = Identifier.ofVanilla("icon/ping_1");
            }
        }

        totalWidth = (2 + textWidth + badgeWidth + (renderHead ? 2 + 8 + 1 : 0) + 5 + scoreLength + pingWidth + 2);
        totalHeight = (2 + textHeight);
    }

    public void render(DrawContext context, int x1, int y1, int columnWidth) {
        context.fill(x1, y1, x1 + columnWidth, y1 + textHeight + 1, 0x20FFFFFF);

        x1 += 2;
        for (Identifier badge : badges) {
            context.drawTexture(RenderLayer::getGuiTextured, badge, x1, y1 + iconY, 0, 0, 8, 8, 8, 8);
            x1 += 10;
        }

        if (renderHead) {
            PlayerSkinDrawer.draw(context, headTexture, x1, y1 + iconY, 8, true, false, -1);
        }

        context.drawWrappedText(client.textRenderer, name, x1 + textStartX, y1 + 2, maxColumnWidth, 0xFFFFFFFF, true);

        if (renderScore) {
            context.drawTextWithShadow(client.textRenderer, scoreText, x1 + columnWidth - pingWidth - scoreLength - 6, y1 + 2, 0xFFFFFFFF);
        }

        if (useNumericPing) {
            context.drawTextWithShadow(client.textRenderer, pingText, x1 + columnWidth - pingWidth - 3, y1 + 2, pingColor);
        } else {
            context.drawGuiTexture(RenderLayer::getGuiTextured, pingTexture, x1 + columnWidth - 14, y1 + 2, 10, 8);
        }
    }
}
