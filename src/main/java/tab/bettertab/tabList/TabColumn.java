package tab.bettertab.tabList;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import tab.bettertab.config.BetterTabConfig;

import java.util.ArrayList;
import java.util.Collections;

import static tab.bettertab.tabList.TabUpdater.*;

public class TabColumn {
    public MinecraftClient client = MinecraftClient.getInstance();

    public ArrayList<TabEntry> entries;
    public int width;
    public int totalWidth;
    public int totalHeight;

    public boolean renderColumnNumber = false;
    public int columnNumber;
    private final int columnNumberColor = BetterTabConfig.CONFIG.instance().columnNumberColor.getRGB();

    public TabColumn(ArrayList<TabEntry> entries, int columnNumber) {
        this.columnNumber = columnNumber + 1;

        BetterTabConfig.RenderColumnNumberEnum renderColumnNumbers = BetterTabConfig.CONFIG.instance().renderColumnNumbers;
        if (renderColumnNumbers == BetterTabConfig.RenderColumnNumberEnum.Always || (renderColumnNumbers == BetterTabConfig.RenderColumnNumberEnum.On_Scroll && (canScrollLeft || canScrollRight))) {
            renderColumnNumber = true;
        }

        this.entries = new ArrayList<>(entries);
        this.width = Collections.max(this.entries.stream().map(entry -> entry.totalWidth).toList());
        this.totalWidth = width + 1;
        this.totalHeight = this.entries.stream().mapToInt(entry -> entry.totalHeight).sum() + (renderColumnNumber ? 10 : 0);
    }

    public void render(DrawContext context, int startX, int startY) {
        int y = startY;
        for (TabEntry entry : entries) {
            entry.render(context, startX + 1, y + 1, width);
            y += entry.totalHeight;
        }

        if (renderColumnNumber) {
            context.drawCenteredTextWithShadow(client.textRenderer, String.valueOf(columnNumber), startX + (width) / 2, startY + columnsHeight - client.textRenderer.fontHeight + client.textRenderer.fontHeight / 2 - 3, columnNumberColor);
        }
    }
}
