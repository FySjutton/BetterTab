package tab.bettertab.tabList;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.RenderLayer;
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
    private int iconY;

    private List<Identifier> badges;

    public TabEntry(MinecraftClient client, PlayerListEntry entry, int maxColumnWidth) {
        this.client = client;
        this.maxColumnWidth = maxColumnWidth;
        TextRenderer textRenderer = client.textRenderer;

        Text displayName = entry.getDisplayName();
        name = displayName != null ? displayName : Text.of(entry.getProfile().getName());
        hasName = name == null || !name.getString().isEmpty();

        if (hasName) {
            List<OrderedText> lines = textRenderer.wrapLines(name, maxColumnWidth);
            textHeight = textRenderer.getWrappedLinesHeight(name, maxColumnWidth);
            textWidth = Collections.max(lines.stream().map(textRenderer::getWidth).toList());
        }

        badges = BadgeManager.getBadges(entry);
        int badgeWidth = badges.size() * 10;

        renderHead = true;
        headTexture = entry.getSkinTextures().texture();
        iconY = (textHeight - 8) / 2 + 1;

        textStartX = 2  + (renderHead ? 10 : 0);

        totalWidth = (2 + textWidth + badgeWidth + (renderHead ? 2 + 8 + 2 : 0) + 2);
        totalHeight = (2 + textHeight);
    }

    public void render(DrawContext context, int x1, int y1, int columnWidth) {
        context.fill(x1, y1, x1 + columnWidth, y1 + textHeight + 1, 0x20FFFFFF);

        x1 += 2;
        for (Identifier badge : badges) {
            context.drawTexture(RenderLayer::getGuiTextured, badge, x1, y1 + iconY, 0, 0, 8, 8, 8, 8);
            x1 += 10;
        }

        if (renderHead) {
            PlayerSkinDrawer.draw(context, headTexture, x1, y1 + iconY, 8, true, false, -1);
        }

        if (hasName) {
            context.drawWrappedText(client.textRenderer, name, x1 + textStartX, y1 + 2, maxColumnWidth, 0xFFFFFFFF, true);
        }
    }
}
