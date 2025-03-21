package tab.bettertab.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class BetterTabConfig {
    public static final ConfigClassHandler<BetterTabConfig> CONFIG = ConfigClassHandler.createBuilder(BetterTabConfig.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("bettertab.json"))
                    .build())
            .build();

    @SerialEntry public boolean enableMod = true;

    @SerialEntry public boolean renderHeader = true; // xxx
    @SerialEntry public boolean renderFooter = true; // xxx
    @SerialEntry public boolean renderHeads = true;
    @SerialEntry public boolean renderBadges = true; // xxx
    @SerialEntry public boolean renderScoreboardNumber = true; // xxx

    public static Screen configScreen(Screen parent) {
        return YetAnotherConfigLib.create(CONFIG, ((defaults, config, builder) -> builder
                .title(Text.of("BetterTab"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.of("General"))

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.of("Enable Mod"))
                                .description(OptionDescription.of(Text.of("Enable and disable the mod")))
                                .binding(true, () -> config.enableMod, newVal -> config.enableMod = newVal)
                                .controller(BooleanControllerBuilder::create)
                                .build())

                        // Rendering
                        .group(OptionGroup.createBuilder()
                                .name(Text.of("Rendering"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.of("Render Header"))
                                        .description(OptionDescription.of(Text.of("Whether the header is to be rendered")))
                                        .binding(true, () -> config.renderHeader, newVal -> config.renderHeader = newVal)
                                        .controller(BooleanControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.of("Render Footer"))
                                        .description(OptionDescription.of(Text.of("Whether the footer is to be rendered")))
                                        .binding(true, () -> config.renderFooter, newVal -> config.renderFooter = newVal)
                                        .controller(BooleanControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.of("Render Heads"))
                                        .description(OptionDescription.of(Text.of("Whether the header is to be rendered")))
                                        .binding(true, () -> config.renderHeads, newVal -> config.renderHeads = newVal)
                                        .controller(BooleanControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.of("Render Badges"))
                                        .description(OptionDescription.of(Text.of("Whether the header is to be rendered")))
                                        .binding(true, () -> config.renderBadges, newVal -> config.renderBadges = newVal)
                                        .controller(BooleanControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.of("Render Scoreboard Number"))
                                        .description(OptionDescription.of(Text.of("Whether the scoreboard number is to be rendered")))
                                        .binding(true, () -> config.renderScoreboardNumber, newVal -> config.renderScoreboardNumber = newVal)
                                        .controller(BooleanControllerBuilder::create)
                                        .build())
                                .build())


//                        .group(OptionGroup.createBuilder()
//                                .name(Text.of("testOption"))
//                                .option(Option.createBuilder(boolean.class)
//                                        .name(Text.of("testname"))
//                                        .description(OptionDescription.of(Text.of("testdesc")))
//                                        .binding(false, () -> config.test, newVal -> config.test = newVal)
//                                        .controller(BooleanControllerBuilder::create)
//                                        .build())
//                        .build())
                    .build()
                ))).generateScreen(parent);
    }
}