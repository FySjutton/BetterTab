package tab.bettertab.config;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Consumer;

public class Wrapper extends ClickableWidget {
    private final SettingWidget widget;

    public Wrapper(SettingWidget widget) {
        super(0, 100, widget.width, widget.height, Text.empty());
        this.widget = widget;
    }

    @Override
    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        widget.render(context, mouseX, mouseY, delta);
    }

    @Override
    public GuiNavigationPath getNavigationPath(GuiNavigation navigation) {
        return widget.getNavigationPath(navigation);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return widget.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return widget.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return widget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return widget.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return widget.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        widget.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return widget.charTyped(chr, modifiers);
    }

    @Override
    public boolean isFocused() {
        return widget.isFocused();
    }

    @Override
    public void setFocused(boolean focused) {
        widget.setFocused(focused);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        widget.appendNarrations(builder);
    }
}