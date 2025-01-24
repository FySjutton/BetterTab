package tab.bettertab;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tab.Tab;
import net.minecraft.client.network.PlayerListEntry;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {
    public static List<TabEntry> tabEntries = new ArrayList<>();

    public void update(MinecraftClient client, List<PlayerListEntry> playerEntries) {
        List<TabEntry> newTabEntries = new ArrayList<>();

        for (PlayerListEntry entry : playerEntries) {
            TabEntry tabEntry = new TabEntry(client, entry, entry.getDisplayName());
            newTabEntries.add(tabEntry);
        }

        tabEntries = newTabEntries;
    }
}
