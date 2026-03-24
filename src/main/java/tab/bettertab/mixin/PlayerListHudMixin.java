package tab.bettertab.mixin;

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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import tab.bettertab.config.BetterTabConfig;
import tab.bettertab.tabList.TabRenderer;
import tab.bettertab.tabList.TabUpdater;
import tab.bettertab.Tools;
import static tab.bettertab.BetterTab.*;
import static tab.bettertab.tabList.TabRenderer.immediatelyUpdate;

@Mixin(PlayerTabOverlay.class)
public abstract class PlayerListHudMixin {
	@Shadow @Final private static Comparator<PlayerInfo> PLAYER_COMPARATOR;
	@Shadow private Minecraft minecraft;
	@Shadow protected abstract List<PlayerInfo> getPlayerInfos();
	@Shadow @Nullable private Component header;
	@Shadow @Nullable private Component footer;
	@Shadow private boolean visible;
	@Unique private long lastCheck = 0;

	@Unique private BetterTabConfig config;

	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	private void onRender(GuiGraphics context, int scaledWindowWidth, Scoreboard scoreboard, @Nullable Objective objective, CallbackInfo ci) {
		if (config.enableMod) {
			if (immediatelyUpdate || (lastCheck + BetterTabConfig.CONFIG.instance().refreshCooldown < System.currentTimeMillis())) {
				TabUpdater.update(minecraft, this.getPlayerInfos(), this.header, this.footer, scoreboard, objective);
				lastCheck = System.currentTimeMillis();
				immediatelyUpdate = false;
			}

			TabRenderer.render(minecraft, context, scaledWindowWidth, scoreboard, objective);
			ci.cancel();
		}
	}

	@Inject(method = "getPlayerInfos", at = @At("HEAD"), cancellable = true)
	private void onCollectPlayerEntries(CallbackInfoReturnable<List<PlayerInfo>> cir) {
		cir.setReturnValue(Tools.getPlayerEntries(minecraft, config.enableMod, config.useExamples, config.exampleAmount, config.exampleText, PLAYER_COMPARATOR));
	}

	@Inject(method = "setVisible", at = @At("HEAD"))
	private void onEnable(boolean visible, CallbackInfo ci) {
		if (!this.visible && visible) {
			if (!BetterTabConfig.CONFIG.instance().saveScroll) {
				tabScroll = 0;
			}
			config = BetterTabConfig.CONFIG.instance();
		}
	}
}