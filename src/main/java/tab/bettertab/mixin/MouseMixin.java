package tab.bettertab.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tab.bettertab.config.BetterTabConfig;

import static tab.bettertab.BetterTab.*;
import static tab.bettertab.tabList.TabUpdater.canScrollLeft;
import static tab.bettertab.tabList.TabUpdater.canScrollRight;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.components.PlayerTabOverlay;

import static tab.bettertab.tabList.TabRenderer.immediatelyUpdate;

@Mixin(MouseHandler.class)
public class MouseMixin {
    @Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
    public void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (BetterTabConfig.CONFIG.instance().scrollWithMouse) {
            Minecraft client = Minecraft.getInstance();
            PlayerTabOverlay playerListHud = client.gui.getTabList();

            if (((PlayerListHudAccess)playerListHud).getVisible() && (!BetterTabConfig.CONFIG.instance().hotBarScroll || (canScrollLeft || canScrollRight))) {
                tabScroll -= vertical;
                immediatelyUpdate = true;
                ci.cancel();
            }
        }
    }
}
