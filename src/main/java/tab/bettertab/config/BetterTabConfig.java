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

    @SerialEntry public Color pingColorNone = new Color(0xFFB0B0B0, true);
    @SerialEntry public Color pingColorLow = new Color(0xFF7EFF70, true);
    @SerialEntry public Color pingColorMedium = new Color(0xFFFFCD70, true);
    @SerialEntry public Color pingColorHigh = new Color(0xFFFF7070, true);

    // Advanced
    @SerialEntry public int scrollIndicatorSpeed = 530;
    @SerialEntry public boolean useExamples = false;
    @SerialEntry public String exampleText = "ExamplePlayer%d";
    @SerialEntry public int exampleAmount = 200;

    @SerialEntry public String numericFormat = "%dms";
    @SerialEntry public int mediumPingMin = 150;
    @SerialEntry public int highPingMin = 300;

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
                        .group(OptionGroup.createBuilder()
                                .name(Text.of("Ping Colors"))
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
                                        .name(Text.of("Render Ping"))
                                        .description(OptionDescription.of(Text.of("Whether the ping is to be rendered")))
                                        .binding(true, () -> config.renderPing, newVal -> config.renderPing = newVal)
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
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.of("Render Scroll Indicator"))
                                        .description(OptionDescription.of(Text.of("Whether the scroll indicator is to be rendered")))
                                        .binding(true, () -> config.renderScrollIndicator, newVal -> config.renderScrollIndicator = newVal)
                                        .controller(opt -> BooleanControllerBuilder.create(opt).coloured(true))
                                        .build())
                                .option(Option.<RenderColumnNumberEnum>createBuilder()
                                        .name(Text.of("Render Column Numbers"))
                                        .description(OptionDescription.of(Text.of("When the column index should be rendered.")))
                                        .binding(RenderColumnNumberEnum.Always, () -> config.renderColumnNumbers, newVal -> config.renderColumnNumbers = newVal)
                                        .controller(opt -> EnumControllerBuilder.create(opt).enumClass(RenderColumnNumberEnum.class))
                                        .build())
                                .build())
                        // Other
                        .group(OptionGroup.createBuilder()
                                .name(Text.of("Other"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.of("Use Numerical Ping"))
                                        .description(OptionDescription.of(Text.of("If numerical ping should be used, otherwise normal will be used")))
                                        .binding(true, () -> config.numericalPing, newVal -> config.numericalPing = newVal)
                                        .controller(opt -> BooleanControllerBuilder.create(opt).coloured(true))
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.of("Force Client First"))
                                        .description(OptionDescription.of(Text.of("If the client player is to be first on the player list")))
                                        .binding(false, () -> config.forceClientFirst, newVal -> config.forceClientFirst = newVal)
                                        .controller(opt -> BooleanControllerBuilder.create(opt).coloured(true))
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.of("Save Scroll"))
                                        .description(OptionDescription.of(Text.of("If the scroll is to be saved")))
                                        .binding(false, () -> config.saveScroll, newVal -> config.saveScroll = newVal)
                                        .controller(opt -> BooleanControllerBuilder.create(opt).coloured(true))
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.of("Scroll With Mouse"))
                                        .description(OptionDescription.of(Text.of("If you can use the mouse to scroll on tab")))
                                        .binding(true, () -> config.scrollWithMouse, newVal -> config.scrollWithMouse = newVal)
                                        .controller(opt -> BooleanControllerBuilder.create(opt).coloured(true))
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.of("Hotbar Scroll"))
                                        .description(OptionDescription.of(Text.of("Allows you to scroll in your hotbar, if there's only one page.")))
                                        .binding(true, () -> config.hotBarScroll, newVal -> config.hotBarScroll = newVal)
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
                        .option(Option.<Color>createBuilder()
                                .name(Text.of("Column Number Color"))
                                .description(OptionDescription.of(Text.of("The column number color")))
                                .binding(new Color(0x66FFFFFF, true), () -> config.columnNumberColor, newVal -> config.columnNumberColor = newVal)
                                .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.of("Ping Text Colors"))
                                .option(Option.<Color>createBuilder()
                                        .name(Text.of("No Ping"))
                                        .description(OptionDescription.of(Text.of("The color when you have no ping")))
                                        .binding(new Color(0xFFB0B0B0, true), () -> config.pingColorNone, newVal -> config.pingColorNone = newVal)
                                        .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.of("Low Ping"))
                                        .description(OptionDescription.of(Text.of("The color when you have low ping")))
                                        .binding(new Color(0xFF7EFF70, true), () -> config.pingColorLow, newVal -> config.pingColorLow = newVal)
                                        .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.of("Medium Ping"))
                                        .description(OptionDescription.of(Text.of("The color when you have medium ping")))
                                        .binding(new Color(0xFFFFCD70, true), () -> config.pingColorMedium, newVal -> config.pingColorMedium = newVal)
                                        .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.of("High Ping"))
                                        .description(OptionDescription.of(Text.of("The color when you have high ping")))
                                        .binding(new Color(0xFFFF7070, true), () -> config.pingColorHigh, newVal -> config.pingColorHigh = newVal)
                                        .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                        .build())
                            .build())
                    .build())

                // Advanced
                .category(ConfigCategory.createBuilder()
                        .name(Text.of("Advanced"))

                        .group(OptionGroup.createBuilder()
                                .name(Text.of("Examples"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.of("Use Examples"))
                                        .description(OptionDescription.of(Text.of("Whether or not examples should be used")))
                                        .binding(false, () -> config.useExamples, newVal -> config.useExamples = newVal)
                                        .controller(opt -> BooleanControllerBuilder.create(opt).coloured(true))
                                        .build())
                                .option(Option.<String>createBuilder()
                                        .name(Text.of("Example Text"))
                                        .description(OptionDescription.of(Text.of("The name of the example. \"%d\" to get the number.")))
                                        .binding("ExamplePlayer%d", () -> config.exampleText, newVal -> config.exampleText = newVal)
                                        .controller(StringControllerBuilder::create)
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.of("Example Amount"))
                                        .description(OptionDescription.of(Text.of("The amount of examples.")))
                                        .binding(200, () -> config.exampleAmount, newVal -> config.exampleAmount = newVal)
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .range(1, 2000)
                                                .step(1))
                                        .build())
                            .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.of("Ping"))
                                .option(Option.<String>createBuilder()
                                        .name(Text.of("Numeric Ping Format"))
                                        .description(OptionDescription.of(Text.of("The format of the numerical ping text. \"%d\" to get the ping.")))
                                        .binding("%dms", () -> config.numericFormat, newVal -> config.numericFormat = newVal)
                                        .controller(StringControllerBuilder::create)
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.of("Medium Ping Minimum"))
                                        .description(OptionDescription.of(Text.of("At what ping the color switches to \"medium\".")))
                                        .binding(150, () -> config.mediumPingMin, newVal -> config.mediumPingMin = newVal)
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .range(20, 1500)
                                                .step(1)
                                                .formatValue(val -> Text.of(val + "ms")))
                                    .build())

                                .option(Option.<Integer>createBuilder()
                                        .name(Text.of("High Ping Minimum"))
                                        .description(OptionDescription.of(Text.of("At what ping the color switches to \"high\". To be higher than medium.")))
                                        .binding(300, () -> config.highPingMin, newVal -> config.highPingMin = newVal)
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .range(200, 2000)
                                                .step(1)
                                                .formatValue(val -> Text.of(val + "ms")))
                                        .build())
                                    .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.of("Other"))
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.of("Scrolling Indicator Flash Speed"))
                                        .description(OptionDescription.of(Text.of("How fast the scroll indicators blink.")))
                                        .binding(530, () -> config.scrollIndicatorSpeed, newVal -> config.scrollIndicatorSpeed = newVal)
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .range(100, 2000)
                                                .step(10))
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