package tab.bettertab.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.hud.PlayerListHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static tab.bettertab.BetterTab.*;
//import static tab.bettertab.config.ConfigSystem.configFile;
import static tab.bettertab.tabList.TabUpdater.canScrollLeft;
import static tab.bettertab.tabList.TabUpdater.canScrollRight;
import static tab.bettertab.tabList.TabRenderer.immediatelyUpdate;

@Mixin(Mouse.class)
public class MouseMixin {
    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    public void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
//        if (configFile.getAsJsonObject().get("scroll_with_mouse").getAsBoolean()) {
//            MinecraftClient client = MinecraftClient.getInstance();
//            PlayerListHud playerListHud = client.inGameHud.getPlayerListHud();
//
//            if (((PlayerListHudAccess)playerListHud).getVisible() && (canScrollLeft || canScrollRight)) {
//                tabScroll -= vertical;
//                immediatelyUpdate = true;
//                ci.cancel();
//            }
//        }
    }
}
