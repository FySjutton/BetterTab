package tab.bettertab.tabList;

import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.Collections;

public class TabColumn {
    public ArrayList<TabEntry> entries;
    public int width;
    public int totalWidth;
    public int totalHeight;

    public TabColumn(ArrayList<TabEntry> entries) {
        this.entries = new ArrayList<>(entries);
        this.width = Collections.max(this.entries.stream().map(entry -> entry.totalWidth).toList());
        this.totalWidth = width + 1;
        this.totalHeight = this.entries.stream().mapToInt(entry -> entry.totalHeight).sum();
    }

    public void render(DrawContext context, int startX, int startY) {
        int y = startY;
        for (TabEntry entry : entries) {
            entry.render(context, startX + 1, y + 1, width);
            y += entry.totalHeight;
        }
    }
}
