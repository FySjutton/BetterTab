package tab.bettertab.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;

import java.awt.*;

public class BetterTabConfig {
    public static final ConfigClassHandler<BetterTabConfig> CONFIG = ConfigClassHandler.createBuilder(BetterTabConfig.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("bettertab.json"))
                    .build())
            .build();

    // General
    @SerialEntry public boolean enableMod = true;
    @SerialEntry public float maxWidth = 0.9f;
    @SerialEntry public float maxColumnWidth = 0.5f;
    @SerialEntry public float maxColumnHeight = 0.5f;

    @SerialEntry public boolean renderHeader = true;
    @SerialEntry public boolean renderFooter = true;
    @SerialEntry public boolean renderHeads = true;
    @SerialEntry public boolean renderPing = true;
    @SerialEntry public boolean renderBadges = true;
    @SerialEntry public boolean renderScoreboardNumber = true;
    @SerialEntry public boolean renderScrollIndicator = true;
    @SerialEntry public RenderColumnNumberEnum renderColumnNumbers = RenderColumnNumberEnum.Always;

    @SerialEntry public boolean numericalPing = true;
    @SerialEntry public boolean forceClientFirst = false;
    @SerialEntry public boolean saveScroll = false;
    @SerialEntry public boolean scrollWithMouse = true;
    @SerialEntry public boolean hotBarScroll = true;

    // Colors
    @SerialEntry public Color backgroundColor = new Color(0x80000000, true);
    @SerialEntry public Color cellColor = new Color(0x20FFFFFF, true);
    @SerialEntry public Color nameColor = new Color(0xFFFFFFFF, true);
    @SerialEntry public Color spectatorColor = new Color(0x90FFFFFF, true);
    @SerialEntry public Color scrollIndicatorColor = new Color(0xFFFFFFFF, true);
    @SerialEntry public Color columnNumberColor = new Color(0x66FFFFFF, true);
    @SerialEntry public Color emptyLineColor = new Color(0x25FFFFFF, true);

    @SerialEntry public Color pingColorNone = new Color(0xFFB0B0B0, true);
    @SerialEntry public Color pingColorLow = new Color(0xFF7EFF70, true);
    @SerialEntry public Color pingColorMedium = new Color(0xFFFFCD70, true);
    @SerialEntry public Color pingColorHigh = new Color(0xFFFF7070, true);

    // Advanced
    @SerialEntry public int refreshCooldown = 250;

    @SerialEntry public boolean useExamples = false;
    @SerialEntry public String exampleText = "ExamplePlayer%d";
    @SerialEntry public int exampleAmount = 200;

    @SerialEntry public String numericFormat = "%dms";
    @SerialEntry public int mediumPingMin = 150;
    @SerialEntry public int highPingMin = 300;

    @SerialEntry public int scrollIndicatorSpeed = 530;
    @SerialEntry public String healthFormat = "%dhp";

    public static Screen configScreen(Screen parent) {
        return YetAnotherConfigLib.create(CONFIG, ((defaults, config, builder) -> builder
                .title(Text.translatable("tab.bettertab.config.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("tab.bettertab.config.category.general"))

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("tab.bettertab.config.option.enable_mod"))
                                .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.enable_mod")))
                                .binding(true, () -> config.enableMod, newVal -> config.enableMod = newVal)
                                .controller(opt -> BooleanControllerBuilder.create(opt).coloured(true))
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("tab.bettertab.config.group.ping_colors"))
                                .option(Option.<Float>createBuilder()
                                        .name(Text.translatable("tab.bettertab.config.option.max_width"))
                                        .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.max_width")))
                                        .binding(0.9f, () -> config.maxWidth, newVal -> config.maxWidth = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                    .range(0.3f, 0.95f)
                                                    .step(0.01f)
                                                    .formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
                                        .build())
                                .option(Option.<Float>createBuilder()
                                        .name(Text.translatable("tab.bettertab.config.option.max_column_width"))
                                        .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.max_column_width")))
                                        .binding(0.5f, () -> config.maxColumnWidth, newVal -> config.maxColumnWidth = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .range(0.1f, 1f)
                                                .step(0.01f)
                                                .formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
                                        .build())
                                .option(Option.<Float>createBuilder()
                                        .name(Text.translatable("tab.bettertab.config.option.max_column_height"))
                                        .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.max_column_height")))
                                        .binding(0.5f, () -> config.maxColumnHeight, newVal -> config.maxColumnHeight = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .range(0.2f, 0.8f)
                                                .step(0.01f)
                                                .formatValue(val -> Text.of(String.format("%.0f", val * 100) + "%")))
                                        .build())
                            .build())

                        // Rendering
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("tab.bettertab.config.group.rendering"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("tab.bettertab.config.option.render_header"))
                                        .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.render_header")))
                                        .binding(true, () -> config.renderHeader, newVal -> config.renderHeader = newVal)
                                        .controller(opt -> BooleanControllerBuilder.create(opt).coloured(true))
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("tab.bettertab.config.option.render_footer"))
                                        .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.render_footer")))
                                        .binding(true, () -> config.renderFooter, newVal -> config.renderFooter = newVal)
                                        .controller(opt -> BooleanControllerBuilder.create(opt).coloured(true))
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("tab.bettertab.config.option.render_heads"))
                                        .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.render_heads")))
                                        .binding(true, () -> config.renderHeads, newVal -> config.renderHeads = newVal)
                                        .controller(opt -> BooleanControllerBuilder.create(opt).coloured(true))
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("tab.bettertab.config.option.render_ping"))
                                        .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.render_ping")))
                                        .binding(true, () -> config.renderPing, newVal -> config.renderPing = newVal)
                                        .controller(opt -> BooleanControllerBuilder.create(opt).coloured(true))
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("tab.bettertab.config.option.render_badges"))
                                        .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.render_badges")))
                                        .binding(true, () -> config.renderBadges, newVal -> config.renderBadges = newVal)
                                        .controller(opt -> BooleanControllerBuilder.create(opt).coloured(true))
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("tab.bettertab.config.option.render_scoreboard_numbers"))
                                        .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.render_scoreboard_numbers")))
                                        .binding(true, () -> config.renderScoreboardNumber, newVal -> config.renderScoreboardNumber = newVal)
                                        .controller(opt -> BooleanControllerBuilder.create(opt).coloured(true))
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("tab.bettertab.config.option.render_scroll_indicator"))
                                        .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.render_scroll_indicator")))
                                        .binding(true, () -> config.renderScrollIndicator, newVal -> config.renderScrollIndicator = newVal)
                                        .controller(opt -> BooleanControllerBuilder.create(opt).coloured(true))
                                        .build())
                                .option(Option.<RenderColumnNumberEnum>createBuilder()
                                        .name(Text.translatable("tab.bettertab.config.option.render_column_numbers"))
                                        .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.render_column_numbers")))
                                        .binding(RenderColumnNumberEnum.Always, () -> config.renderColumnNumbers, newVal -> config.renderColumnNumbers = newVal)
                                        .controller(opt -> EnumControllerBuilder.create(opt).enumClass(RenderColumnNumberEnum.class))
                                        .build())
                                .build())
                        // Other
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("tab.bettertab.config.group.other"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("tab.bettertab.config.option.use_numerical_ping"))
                                        .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.use_numerical_ping")))
                                        .binding(true, () -> config.numericalPing, newVal -> config.numericalPing = newVal)
                                        .controller(opt -> BooleanControllerBuilder.create(opt).coloured(true))
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("tab.bettertab.config.option.force_client_first"))
                                        .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.force_client_first")))
                                        .binding(false, () -> config.forceClientFirst, newVal -> config.forceClientFirst = newVal)
                                        .controller(opt -> BooleanControllerBuilder.create(opt).coloured(true))
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("tab.bettertab.config.option.save_scroll"))
                                        .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.save_scroll")))
                                        .binding(false, () -> config.saveScroll, newVal -> config.saveScroll = newVal)
                                        .controller(opt -> BooleanControllerBuilder.create(opt).coloured(true))
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("tab.bettertab.config.option.scroll_with_mouse"))
                                        .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.scroll_with_mouse")))
                                        .binding(true, () -> config.scrollWithMouse, newVal -> config.scrollWithMouse = newVal)
                                        .controller(opt -> BooleanControllerBuilder.create(opt).coloured(true))
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("tab.bettertab.config.option.hotbar_scroll"))
                                        .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.hotbar_scroll")))
                                        .binding(true, () -> config.hotBarScroll, newVal -> config.hotBarScroll = newVal)
                                        .controller(opt -> BooleanControllerBuilder.create(opt).coloured(true))
                                        .build())
                            .build())

                    .build())

                // Colors
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("tab.bettertab.config.category.colors"))

                        .option(Option.<Color>createBuilder()
                                .name(Text.translatable("tab.bettertab.config.option.background_color"))
                                .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.background_color")))
                                .binding(new Color(0x80000000, true), () -> config.backgroundColor, newVal -> config.backgroundColor = newVal)
                                .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                .build())
                        .option(Option.<Color>createBuilder()
                                .name(Text.translatable("tab.bettertab.config.option.cell_color"))
                                .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.cell_color")))
                                .binding(new Color(0x20FFFFFF, true), () -> config.cellColor, newVal -> config.cellColor = newVal)
                                .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                .build())
                        .option(Option.<Color>createBuilder()
                                .name(Text.translatable("tab.bettertab.config.option.name_color"))
                                .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.name_color")))
                                .binding(new Color(0xFFFFFFFF, true), () -> config.nameColor, newVal -> config.nameColor = newVal)
                                .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                .build())
                        .option(Option.<Color>createBuilder()
                                .name(Text.translatable("tab.bettertab.config.option.spectator_name_color"))
                                .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.spectator_name_color")))
                                .binding(new Color(0x90FFFFFF, true), () -> config.spectatorColor, newVal -> config.spectatorColor = newVal)
                                .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                .build())
                        .option(Option.<Color>createBuilder()
                                .name(Text.translatable("tab.bettertab.config.option.scroll_indicator_color"))
                                .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.scroll_indicator_color")))
                                .binding(new Color(0xFFFFFFFF, true), () -> config.scrollIndicatorColor, newVal -> config.scrollIndicatorColor = newVal)
                                .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                .build())
                        .option(Option.<Color>createBuilder()
                                .name(Text.translatable("tab.bettertab.config.option.column_number_color"))
                                .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.column_number_color")))
                                .binding(new Color(0x66FFFFFF, true), () -> config.columnNumberColor, newVal -> config.columnNumberColor = newVal)
                                .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                .build())
                        .option(Option.<Color>createBuilder()
                                .name(Text.translatable("tab.bettertab.config.option.empty_line_color"))
                                .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.empty_line_color")))
                                .binding(new Color(0x25FFFFFF, true), () -> config.emptyLineColor, newVal -> config.emptyLineColor = newVal)
                                .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("tab.bettertab.config.group.ping_text_colors"))
                                .option(Option.<Color>createBuilder()
                                        .name(Text.translatable("tab.bettertab.config.option.no_ping_color"))
                                        .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.no_ping_color")))
                                        .binding(new Color(0xFFB0B0B0, true), () -> config.pingColorNone, newVal -> config.pingColorNone = newVal)
                                        .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.translatable("tab.bettertab.config.option.low_ping_color"))
                                        .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.low_ping_color")))
                                        .binding(new Color(0xFF7EFF70, true), () -> config.pingColorLow, newVal -> config.pingColorLow = newVal)
                                        .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.translatable("tab.bettertab.config.option.medium_ping_color"))
                                        .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.medium_ping_color")))
                                        .binding(new Color(0xFFFFCD70, true), () -> config.pingColorMedium, newVal -> config.pingColorMedium = newVal)
                                        .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.translatable("tab.bettertab.config.option.high_ping_color"))
                                        .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.high_ping_color")))
                                        .binding(new Color(0xFFFF7070, true), () -> config.pingColorHigh, newVal -> config.pingColorHigh = newVal)
                                        .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                        .build())
                            .build())
                    .build())

                // Advanced
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("tab.bettertab.config.category.advanced"))

                        .option(Option.<Integer>createBuilder()
                                .name(Text.translatable("tab.bettertab.config.option.refresh_cooldown"))
                                .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.refresh_cooldown")))
                                .binding(250, () -> config.refreshCooldown, newVal -> config.refreshCooldown = newVal)
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                        .range(0, 2000)
                                        .step(1)
                                        .formatValue(val -> Text.of(val + "ms")))
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("tab.bettertab.config.group.examples"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("tab.bettertab.config.option.use_examples"))
                                        .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.use_examples")))
                                        .binding(false, () -> config.useExamples, newVal -> config.useExamples = newVal)
                                        .controller(opt -> BooleanControllerBuilder.create(opt).coloured(true))
                                        .build())
                                .option(Option.<String>createBuilder()
                                        .name(Text.translatable("tab.bettertab.config.option.example_text"))
                                        .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.example_text")))
                                        .binding("ExamplePlayer%d", () -> config.exampleText, newVal -> config.exampleText = newVal)
                                        .controller(StringControllerBuilder::create)
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.translatable("tab.bettertab.config.option.example_amount"))
                                        .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.example_amount")))
                                        .binding(200, () -> config.exampleAmount, newVal -> config.exampleAmount = newVal)
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .range(1, 2000)
                                                .step(1))
                                        .build())
                            .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("tab.bettertab.config.group.ping"))
                                .option(Option.<String>createBuilder()
                                        .name(Text.translatable("tab.bettertab.config.option.numeric_ping_format"))
                                        .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.numeric_ping_format")))
                                        .binding("%dms", () -> config.numericFormat, newVal -> config.numericFormat = newVal)
                                        .controller(StringControllerBuilder::create)
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.translatable("tab.bettertab.config.option.medium_ping_minimum"))
                                        .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.medium_ping_minimum")))
                                        .binding(150, () -> config.mediumPingMin, newVal -> config.mediumPingMin = newVal)
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .range(20, 1500)
                                                .step(1)
                                                .formatValue(val -> Text.of(val + "ms")))
                                    .build())

                                .option(Option.<Integer>createBuilder()
                                        .name(Text.translatable("tab.bettertab.config.option.high_ping_minimum"))
                                        .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.high_ping_minimum")))
                                        .binding(300, () -> config.highPingMin, newVal -> config.highPingMin = newVal)
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .range(200, 2000)
                                                .step(1)
                                                .formatValue(val -> Text.of(val + "ms")))
                                        .build())
                                    .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("tab.bettertab.config.group.other"))
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.translatable("tab.bettertab.config.option.scrolling_indicator_flash_speed"))
                                        .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.scrolling_indicator_flash_speed")))
                                        .binding(530, () -> config.scrollIndicatorSpeed, newVal -> config.scrollIndicatorSpeed = newVal)
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .range(100, 2000)
                                                .step(10))
                                        .build())
                                .option(Option.<String>createBuilder()
                                        .name(Text.translatable("tab.bettertab.config.option.scoreboard_health_text"))
                                        .description(OptionDescription.of(Text.translatable("tab.bettertab.config.option.desc.scoreboard_health_text")))
                                        .binding("%dhp", () -> config.healthFormat, newVal -> config.healthFormat = newVal)
                                        .controller(StringControllerBuilder::create)
                                        .build())
                            .build())
                    .build())
            )).generateScreen(parent);
    }

    public enum RenderColumnNumberEnum implements NameableEnum {
        Always,
        On_Scroll,
        Never;

        @Override
        public Text getDisplayName() {
            return Text.literal(name().replace("_", " ")).formatted(name().equals("Always") ? Formatting.GREEN : (name().equals("On_Scroll") ? Formatting.YELLOW : Formatting.RED));
        }
    }
}