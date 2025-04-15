package tab.bettertab.tabList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameMode;
import tab.bettertab.Tools;
import tab.bettertab.config.BetterTabConfig;

public class TabEntry {
    private MinecraftClient client;
    public boolean validEntry = true;

    private final boolean renderHead = BetterTabConfig.CONFIG.instance().renderHeads;
    private final boolean renderPing = BetterTabConfig.CONFIG.instance().renderPing;
    private final boolean useNumericPing = BetterTabConfig.CONFIG.instance().numericalPing;
    
    private static final Identifier ICONS_TEXTURE = new Identifier("textures/gui/icons.png");

    public int totalWidth;
    public int totalHeight;

    public Text name;
    private List<OrderedText> lines;
    public int textWidth = 0;
    public int textHeight = 0;
    private int maxColumnWidth;
    private int textStartX = 0;

    private Identifier headTexture;
    private int iconY = 0;
    private GameMode gameMode;
    private List<Identifier> badges = new ArrayList<>();

    private String pingText;
    private int pingWidth = 0;
    private int pingColor;
    private int v;

    private boolean renderScore = false;
    private int scoreLength = 0;
    private Text scoreText;

    private boolean lineEntry = false;

    public TabEntry(MinecraftClient client, PlayerListEntry entry, int maxColumnWidth, Scoreboard scoreboard, ScoreboardObjective objective, boolean lineEntry) {
        if (lineEntry) {
            this.totalWidth = maxColumnWidth;
            this.textHeight = client.textRenderer.fontHeight;
            this.totalHeight = textHeight + 2;
            this.lineEntry = true;
            return;
        }
        this.client = client;
        this.maxColumnWidth = maxColumnWidth;
        TextRenderer textRenderer = client.textRenderer;
        this.gameMode = entry.getGameMode();

        name = Tools.getPlayerName(entry);
        if (name == null || name.getString().isEmpty()) {
            validEntry = false;
            return;
        }
        lines = textRenderer.wrapLines(name, maxColumnWidth);
        textHeight = textRenderer.getWrappedLinesHeight(name, maxColumnWidth) + (lines.size() > 1 ? (lines.size() - 1) * 2 : 0);
        textWidth = Collections.max(lines.stream().map(textRenderer::getWidth).toList());

        int badgeWidth = 0;
        if (BetterTabConfig.CONFIG.instance().renderBadges) {
            badges = BadgeManager.getBadges(entry);
            badgeWidth = badges.size() * 10;
        }

        if (BetterTabConfig.CONFIG.instance().renderScoreboardNumber && objective != null && entry.getGameMode() != GameMode.SPECTATOR) {
            ScoreboardPlayerScore readableScoreboardScore = scoreboard.getPlayerScore(entry.getProfile().getName(), objective);
            if (readableScoreboardScore != null) {
                if (objective.getRenderType() == ScoreboardCriterion.RenderType.HEARTS) {
                    scoreText = Text.literal(String.format(BetterTabConfig.CONFIG.instance().healthFormat, readableScoreboardScore.getScore())).formatted(Formatting.RED);
                } else {
                    scoreText = Text.literal(Integer.toString(readableScoreboardScore.getScore()))
                    		.fillStyle(Style.EMPTY.withColor(Formatting.YELLOW));
                }

                renderScore = true;
                scoreLength = client.textRenderer.getWidth(scoreText);
            }
        }

        iconY = (textHeight - 8) / 2 + 1;
        if (renderHead) {
            headTexture = entry.getSkinTexture();
        }

        if (renderPing) {
            if (useNumericPing) {
                pingText = String.format(BetterTabConfig.CONFIG.instance().numericFormat, entry.getLatency());
                pingWidth = textRenderer.getWidth(pingText);
                pingColor = Tools.numericalColorizer(entry.getLatency());
            } else {
                pingWidth = 10;
                if (entry.getLatency() < 0) {
                    v = 5;
                } else if (entry.getLatency() < 150) {
                    v = 0;
                } else if (entry.getLatency() < 300) {
                    v = 1;
                } else if (entry.getLatency() < 600) {
                    v = 2;
                } else if (entry.getLatency() < 1000) {
                    v = 3;
                } else {
                    v = 4;
                }
            }
        }

        textStartX = 2 + (renderHead ? 9 : 0) + badgeWidth;

        totalWidth = (2 + textWidth + badgeWidth + (renderHead ? 2 + 8 + 1 : 0) + ((scoreLength > 0 || pingWidth > 0) ? 5 : 0) + scoreLength + pingWidth + 2);
        totalHeight = (2 + textHeight);
    }

    public void render(DrawContext context, int x1, int y1, int columnWidth) {
        context.fill(x1, y1, x1 + columnWidth, y1 + textHeight + 1, BetterTabConfig.CONFIG.instance().cellColor.getRGB());

        if (lineEntry) {
            context.fill(x1 + 2, y1 + (textHeight + 1) / 2, x1 + columnWidth - 2, y1 + (textHeight + 1) / 2 + 1, BetterTabConfig.CONFIG.instance().emptyLineColor.getRGB());
            return;
        }
        x1 += 2;
        int iconX = x1;
        for (Identifier badge : badges) {
            context.drawTexture(badge, iconX, y1 + iconY, 0, 0, 8, 8, 8, 8);
            iconX += 10;
        }

        if (renderHead) {
            PlayerSkinDrawer.draw(context, headTexture, iconX, y1 + iconY, 8, true, false);
        }

        context.drawTextWrapped(client.textRenderer, name, x1 + textStartX, y1 + 2 + (lines.size() > 1 ? lines.size() - 1 : 0), 
        		maxColumnWidth, gameMode == GameMode.SPECTATOR ? BetterTabConfig.CONFIG.instance().spectatorColor.getRGB() : BetterTabConfig.CONFIG.instance().nameColor.getRGB());

        if (renderScore) {
            context.drawTextWithShadow(client.textRenderer, scoreText, x1 + columnWidth - pingWidth - scoreLength - 6, y1 + 2, 0xFFFFFFFF);
        }

        if (renderPing) {
            if (useNumericPing) {
                context.drawTextWithShadow(client.textRenderer, pingText, x1 + columnWidth - pingWidth - 3, y1 + 2, pingColor);
            } else {
                context.getMatrices().push();
        		context.getMatrices().translate(0.0F, 0.0F, 100.0F);
        		context.drawTexture(ICONS_TEXTURE, x1 + columnWidth - 14, y1 + 2, 0, 176 + v * 8, 10, 8);
        		context.getMatrices().pop();
            }
        }
    }
}
