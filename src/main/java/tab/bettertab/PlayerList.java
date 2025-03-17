package tab.bettertab;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.StringVisitable;
import org.jetbrains.annotations.Nullable;

import static tab.bettertab.BetterTab.LOGGER;
import static tab.bettertab.PlayerManager.*;

public class PlayerList {
    public static boolean immediatelyUpdate = false;

    public void render(MinecraftClient client, DrawContext context, int scaledWindowWidth, Scoreboard scoreboard, @Nullable ScoreboardObjective objective) {
        TextRenderer textRenderer = client.textRenderer;

        int x = startTextX;
        int y = 10;

        context.fill(startBoxX, y, startBoxX + totalWidth, y + totalHeight, 0x80000000);

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
