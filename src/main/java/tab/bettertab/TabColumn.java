package tab.bettertab;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class TabColumn {
    public ArrayList<TabEntry> entries;
    public int width;

    public TabColumn(ArrayList<TabEntry> entries) {
        this.entries = new ArrayList<>(entries);
        this.width = Collections.max(this.entries.stream().map(entry -> entry.textWidth).toList());
    }
}
