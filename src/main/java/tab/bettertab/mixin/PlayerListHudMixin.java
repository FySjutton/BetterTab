package tab.bettertab.mixin;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

import tab.bettertab.FakePlayer;
import tab.bettertab.PlayerManager;
import tab.bettertab.TabEntry;
import tab.bettertab.Tools;
import static tab.bettertab.BetterTab.*;
import static tab.bettertab.ConfigSystem.configFile;

@Mixin(PlayerListHud.class)
public abstract class PlayerListHudMixin {

	@Shadow @Final private static Comparator<PlayerListEntry> ENTRY_ORDERING;
	@Shadow private MinecraftClient client;
	@Shadow protected abstract List<PlayerListEntry> collectPlayerEntries();
	@Shadow @Nullable private Text header;
	@Shadow @Nullable private Text footer;
	@Shadow private boolean visible;
	@Shadow public abstract Text getPlayerName(PlayerListEntry entry);
	@Shadow protected abstract void renderLatencyIcon(DrawContext context, int width, int x, int y, PlayerListEntry entry);

	@Unique private boolean showArrows = false;
	@Unique private long lastCheck = 0;

	@Unique private boolean ENABLE_MOD;
	@Unique private int SCROLL_TYPE;
	@Unique private double MAX_ROW_HEIGHT;
	@Unique private double MAX_WIDTH;
	@Unique private boolean RENDER_HEADS;
	@Unique private boolean RENDER_HEADER;
	@Unique private boolean RENDER_FOOTER;
	@Unique private boolean RENDER_PING;
	@Unique private boolean USE_NUMERIC;
	@Unique private boolean SCROLL_INDICATORS;
	@Unique private String NUMERIC_FORMAT;
	@Unique private int COLUMN_NUMBERS;
	@Unique private int START_Y;
	@Unique private int BACKGROUND_COLOR;
	@Unique private int CELL_COLOR;
	@Unique private int NAME_COLOR;
	@Unique private int SPECTATOR_COLOR;
	@Unique private int SCROLL_INDICATOR_COLOR;
	@Unique private int PING_COLOR_NONE;
	@Unique private int PING_COLOR_LOW;
	@Unique private int PING_COLOR_MEDIUM;
	@Unique private int PING_COLOR_HIGH;
	@Unique private int EMPTY_CELL_LINE_COLOR;
	@Unique private int COLUMN_NUMBER_COLOR;
	@Unique private int MEDIUM_PING_MINIMUM;
	@Unique private int HIGH_PING_MINIMUM;
	@Unique private boolean USE_EXAMPLES;
	@Unique private String EXAMPLE_TEXT;
	@Unique private int EXAMPLE_AMOUNT;
	@Unique private int SCROLL_INDICATOR_FLASH_SPEED;

	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	private void onRender(DrawContext context, int scaledWindowWidth, Scoreboard scoreboard, @Nullable ScoreboardObjective objective, CallbackInfo ci) {
		if (lastCheck + 250 < System.currentTimeMillis()) {
			new PlayerManager().update(client, this.collectPlayerEntries());
			lastCheck = System.currentTimeMillis();
		}

		playerList.render(client, context, scaledWindowWidth, scoreboard, objective);
		ci.cancel();
	}

	@Inject(method = "collectPlayerEntries", at = @At("HEAD"), cancellable = true)
	private void onCollectPlayerEntries(CallbackInfoReturnable<List<PlayerListEntry>> cir) {
		List<PlayerListEntry> playerList = new ArrayList<>(client.player.networkHandler.getListedPlayerListEntries().stream().sorted(ENTRY_ORDERING).toList());

		if (ENABLE_MOD) {
			if (USE_EXAMPLES) {
				for (int i = 0; i < EXAMPLE_AMOUNT; i++) {
					playerList.add(new FakePlayer(String.format(EXAMPLE_TEXT, i + 1)));
				}
			}
			cir.setReturnValue(playerList);
		} else {
			cir.setReturnValue(playerList.stream().sorted(ENTRY_ORDERING).limit(80L).toList());
		}
	}

	@Inject(method = "setVisible", at = @At("HEAD"))
	private void onEnable(boolean visible, CallbackInfo ci) {
		if (this.visible != visible) {
			if (!configFile.getAsJsonObject().get("save_scroll").getAsBoolean()) {
				tabScroll = 0;
			}
			ENABLE_MOD = configFile.getAsJsonObject().get("enable_mod").getAsBoolean();
			SCROLL_TYPE = configFile.getAsJsonObject().get("scroll_type").getAsInt();
			MAX_ROW_HEIGHT = configFile.getAsJsonObject().get("max_row_height").getAsDouble();
			MAX_WIDTH = configFile.getAsJsonObject().get("max_width").getAsDouble();
			RENDER_HEADS = configFile.getAsJsonObject().get("render_heads").getAsBoolean();
			RENDER_HEADER = configFile.getAsJsonObject().get("render_header").getAsBoolean();
			RENDER_FOOTER = configFile.getAsJsonObject().get("render_footer").getAsBoolean();
			RENDER_PING = configFile.getAsJsonObject().get("render_ping").getAsBoolean();
			USE_NUMERIC = configFile.getAsJsonObject().get("use_numeric").getAsBoolean();
			SCROLL_INDICATORS = configFile.getAsJsonObject().get("scroll_indicators").getAsBoolean();
			NUMERIC_FORMAT = configFile.getAsJsonObject().get("numeric_format").getAsString();
			COLUMN_NUMBERS = configFile.getAsJsonObject().get("column_numbers").getAsInt();
			START_Y = configFile.getAsJsonObject().get("start_y").getAsInt();
			BACKGROUND_COLOR = new Tools().parseColor(configFile.getAsJsonObject().get("background_color").getAsString());
			EMPTY_CELL_LINE_COLOR = new Tools().parseColor(configFile.getAsJsonObject().get("empty_cell_line_color").getAsString());
			SCROLL_INDICATOR_COLOR = new Tools().parseColor(configFile.getAsJsonObject().get("scroll_indicator_color").getAsString());
			COLUMN_NUMBER_COLOR = new Tools().parseColor(configFile.getAsJsonObject().get("column_number_color").getAsString());
			CELL_COLOR = new Tools().parseColor(configFile.getAsJsonObject().get("cell_color").getAsString());
			NAME_COLOR = new Tools().parseColor(configFile.getAsJsonObject().get("name_color").getAsString());
			SPECTATOR_COLOR = new Tools().parseColor(configFile.getAsJsonObject().get("spectator_color").getAsString());
			PING_COLOR_NONE = new Tools().parseColor(configFile.getAsJsonObject().get("ping_color_none").getAsString());
			PING_COLOR_LOW = new Tools().parseColor(configFile.getAsJsonObject().get("ping_color_low").getAsString());
			PING_COLOR_MEDIUM = new Tools().parseColor(configFile.getAsJsonObject().get("ping_color_medium").getAsString());
			PING_COLOR_HIGH = new Tools().parseColor(configFile.getAsJsonObject().get("ping_color_high").getAsString());
			MEDIUM_PING_MINIMUM = configFile.getAsJsonObject().get("medium_ping_minimum").getAsInt();
			HIGH_PING_MINIMUM = configFile.getAsJsonObject().get("high_ping_minimum").getAsInt();
			USE_EXAMPLES = configFile.getAsJsonObject().get("use_examples").getAsBoolean();
			EXAMPLE_TEXT = configFile.getAsJsonObject().get("example_text").getAsString();
			EXAMPLE_AMOUNT = configFile.getAsJsonObject().get("example_amount").getAsInt();
			SCROLL_INDICATOR_FLASH_SPEED = configFile.getAsJsonObject().get("scroll_indicator_flash_speed").getAsInt();
		}
	}


}