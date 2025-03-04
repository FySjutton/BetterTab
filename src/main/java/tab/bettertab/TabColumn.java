package tab.bettertab;

import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class TabColumn {
    public ArrayList<TabEntry> entries;
    public int width;

    public TabColumn(ArrayList<TabEntry> entries) {
        this.entries = new ArrayList<>(entries);
        this.width = Collections.max(this.entries.stream().map(entry -> entry.totalWidth).toList());
    }

    public void render(DrawContext context, int startX, int startY) {
        int y = startY;
        for (TabEntry entry : entries) {
            entry.render(context, startX, y);
            y += entry.totalHeight;
        }
    }
}
