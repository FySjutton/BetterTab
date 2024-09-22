package tab.bettertab.config;

import com.google.gson.JsonObject;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tab.GridScreenTab;
import net.minecraft.client.gui.tab.Tab;
import net.minecraft.client.gui.tab.TabManager;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.TabNavigationWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

import static tab.bettertab.ConfigSystem.configFile;
import tab.bettertab.ConfigSystem;

public class ConfigScreen extends Screen {
    private final TabManager tabManager = new TabManager(this::addDrawableChild, this::remove);
    private final JsonObject editedConfigFile = new JsonObject();

    public ConfigScreen(Screen screen) {
        super(Text.of("BetterStats"));
    }

    @Override
    public void init() {
        Tab[] tabs = new Tab[1];
        tabs[0] = new newTab(Text.translatable("tab.bettertab.config.tabs.general").getString(), new ArrayList<>(List.of("enable_mod", "render_heads", "render_ping", "use_numeric")));

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

    private class newTab extends GridScreenTab {
        public newTab(String tabName, ArrayList<String> settings) {
            super(Text.of(tabName));
            GridWidget.Adder adder = grid.createAdder(1);

            SettingWidget settingWidget = new SettingWidget(width, height, settings, editedConfigFile);
            adder.add(settingWidget);
        }
    }

    private void saveFile() {
        boolean saved = new ConfigSystem().saveElementFiles(editedConfigFile);
        if (saved) {
            configFile = editedConfigFile;
        }
        close();
    }
}
