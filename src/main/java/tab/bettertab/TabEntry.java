package tab.bettertab;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;

public class TabEntry {
    public Text name;
    public int textWidth;
    public int textHeight;

    public TabEntry(MinecraftClient client, Text name, int width, int height) {
        this.name = name;
        this.textWidth = width;
        this.textHeight = height;
    }
}
