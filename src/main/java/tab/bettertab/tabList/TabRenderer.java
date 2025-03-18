package tab.bettertab.tabList;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.OrderedText;
import org.jetbrains.annotations.Nullable;

import static tab.bettertab.tabList.TabUpdater.*;

public class TabRenderer {
    public static boolean immediatelyUpdate = false;

    public void render(MinecraftClient client, DrawContext context, int scaledWindowWidth, Scoreboard scoreboard, @Nullable ScoreboardObjective objective) {
        if (renderColumns.isEmpty()) {
            return;
        }
        TextRenderer textRenderer = client.textRenderer;

        int x = startTextX;
        int y = startBoxY + 5;

        context.fill(startBoxX, startBoxY, startBoxX + totalWidth, startBoxY + totalHeight, 0x80000000);

        y = renderHeader(textRenderer, context, y);
        renderFooter(textRenderer, context);

        renderPageArrows(textRenderer, context, y);

        for (TabColumn column : renderColumns) {
            column.render(context, x, y);
            x += column.totalWidth;
        }
    }

    private int renderHeader(TextRenderer textRenderer, DrawContext context, int y) {
        for (OrderedText text : headerList) {
            context.drawCenteredTextWithShadow(textRenderer, text, startBoxX + totalWidth / 2, y, 0xffffffff);
            y += textRenderer.fontHeight;
        }
        return y;
    }

    private void renderFooter(TextRenderer textRenderer, DrawContext context) {
        int footerHeight = footerStartY;
        for (OrderedText text : footerList) {
            context.drawCenteredTextWithShadow(textRenderer, text, startBoxX + totalWidth / 2, footerHeight, 0xffffffff);
            footerHeight += textRenderer.fontHeight;
        }
    }

    private void renderPageArrows(TextRenderer textRenderer, DrawContext context, int y) {
        if (canScrollLeft) {
            context.drawCenteredTextWithShadow(textRenderer, "<", startBoxX + 7, y + columnsHeight / 2 - 4, 0xFFFFFFFF);
        }
        if (canScrollRight) {
            context.drawCenteredTextWithShadow(textRenderer, ">", startBoxX + totalWidth - 7, y + columnsHeight / 2 - 4, 0xFFFFFFFF);
        }
    }
}
