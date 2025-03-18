package tab.bettertab;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.OrderedText;
import org.jetbrains.annotations.Nullable;

import static tab.bettertab.PlayerManager.*;

public class Renderer {
    public static boolean immediatelyUpdate = false;

    public void render(MinecraftClient client, DrawContext context, int scaledWindowWidth, Scoreboard scoreboard, @Nullable ScoreboardObjective objective) {
        TextRenderer textRenderer = client.textRenderer;

        int x = startTextX;
        int y = startBoxY + 5;

        context.fill(startBoxX, startBoxY, startBoxX + totalWidth, startBoxY + totalHeight, 0x80000000);

        for (OrderedText text : headerList) {
            context.drawCenteredTextWithShadow(textRenderer, text, startTextX + totalWidth / 2, y, 0xffffffff);
            y += textRenderer.fontHeight;
        }

        int footerHeight = footerStartY;
        for (OrderedText text : footerList) {
            context.drawCenteredTextWithShadow(textRenderer, text, startTextX + totalWidth / 2, footerHeight, 0xffffffff);
            footerHeight += textRenderer.fontHeight;
        }

        if (canScrollLeft) {
            context.drawCenteredTextWithShadow(textRenderer, "<", startBoxX + 7, y + totalHeight / 2 - 4, 0xFFFFFFFF);
        }
        if (canScrollRight) {
            context.drawCenteredTextWithShadow(textRenderer, ">", startBoxX + totalWidth - 7, y + totalHeight / 2 - 4, 0xFFFFFFFF);
        }
        for (TabColumn column : renderColumns) {
            column.render(context, x, y);
            x += column.totalWidth;
        }
    }

    private void renderHeader(DrawContext context) {

    }
}
