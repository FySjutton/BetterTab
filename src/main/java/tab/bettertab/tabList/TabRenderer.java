package tab.bettertab.tabList;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.jetbrains.annotations.Nullable;
import tab.bettertab.config.BetterTabConfig;

import static tab.bettertab.tabList.TabUpdater.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;

public class TabRenderer {
    public static boolean immediatelyUpdate = false;
    private static long scrollIndicatorLast = 0;
    private static boolean showScrollingIndicator = false;
    private static final int columnNumberColor = BetterTabConfig.CONFIG.instance().columnNumberColor.getRGB();

    public static void render(Minecraft client, GuiGraphicsExtractor graphics, int screenWidth, Scoreboard scoreboard, @Nullable Objective displayObjective) {
        if (renderColumns.isEmpty()) {
            return;
        }
        Font textRenderer = client.font;

        int x = startTextX;
        int y = startBoxY + 5;

        graphics.fill(startBoxX, startBoxY, startBoxX + totalWidth, startBoxY + totalHeight, BetterTabConfig.CONFIG.instance().backgroundColor.getRGB());

        y = renderHeader(textRenderer, graphics, y);
        renderFooter(textRenderer, graphics);

        if (BetterTabConfig.CONFIG.instance().renderScrollIndicator) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - scrollIndicatorLast > BetterTabConfig.CONFIG.instance().scrollIndicatorSpeed) {
                showScrollingIndicator = !showScrollingIndicator;
                scrollIndicatorLast = currentTime;
            }
            if (showScrollingIndicator) {
                renderPageArrows(textRenderer, graphics, y);
            }
        }

        for (TabColumn column : renderColumns) {
            column.render(graphics, x, y);
            x += column.totalWidth;
        }

        if (BetterTabConfig.CONFIG.instance().scrollingType.equals(BetterTabConfig.ScrollingType.Page)) {
            graphics.centeredText(client.font, String.valueOf(pageNumber), startBoxX + totalWidth / 2, footerStartY - 11, columnNumberColor);
        }
    }

    private static int renderHeader(Font textRenderer, GuiGraphicsExtractor graphics, int y) {
        for (FormattedCharSequence text : headerList) {
            graphics.centeredText(textRenderer, text, startBoxX + totalWidth / 2, y, 0xffffffff);
            y += textRenderer.lineHeight;
        }
        return y;
    }

    private static void renderFooter(Font textRenderer, GuiGraphicsExtractor graphics) {
        int footerHeight = footerStartY;
        for (FormattedCharSequence text : footerList) {
            graphics.centeredText(textRenderer, text, startBoxX + totalWidth / 2, footerHeight, 0xffffffff);
            footerHeight += textRenderer.lineHeight;
        }
    }

    private static void renderPageArrows(Font textRenderer, GuiGraphicsExtractor graphics, int y) {
        if (canScrollLeft) {
            graphics.centeredText(textRenderer, "<", startBoxX + 7, y + columnsHeight / 2 - 4, BetterTabConfig.CONFIG.instance().scrollIndicatorColor.getRGB());
        }
        if (canScrollRight) {
            graphics.centeredText(textRenderer, ">", startBoxX + totalWidth - 7, y + columnsHeight / 2 - 4, BetterTabConfig.CONFIG.instance().scrollIndicatorColor.getRGB());
        }
    }
}
