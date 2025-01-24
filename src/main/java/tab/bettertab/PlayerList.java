package tab.bettertab;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static tab.bettertab.PlayerManager.tabEntries;

public class PlayerList {

    public void render(MinecraftClient client, DrawContext context, int scaledWindowWidth, Scoreboard scoreboard, @Nullable ScoreboardObjective objective) {
        for (TabEntry tabEntry : tabEntries) {

        }
    }

    private void renderHeader(DrawContext context) {

    }
}
