package tab.bettertab.tabList;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import tab.bettertab.mixin.PlayerListHudMixin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static tab.bettertab.BetterTab.tabScroll;

public class TabUpdater {
    public static ArrayList<TabColumn> renderColumns = new ArrayList<>();

    public static int totalWidth;
    public static int totalHeight;
    public static int columnsHeight;

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
        if (playerEntries.isEmpty()) {
            renderColumns = new ArrayList<>();
            return;
        }

        maxScreenWidth = client.getWindow().getScaledWidth() - 10;
        if (header != null) {
            headerList = client.textRenderer.wrapLines(header, maxScreenWidth);
        } else {
            headerList = new ArrayList<>();
        }
        int headerHeight = 5 + headerList.size() * client.textRenderer.fontHeight;
        if (footer != null) {
            footerList = client.textRenderer.wrapLines(footer, maxScreenWidth);
        } else {
            footerList = new ArrayList<>();
        }
        int footerHeight = footerList.size() * client.textRenderer.fontHeight + 5;


        // Generate all tab entries
        List<TabEntry> tabEntries = new ArrayList<>();

        int maxColumnWidth = client.textRenderer.getWidth("W".repeat(32));
        int maxColumnHeight = (client.getWindow().getScaledHeight() - headerHeight - footerHeight) / 2;

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

        int colTotWidth = columnsWidth + offset + 10;
        int serverInfoWidth = Math.max(
                headerList.isEmpty() ? 0 : Collections.max(headerList.stream().map(client.textRenderer::getWidth).toList()),
                footerList.isEmpty() ? 0 : Collections.max(footerList.stream().map(client.textRenderer::getWidth).toList())
        ) + 10;

        boolean useColumnWidth = colTotWidth > serverInfoWidth;
        totalWidth = useColumnWidth ? colTotWidth : serverInfoWidth;

        columnsHeight = Collections.max(renderColumns.stream().map(col -> col.totalHeight).toList());
        totalHeight = headerHeight + columnsHeight + footerHeight;

        footerStartY = totalHeight - footerHeight + 13;
        startBoxX = (client.getWindow().getScaledWidth() - totalWidth) / 2;
        startBoxY = 10;
        if (useColumnWidth) {
            startTextX = startBoxX + 5 + (canScrollLeft ? 10 : 0);
        } else {
            startTextX = startBoxX + ((totalWidth - columnsWidth) / 2);
        }
        startTextY = headerHeight;
    }
}
