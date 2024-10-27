package tab.bettertab.config;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.screen.ScreenTexts;

public class SettingWidgetWrapper extends ClickableWidget implements ParentElement {
	private final SettingWidget settingWidget;

	public SettingWidgetWrapper(SettingWidget settingWidget) {
		super(0, settingWidget.getY(), settingWidget.getWidth(), settingWidget.getHeight(), ScreenTexts.EMPTY);
		this.settingWidget = settingWidget;
	}

	public final boolean isDragging() {
		return settingWidget.isDragging();
	}

	public final void setDragging(boolean dragging) {
		settingWidget.setDragging(dragging);
	}

	@Nullable
	public Element getFocused() {
		return settingWidget.getFocused();
	}

	public void setFocused(@Nullable Element focused) {
		settingWidget.setFocused(focused);
	}

	@Nullable
	public GuiNavigationPath getNavigationPath(GuiNavigation navigation) {
		return settingWidget.getNavigationPath(navigation);
	}

	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		return settingWidget.mouseClicked(mouseX, mouseY, button);
	}

	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		return settingWidget.mouseReleased(mouseX, mouseY, button);
	}

	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		return settingWidget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}
	
	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		settingWidget.mouseMoved(mouseX, mouseY);;
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		return settingWidget.mouseScrolled(mouseX, mouseY, amount);
	}

	public boolean isFocused() {
		return settingWidget.isFocused();
	}

	public void setFocused(boolean focused) {
		settingWidget.setFocused(focused);
	}

	@Override
	protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
		settingWidget.render(context, mouseX, mouseY, delta);
	}

	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder) {
		settingWidget.appendNarrations(builder);
	}

	@Override
	public List<? extends Element> children() {
		return settingWidget.children();
	}
}
