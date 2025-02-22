package tab.bettertab;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tab.Tab;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.fasterxml.jackson.databind.type.LogicalType.Collection;
import static tab.bettertab.BetterTab.LOGGER;
import static tab.bettertab.BetterTab.tabScroll;

public class PlayerManager {
    public static ArrayList<TabColumn> renderColumns = new ArrayList<>();

    public void update(MinecraftClient client, List<PlayerListEntry> playerEntries) {
        List<TabEntry> tabEntries = new ArrayList<>();
        TextRenderer textRenderer = client.textRenderer;

        int scaledWindowWidth = client.getWindow().getScaledWidth();

        int maxColumnWidth = scaledWindowWidth / 2;
        int maxColumnHeight = client.getWindow().getHeight() / 3;

        for (PlayerListEntry entry : playerEntries) {
            Text displayName = entry.getDisplayName();
            displayName = displayName != null ? displayName : Text.of(entry.getProfile().getName());

            List<OrderedText> lines = textRenderer.wrapLines(displayName, maxColumnWidth);
            int entryWidth = Collections.max(lines.stream().map(textRenderer::getWidth).toList());
            int entryHeight = lines.size() * textRenderer.fontHeight;

            TabEntry tabEntry = new TabEntry(client, displayName, entryWidth, entryHeight);
            tabEntries.add(tabEntry);
        }

        ArrayList<TabColumn> columns = new ArrayList<>();
        ArrayList<TabEntry> columnEntries = new ArrayList<>();
        int columnHeight = 0;

        for (TabEntry tabEntry : tabEntries) {
            if (columnHeight + tabEntry.textHeight > maxColumnHeight) {
                columns.add(new TabColumn(columnEntries));
                columnEntries.clear();
                columnHeight = 0;
            }

            columnHeight += tabEntry.textHeight;
            columnEntries.add(tabEntry);
        }
        if (!columnEntries.isEmpty()) {
            columns.add(new TabColumn(columnEntries));
        }

        int scroll = Math.max(0, (int) tabScroll);

        int availableWidth = client.getWindow().getScaledWidth();

        int startIndex = scroll;
        int endIndex = startIndex;

        while (availableWidth - columns.get(endIndex).width > 0) {
            availableWidth -= columns.get(endIndex).width;
            endIndex ++;
        }

        while (startIndex > 0 && availableWidth - columns.get(startIndex - 1).width > 0) {
            availableWidth -= columns.get(startIndex).width;
            startIndex--;
        }

        LOGGER.info(String.valueOf(tabScroll));
        tabScroll = Math.max(0, Math.min(tabScroll, startIndex));

        renderColumns = new ArrayList<>(columns.subList(startIndex, Math.min(endIndex, columns.size() - 1)));
    }
}
