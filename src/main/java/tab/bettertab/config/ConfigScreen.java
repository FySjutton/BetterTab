package tab.bettertab.config;

import com.google.gson.JsonObject;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tab.GridScreenTab;
import net.minecraft.client.gui.tab.Tab;
import net.minecraft.client.gui.tab.TabManager;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

import static tab.bettertab.ConfigSystem.configFile;
import tab.bettertab.ConfigSystem;

public class ConfigScreen extends Screen {
    private final TabManager tabManager = new TabManager(this::addDrawableChild, this::remove);
    private final JsonObject editedConfigFile = new JsonObject();
    private final Screen PARENT;

    public ConfigScreen(Screen screen) {
        super(Text.of("BetterStats"));
        this.PARENT = screen;
    }

    @Override
    public void init() {
        Tab[] tabs = new Tab[4];
        tabs[0] = new newTab(Text.translatable("tab.bettertab.config.tabs.general").getString(), new ArrayList<>(List.of("enable_mod", "scroll_type", "max_row_height", "max_width", "render_heads", "render_header", "render_footer", "column_numbers", "scroll_indicators", "start_y", "scroll_with_mouse")));
        tabs[1] = new newTab(Text.translatable("tab.bettertab.config.tabs.styling").getString(), new ArrayList<>(List.of("background_color", "cell_color", "name_color", "spectator_color", "column_number_color", "empty_cell_line_color", "scroll_indicator_color")));
        tabs[2] = new newTab(Text.translatable("tab.bettertab.config.tabs.ping").getString(), new ArrayList<>(List.of("render_ping", "use_numeric", "numeric_format", "ping_color_none", "ping_color_low", "ping_color_medium", "ping_color_high", "high_ping_minimum", "medium_ping_minimum")));
        tabs[3] = new newTab(Text.translatable("tab.bettertab.config.tabs.advanced").getString(), new ArrayList<>(List.of("save_scroll", "scroll_indicator_flash_speed", "use_examples", "example_text", "example_amount")));

        TabNavigationWidget tabNavigation = TabNavigationWidget.builder(this.tabManager, this.width).tabs(tabs).build();
        this.addDrawableChild(tabNavigation);

        ButtonWidget saveButton = ButtonWidget.builder(Text.translatable("tab.bettertab.config.button_text.done"), btn -> saveFile()).dimensions(width / 4, height - 25, width / 2, 20).build();
        this.addDrawableChild(saveButton);

        tabNavigation.selectTab(0, false);
        tabNavigation.init();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public void close() {
        client.setScreen(PARENT);
    }

    private class newTab extends GridScreenTab {
        public SettingWidget settingWidget;
        public newTab(String tabName, ArrayList<String> settings) {
            super(Text.of(tabName));
            GridWidget.Adder adder = grid.createAdder(1);

            settingWidget = new SettingWidget(width, height, settings, editedConfigFile);
            adder.add(settingWidget);
        }
    }

    private void saveFile() {
        for (Element tab : ((TabNavigationWidget) this.children().get(0)).children()) {
            newTab tabElm = (newTab) ((TabButtonWidget) tab).getTab();
            for (SettingWidget.Entry a : tabElm.settingWidget.children()) {
                if (a.textField != null) {
                    editedConfigFile.addProperty(a.setting, a.textField.getText());
                }
            }
        }
        boolean saved = new ConfigSystem().saveElementFiles(editedConfigFile);
        if (saved) {
            configFile = editedConfigFile;
        }
        close();
    }
}
