package tab.bettertab.config;

import com.google.gson.JsonObject;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

import static tab.bettertab.ConfigSystem.configFile;
import static tab.bettertab.ConfigSystem.defaultConfig;

public class SettingWidget extends ElementListWidget<SettingWidget.Entry> {
    private final JsonObject editedConfigFile;

    public SettingWidget(int width, int height, ArrayList<String> settings, JsonObject eCF) {
        super(MinecraftClient.getInstance(), width, height - 24 - 35, 24, 25);

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
    protected int getScrollbarPositionX() {
        return width - 15;
    }

    @Override
    public int getRowWidth() {
        return width - 15;
    }

    public class Entry extends ElementListWidget.Entry<Entry> {
        private ButtonWidget button;
        private ButtonWidget resetButton;
        private CustomSliderWidget slider;
        public TextFieldWidget textField;
        public String setting;
        private String displayText;

        private final TextRenderer textRenderer = client.textRenderer;

        public Entry(String setting) {
            this.setting = setting;
            this.displayText = Text.translatable("tab.bettertab.config.option." + setting).getString();

            this.resetButton = ButtonWidget.builder(Text.translatable("tab.bettertab.config.button_text.reset"), btn -> resetButton(this.textField, this.button, this.slider, setting, btn))
                    .dimensions(width / 2 + width / 4 - 50 + 100 + 3, 0, textRenderer.getWidth(Text.translatable("tab.bettertab.config.button_text.reset")) + 7, 20)
                    .build();
            if (setting.contains("_color") || setting.equals("numeric_format") || setting.equals("example_text")) {
                this.textField = new TextFieldWidget(textRenderer, width / 2 + width / 4 - 50, 0, 100, 20, Text.of(setting));
                this.textField.setText(editedConfigFile.get(setting).getAsString());
                this.textField.setChangedListener(newValue -> textChanged(setting, newValue, this.resetButton));
                textChanged(setting, this.textField.getText(), this.resetButton);
            } else if (List.of("max_row_height", "max_width", "example_amount", "start_y", "scroll_indicator_flash_speed", "high_ping_minimum", "medium_ping_minimum").contains(setting)) {
                if (setting.equals("max_row_height") || setting.equals("max_width")) {
                    this.slider = new CustomSliderWidget(width / 2 + width / 4 - 50, 0, 100, 20, Text.empty(), editedConfigFile.get(setting).getAsDouble(), 0.10, 0.95, true, editedConfigFile, setting, resetButton);
                } else if (setting.equals("example_amount") || setting.equals("start_y") || setting.equals("scroll_indicator_flash_speed")) {
                    this.slider = new CustomSliderWidget(width / 2 + width / 4 - 50, 0, 100, 20, Text.empty(), editedConfigFile.get(setting).getAsDouble(), (setting.equals("start_y") ? 0 : 1), (setting.equals("example_amount") ? 500 : (setting.equals("start_y") ? 200 : 4000)), false, editedConfigFile, setting, resetButton);
                } else {
                    this.slider = new CustomSliderWidget(width / 2 + width / 4 - 50, 0, 100, 20, Text.empty(), editedConfigFile.get(setting).getAsDouble(), (setting.equals("medium_ping_minimum") ? 5 : 10), (setting.equals("medium_ping_minimum") ? 2000 : 4000), false, editedConfigFile, setting, resetButton);
                }
                this.slider.updateMessage();
                this.slider.updateResetButton();
            } else {
                this.button = ButtonWidget.builder(Text.empty(), btn -> buttonHandler(btn, setting, this.resetButton))
                        .dimensions(width / 2 + width / 4 - 50, 0, 100, 20)
                        .build();
                displayButtonValue(this.button, setting, resetButton);
            }
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            List<Selectable> children = new ArrayList<>();
            if (button != null) {
                children.add(button);
            }
            if (textField != null) {
                children.add(textField);
            }
            if (slider != null) {
                children.add(slider);
            }
            children.add(resetButton);
            return children;
        }

        @Override
        public List<? extends Element> children() {
            List<Element> children = new ArrayList<>();
            if (button != null) {
                children.add(button);
            }
            if (textField != null) {
                children.add(textField);
            }
            if (slider != null) {
                children.add(slider);
            }
            children.add(resetButton);
            return children;
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            if (button != null) {
                button.setY(y);
                button.render(context, mouseX, mouseY, tickDelta);
            }
            if (textField != null) {
                textField.setY(y);
                textField.render(context, mouseX, mouseY, tickDelta);
            }
            if (slider != null) {
                slider.setY(y);
                slider.render(context, mouseX, mouseY, tickDelta);
            }
            resetButton.setY(y);
            resetButton.render(context, mouseX, mouseY, tickDelta);
            context.drawCenteredTextWithShadow(textRenderer, displayText, width / 4, y + entryHeight / 2, 0xFFFFFF);
        }
    }

    private void buttonHandler(ButtonWidget button, String setting, ButtonWidget resetButton) {
        if (setting.equals("column_numbers")) {
            int newValue = editedConfigFile.get(setting).getAsInt();
            editedConfigFile.addProperty(setting, (newValue == 0 ? 1 : (newValue == 1 ? 2 : 0)));
        } else if (setting.equals("scroll_type")) {
            int newValue = editedConfigFile.get(setting).getAsInt();
            editedConfigFile.addProperty(setting, (newValue == 0 ? 1 : 0));
        } else {
            boolean newValue = !editedConfigFile.get(setting).getAsBoolean();
            editedConfigFile.addProperty(setting, newValue);
        }
        displayButtonValue(button, setting, resetButton);
    }

    private void displayButtonValue(ButtonWidget button, String setting, ButtonWidget resetButton) {
        Text result;
        if (setting.equals("column_numbers")) {
            int newValue = editedConfigFile.get(setting).getAsInt();
            result = Text.of(newValue == 0 ? "Disabled" : (newValue == 1 ? "On Scroll" : "Always"));
            resetButton.active = newValue != defaultConfig.get(setting).getAsInt();
        } else if (setting.equals("scroll_type")) {
            int newValue = editedConfigFile.get(setting).getAsInt();
            result = Text.of(newValue == 0 ? "Column" : "Page");
            resetButton.active = newValue != defaultConfig.get(setting).getAsInt();
        } else {
            boolean newValue = editedConfigFile.get(setting).getAsBoolean();
            result = Text.translatable("tab.bettertab.config.button_text." + (newValue ? "on" : "off"));
            resetButton.active = newValue != defaultConfig.get(setting).getAsBoolean();
        }
        button.setMessage(result);
    }

    private void resetButton(TextFieldWidget textField, ButtonWidget button, CustomSliderWidget slider, String setting, ButtonWidget resetButton) {
        if (slider != null) {
            slider.setValueFromDisplayNumber(defaultConfig.get(setting).getAsDouble());
            slider.updateMessage();
            slider.applyValue();
            resetButton.active = false;
        } else if (textField != null) {
            textField.setText(defaultConfig.get(setting).getAsString());
            resetButton.active = false;
        } else if (button != null) {
            if (new ArrayList<>(List.of("column_numbers", "scroll_type")).contains(setting)) {
                editedConfigFile.addProperty(setting, defaultConfig.get(setting).getAsInt());
            } else {
                editedConfigFile.addProperty(setting, defaultConfig.get(setting).getAsBoolean());
            }
            displayButtonValue(button, setting, resetButton);
        }
    }

    private void textChanged(String setting, String newSvalue, ButtonWidget resetButton) {
        resetButton.active = !newSvalue.equals(defaultConfig.get(setting).getAsString());
    }
}