package tab.bettertab.config;

import com.google.gson.JsonObject;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

import static tab.bettertab.ConfigSystem.configFile;

public class SettingWidget extends ElementListWidget<SettingWidget.Entry> {
    private final JsonObject editedConfigFile;

    public SettingWidget(int width, int height, ArrayList<String> settings, JsonObject eCF) {
        super(MinecraftClient.getInstance(), width, height - 24 - 35, 24, 20);

        editedConfigFile = eCF;
        JsonObject obj = configFile.getAsJsonObject();
        for (String key : obj.keySet()) {
            editedConfigFile.add(key, obj.get(key));
        }

        for (String setting : settings) {
            addEntry(new Entry(setting));
        }
    }

    @Override
    protected int getScrollbarX() {
        return width - 15;
    }

    @Override
    public int getRowWidth() {
        return width - 15;
    }

    public class Entry extends ElementListWidget.Entry<Entry> {
        private ButtonWidget button;
        private String setting;
        private String displayText;

        private final TextRenderer textRenderer = client.textRenderer;

        public Entry(String setting) {
            this.setting = setting;
            this.displayText = Text.translatable("tab.bettertab.config.option." + setting).getString();

            this.button = ButtonWidget.builder(Text.empty(), btn -> buttonHandler(btn, setting))
                .dimensions(width / 2 + width / 4 - 50, 0, 100, 20)
                .build();
            displayButtonValue(this.button, setting);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            List<Selectable> children = new ArrayList<>();
            if (button != null) {
                children.add(button);
            }
            return children;
        }

        @Override
        public List<? extends Element> children() {
            List<Element> children = new ArrayList<>();
            if (button != null) {
                children.add(button);
            }
            return children;
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            if (button != null) {
                button.setY(y);
                button.render(context, mouseX, mouseY, tickDelta);
            }
            context.drawCenteredTextWithShadow(textRenderer, displayText, width / 4, y + entryHeight / 2, 0xFFFFFF);
        }
    }

    private void buttonHandler(ButtonWidget button, String setting) {
        if (setting.equals("enable_mod")) {
            editedConfigFile.addProperty(setting, !editedConfigFile.get(setting).getAsBoolean());
        }
        displayButtonValue(button, setting);
    }

    private void displayButtonValue(ButtonWidget button, String setting) {
        Text result = null;
        if (setting.equals("enable_mod")) {
            result = Text.translatable("tab.bettertab.config.button_text." + (editedConfigFile.get(setting).getAsBoolean() ? "on" : "off"));
        }
        button.setMessage(Text.of(result));
    }
}