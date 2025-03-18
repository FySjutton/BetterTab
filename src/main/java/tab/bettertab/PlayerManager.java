package tab.bettertab;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

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
    public static int startBoxY;
    public static int startTextX;
    public static int startTextY;

    public static boolean canScrollLeft;
    public static boolean canScrollRight;

    private static int atZeroEndIndex;
    private static boolean wasAtZero;

    public static List<OrderedText> headerList;
    public static List<OrderedText> footerList;
    public static int footerStartY;

    private static int maxScreenWidth;

    public static void update(MinecraftClient client, List<PlayerListEntry> playerEntries, Text header, Text footer) {
        maxScreenWidth = client.getWindow().getScaledWidth() - 10;
        headerList = client.textRenderer.wrapLines(header, maxScreenWidth);
        int headerHeight = 5 + headerList.size() * client.textRenderer.fontHeight;
        footerList = client.textRenderer.wrapLines(footer, maxScreenWidth);
        int footerHeight = footerList.size() * client.textRenderer.fontHeight + 5;

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

        int startIndex = 0;
        int endIndex = 0;
        int availableWidth = client.getWindow().getScaledWidth() - 10 - 30; // margin, arrows

        // If we're at the beginning of the scrolling, go from left to right instead of right to left
        if (tabScroll == 0) {
            while (endIndex < columns.size() && availableWidth - columns.get(endIndex).totalWidth > 0) {
                availableWidth -= columns.get(endIndex).totalWidth;
                endIndex ++;
            }
            atZeroEndIndex = endIndex;
            wasAtZero = true;
        } else {
            if (wasAtZero) {
                tabScroll = atZeroEndIndex + 1;
                wasAtZero = false;
            }
            int scroll = (int) tabScroll;
            startIndex = scroll;
            endIndex = scroll;

            while (startIndex > 0 && availableWidth - columns.get(startIndex - 1).totalWidth > 0) {
                availableWidth -= columns.get(startIndex - 1).totalWidth;
                startIndex --;
            }

            if (startIndex == 0) {
                tabScroll = 0;
                while (endIndex < columns.size() && availableWidth - columns.get(endIndex).totalWidth > 0) {
                    availableWidth -= columns.get(endIndex).totalWidth;
                    endIndex ++;
                }
            }
        }

        renderColumns = new ArrayList<>(columns.subList(startIndex, endIndex));

        canScrollLeft = startIndex > 0;
        canScrollRight = endIndex < columns.size();

        int offset = (canScrollLeft ? 10 : 0) + (canScrollRight ? 10 : 0);
        int columnsWidth = renderColumns.stream().mapToInt(col -> col.totalWidth).sum();
        totalWidth = Math.max(
                columnsWidth + offset + 10,
                Math.max(
                        Collections.max(headerList.stream().map(client.textRenderer::getWidth).toList()),
                        Collections.max(footerList.stream().map(client.textRenderer::getWidth).toList())
                ) + 10
        );
        totalHeight = headerHeight + Collections.max(renderColumns.stream().map(col -> col.totalHeight).toList()) + footerHeight;

        footerStartY = totalHeight - footerHeight;
        startBoxX = (client.getWindow().getScaledWidth() - totalWidth) / 2;
        startBoxY = 10;
        // RENDER IN MIDDLE OF BOX; NOT boxX +5!!!! NOT SUPER EASY...
        startTextX = startBoxX + 5 + (canScrollLeft ? 10 : 0);
        startTextY = headerHeight;
    }
}
