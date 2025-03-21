package tab.bettertab.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.*;

public class BetterTabConfig {
    public static final ConfigClassHandler<BetterTabConfig> CONFIG = ConfigClassHandler.createBuilder(BetterTabConfig.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("bettertab.json"))
                    .build())
            .build();

    @SerialEntry public boolean enableMod = true;
    @SerialEntry public float maxWidth = 0.9f;
    @SerialEntry public float maxColumnWidth = 0.5f;
    @SerialEntry public float maxColumnHeight = 0.5f;
    @SerialEntry public boolean scrollWithMouse = true;

    @SerialEntry public boolean renderHeader = true;
    @SerialEntry public boolean renderFooter = true;
    @SerialEntry public boolean renderHeads = true;
    @SerialEntry public boolean renderBadges = true;
    @SerialEntry public boolean renderScoreboardNumber = true;

    @SerialEntry public Color backgroundColor = new Color(0x80000000, true);
    @SerialEntry public Color cellColor = new Color(0x20FFFFFF, true);
    @SerialEntry public Color nameColor = new Color(0xFFFFFFFF, true);
    @SerialEntry public Color spectatorColor = new Color(0x90FFFFFF, true);
    @SerialEntry public Color scrollIndicatorColor = new Color(0xFFFFFFFF, true);

    public static Screen configScreen(Screen parent) {
        return YetAnotherConfigLib.create(CONFIG, ((defaults, config, builder) -> builder
                .title(Text.of("BetterTab"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.of("General"))

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.of("Enable Mod"))
                                .description(OptionDescription.of(Text.of("Enable and disable the mod")))
                                .binding(true, () -> config.enableMod, newVal -> config.enableMod = newVal)
                                .controller(opt -> BooleanControllerBuilder.create(opt).coloured(true))
                                .build())
                        .option(Option.<Float>createBuilder()
                                .name(Text.of("Max Width"))
                                .description(OptionDescription.of(Text.of("The maximum width of the player list")))
                                .binding(0.9f, () -> config.maxWidth, newVal -> config.maxWidth = newVal)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                            .range(0.3f, 0.95f)
                                            .step(0.01f)
                                            .formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
                                .build())
                        .option(Option.<Float>createBuilder()
                                .name(Text.of("Max Column Width"))
                                .description(OptionDescription.of(Text.of("The maximum width of a column")))
                                .binding(0.5f, () -> config.maxColumnWidth, newVal -> config.maxColumnWidth = newVal)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .range(0.1f, 1f)
                                        .step(0.01f)
                                        .formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
                                .build())
                        .option(Option.<Float>createBuilder()
                                .name(Text.of("Max Column Height"))
                                .description(OptionDescription.of(Text.of("The maximum height of a column")))
                                .binding(0.5f, () -> config.maxColumnHeight, newVal -> config.maxColumnHeight = newVal)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .range(0.2f, 0.8f)
                                        .step(0.01f)
                                        .formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.of("Scroll with mouse"))
                                .description(OptionDescription.of(Text.of("If you can use the mouse to scroll on tab")))
                                .binding(true, () -> config.scrollWithMouse, newVal -> config.scrollWithMouse = newVal)
                                .controller(opt -> BooleanControllerBuilder.create(opt).coloured(true))
                                .build())

                        // Rendering
                        .group(OptionGroup.createBuilder()
                                .name(Text.of("Rendering"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.of("Render Header"))
                                        .description(OptionDescription.of(Text.of("Whether the header is to be rendered")))
                                        .binding(true, () -> config.renderHeader, newVal -> config.renderHeader = newVal)
                                        .controller(opt -> BooleanControllerBuilder.create(opt).coloured(true))
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.of("Render Footer"))
                                        .description(OptionDescription.of(Text.of("Whether the footer is to be rendered")))
                                        .binding(true, () -> config.renderFooter, newVal -> config.renderFooter = newVal)
                                        .controller(opt -> BooleanControllerBuilder.create(opt).coloured(true))
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.of("Render Heads"))
                                        .description(OptionDescription.of(Text.of("Whether the header is to be rendered")))
                                        .binding(true, () -> config.renderHeads, newVal -> config.renderHeads = newVal)
                                        .controller(opt -> BooleanControllerBuilder.create(opt).coloured(true))
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.of("Render Badges"))
                                        .description(OptionDescription.of(Text.of("Whether the header is to be rendered")))
                                        .binding(true, () -> config.renderBadges, newVal -> config.renderBadges = newVal)
                                        .controller(opt -> BooleanControllerBuilder.create(opt).coloured(true))
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.of("Render Scoreboard Number"))
                                        .description(OptionDescription.of(Text.of("Whether the scoreboard number is to be rendered")))
                                        .binding(true, () -> config.renderScoreboardNumber, newVal -> config.renderScoreboardNumber = newVal)
                                        .controller(opt -> BooleanControllerBuilder.create(opt).coloured(true))
                                        .build())
                                .build())
                    .build())
                // Colors
                .category(ConfigCategory.createBuilder()
                        .name(Text.of("Colors"))

                        .option(Option.<Color>createBuilder()
                                .name(Text.of("Background Color"))
                                .description(OptionDescription.of(Text.of("The background color")))
                                .binding(new Color(0x80000000, true), () -> config.backgroundColor, newVal -> config.backgroundColor = newVal)
                                .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                .build())
                        .option(Option.<Color>createBuilder()
                                .name(Text.of("Cell Color"))
                                .description(OptionDescription.of(Text.of("The cell color")))
                                .binding(new Color(0x20FFFFFF, true), () -> config.cellColor, newVal -> config.cellColor = newVal)
                                .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                .build())
                        .option(Option.<Color>createBuilder()
                                .name(Text.of("Name Color"))
                                .description(OptionDescription.of(Text.of("The name color")))
                                .binding(new Color(0xFFFFFFFF, true), () -> config.nameColor, newVal -> config.nameColor = newVal)
                                .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                .build())
                        .option(Option.<Color>createBuilder()
                                .name(Text.of("Spectator Name Color"))
                                .description(OptionDescription.of(Text.of("The spectator name color")))
                                .binding(new Color(0x90FFFFFF, true), () -> config.spectatorColor, newVal -> config.spectatorColor = newVal)
                                .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                .build())
                        .option(Option.<Color>createBuilder()
                                .name(Text.of("Scroll Indicator Color"))
                                .description(OptionDescription.of(Text.of("The scroll indicator color")))
                                .binding(new Color(0xFFFFFFFF, true), () -> config.scrollIndicatorColor, newVal -> config.scrollIndicatorColor = newVal)
                                .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                .build())
                    .build())
            )).generateScreen(parent);
    }
}