package tab.bettertab;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static tab.bettertab.PlayerManager.renderColumns;
import static tab.bettertab.BetterTab.LOGGER;

public class PlayerList {

    public void render(MinecraftClient client, DrawContext context, int scaledWindowWidth, Scoreboard scoreboard, @Nullable ScoreboardObjective objective) {
        TextRenderer textRenderer = client.textRenderer;

        int x = 0;
        int y = 0;
        for (TabColumn column : renderColumns) {
            column.render(context, x, y);
//            for (TabEntry entry : column.entries) {
//                context.drawWrappedTextWithShadow(textRenderer, entry.name, x, y, column.width, 0xFFFFFFFF);
//                y += entry.textHeight;
//            }
//            y = 0;
            x += column.width;
        }
    }

    private void renderHeader(DrawContext context) {

    }
}
