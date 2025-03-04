package tab.bettertab;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;

public class TabEntry {
    private MinecraftClient client;

    public int totalWidth;
    public int totalHeight;

    private boolean hasName;
    public Text name;
    public int textWidth = 0;
    public int textHeight = 0;
    private int maxColumnWidth;
    private int textStartX;

    private boolean renderHead;
    private Identifier headTexture;
    private int renderHeadY;

    public TabEntry(MinecraftClient client, PlayerListEntry entry, int maxColumnWidth) {
        this.client = client;
        this.maxColumnWidth = maxColumnWidth;
        TextRenderer textRenderer = client.textRenderer;

        Text displayName = entry.getDisplayName();
        name = displayName != null ? displayName : Text.of(entry.getProfile().getName());
        hasName = name == null || !name.getString().isEmpty();

        if (hasName) {
            List<OrderedText> lines = textRenderer.wrapLines(name, maxColumnWidth);
            textWidth = Collections.max(lines.stream().map(textRenderer::getWidth).toList());
            textHeight = lines.size() * textRenderer.fontHeight;
        }

        renderHead = true;
        headTexture = entry.getSkinTextures().texture();
        renderHeadY = (textHeight - 8) / 2;

        textStartX = 2 + (renderHead ? 10 : 0);

        totalWidth = (2 + textWidth + (renderHead ? 2 + 8 + 2 : 0) + 2);
        totalHeight = (2 + textHeight + 2);
    }

    public void render(DrawContext context, int x1, int y1) {
        context.fill(x1, y1, x1 + totalWidth, y1 + textHeight, 0x20FFFFFF);
        if (renderHead) {
            PlayerSkinDrawer.draw(context, headTexture, x1 + 2, y1 + renderHeadY, 8, true, false, -1);
        }

        if (hasName) {
            context.drawWrappedText(client.textRenderer, name, x1 + textStartX, y1 + 2, maxColumnWidth, 0xFFFFFFFF, true);
        }
    }
}
