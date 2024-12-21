package tab.bettertab;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;

public class TabEntry {
    private Text name;
    private int textWidth;

    public TabEntry(MinecraftClient client, PlayerListEntry entry, Text name) {
        this.name = name;
        this.textWidth = client.textRenderer.getWidth(name);
    }
}
