package tab.bettertab.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;
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

import tab.bettertab.config.BetterTabConfig;
import tab.bettertab.tabList.TabUpdater;
import tab.bettertab.Tools;
import static tab.bettertab.BetterTab.*;
import static tab.bettertab.tabList.TabRenderer.immediatelyUpdate;

@Mixin(PlayerListHud.class)
public abstract class PlayerListHudMixin {
	@Shadow @Final private static Comparator<PlayerListEntry> ENTRY_ORDERING;
	@Shadow private MinecraftClient client;
	@Shadow protected abstract List<PlayerListEntry> collectPlayerEntries();
	@Shadow @Nullable private Text header;
	@Shadow @Nullable private Text footer;
	@Shadow private boolean visible;
	@Unique private long lastCheck = 0;

	@Unique private BetterTabConfig config;

	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	private void onRender(DrawContext context, int scaledWindowWidth, Scoreboard scoreboard, @Nullable ScoreboardObjective objective, CallbackInfo ci) {
		if (config.enableMod) {
			if (immediatelyUpdate || (lastCheck + 250 < System.currentTimeMillis())) {
				TabUpdater.update(client, this.collectPlayerEntries(), this.header, this.footer, scoreboard, objective);
				lastCheck = System.currentTimeMillis();
				immediatelyUpdate = false;
			}

			playerList.render(client, context, scaledWindowWidth, scoreboard, objective);
			ci.cancel();
		}
	}

	@Inject(method = "collectPlayerEntries", at = @At("HEAD"), cancellable = true)
	private void onCollectPlayerEntries(CallbackInfoReturnable<List<PlayerListEntry>> cir) {
		cir.setReturnValue(Tools.getPlayerEntries(client, config.enableMod, false, 0, "", ENTRY_ORDERING));
	}

	@Inject(method = "setVisible", at = @At("HEAD"))
	private void onEnable(boolean visible, CallbackInfo ci) {
		if (this.visible != visible) {
			config = BetterTabConfig.CONFIG.instance();
		}
	}
}