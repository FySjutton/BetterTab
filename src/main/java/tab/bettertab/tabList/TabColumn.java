package tab.bettertab.tabList;

import tab.bettertab.config.BetterTabConfig;

import java.util.ArrayList;
import java.util.Collections;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import static tab.bettertab.tabList.TabUpdater.*;

public class TabColumn {
    public Minecraft client = Minecraft.getInstance();

    public ArrayList<tab.bettertab.tabList.TabEntry> entries;
    public int width;
    public int totalWidth;
    public int totalHeight;

    public boolean renderColumnNumber = false;
    public int columnNumber;
    private final int columnNumberColor = BetterTabConfig.CONFIG.instance().columnNumberColor.getRGB();
    private final BetterTabConfig.ScrollingType scrollingType = BetterTabConfig.CONFIG.instance().scrollingType;

    public TabColumn(ArrayList<tab.bettertab.tabList.TabEntry> entries, int columnNumber) {
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

    public void render(GuiGraphics context, int startX, int startY) {
        int y = startY;
        for (TabEntry entry : entries) {
            entry.render(context, startX + 1, y + 1, width);
            y += entry.totalHeight;
        }

        if (renderColumnNumber && scrollingType.equals(BetterTabConfig.ScrollingType.Column)) {
            context.drawCenteredString(client.font, String.valueOf(columnNumber), startX + (width) / 2, startY + columnsHeight - client.font.lineHeight + client.font.lineHeight / 2 - 3, columnNumberColor);
        }
    }
}
