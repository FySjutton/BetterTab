package tab.bettertab;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static tab.bettertab.BetterTab.LOGGER;
import static tab.bettertab.BetterTab.tabScroll;

public class PlayerManager {
    public static ArrayList<TabColumn> renderColumns = new ArrayList<>();

    public static int totalWidth;
    public static int totalHeight;

    public static int startBoxX;
    public static int startTextX;

    public static boolean canScrollLeft;
    public static boolean canScrollRight;

    public static void update(MinecraftClient client, List<PlayerListEntry> playerEntries) {
        // Generate all tab entries
        List<TabEntry> tabEntries = new ArrayList<>();

        int maxColumnWidth = client.textRenderer.getWidth("W".repeat(32));
        int maxColumnHeight = client.getWindow().getScaledHeight() / 3;

        for (PlayerListEntry entry : playerEntries) {
            TabEntry tabEntry = new TabEntry(client, entry, maxColumnWidth);
            tabEntries.add(tabEntry);
        }

        // Sort all entries into columns
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

        // Calculate which columns are to be displayed
        tabScroll = Math.min(Math.max(0, tabScroll), columns.size());
        int scroll = (int) tabScroll;

        int availableWidth = client.getWindow().getScaledWidth() - 10 - 30; // margin, arrows
        int startIndex = scroll;
        int endIndex = scroll;

        while (startIndex > 0 && availableWidth - columns.get(startIndex - 1).totalWidth > 0) {
            availableWidth -= columns.get(startIndex - 1).totalWidth;
            startIndex --;
        }

        if (startIndex == 0) {
            while (endIndex < columns.size() && availableWidth - columns.get(endIndex).totalWidth > 0) {
                availableWidth -= columns.get(endIndex).totalWidth;
                endIndex ++;
                tabScroll = endIndex;
            }
        }

        renderColumns = new ArrayList<>(columns.subList(startIndex, endIndex));


        canScrollLeft = startIndex > 0;
        canScrollRight = endIndex < columns.size();

        int offset = (canScrollLeft ? 15 : 0) + (canScrollRight ? 15 : 0);
        int columnsWidth = renderColumns.stream().mapToInt(col -> col.totalWidth).sum();
        totalWidth = columnsWidth + offset;
        totalHeight = Collections.max(renderColumns.stream().map(col -> col.totalHeight).toList());

        startBoxX = (client.getWindow().getScaledWidth() - totalWidth) / 2;
        startTextX = startBoxX + (canScrollLeft ? 15 : 0);
    }
}
