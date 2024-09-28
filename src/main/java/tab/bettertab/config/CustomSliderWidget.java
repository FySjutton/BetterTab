package tab.bettertab.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionSliderWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import static tab.bettertab.BetterTab.LOGGER;
import static tab.bettertab.ConfigSystem.defaultConfig;

public class CustomSliderWidget extends SliderWidget {
    private final double max;
    private final double minimum;
    private final boolean useDecimal;
    private final JsonObject editedConfigFile;
    private final String setting;
    private final ButtonWidget resetButton;

    public CustomSliderWidget(int x, int y, int width, int height, Text text, double value, double minimum, double max, boolean useDecimal, JsonObject editedConfigFile, String setting, ButtonWidget resetButton) {
        super(x, y, width, height, text, value);
        this.max = max;
        this.minimum = minimum;
        this.editedConfigFile = editedConfigFile;
        this.setting = setting;
        this.resetButton = resetButton;
        this.useDecimal = useDecimal;
        setValueFromDisplayNumber(value); // Reverse it back to 0-100 scale
    }

    @Override
    protected void updateMessage() {
        if (useDecimal) {
            this.setMessage(Text.literal(Math.round((minimum + (max - minimum) * this.value) * 100) + "%"));
        } else {
            this.setMessage(Text.literal(String.valueOf((int) Math.round((minimum + (max - minimum) * this.value)))));
        }
    }

    @Override
    protected void applyValue() {
        if (useDecimal) {
            editedConfigFile.addProperty(setting, Math.round((minimum + (max - minimum) * this.value) * 100) / 100.00);
        } else {
            editedConfigFile.addProperty(setting, (int) Math.round((minimum + (max - minimum) * this.value)));
        }
        updateResetButton();
    }

    protected void setValueFromDisplayNumber(double newValue) {
        value = (newValue - minimum) / (max - minimum);
    }

    public void updateResetButton() {
        if (useDecimal) {
            resetButton.active = editedConfigFile.getAsJsonObject().get(setting).getAsDouble() != defaultConfig.get(setting).getAsDouble();
        } else {
            resetButton.active = editedConfigFile.getAsJsonObject().get(setting).getAsInt() != defaultConfig.get(setting).getAsInt();
        }
    }
}