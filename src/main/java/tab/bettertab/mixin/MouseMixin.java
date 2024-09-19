package tab.bettertab.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.hud.PlayerListHud;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static tab.bettertab.BetterTab.LOGGER;

@Mixin(Mouse.class)
public class MouseMixin {

    @Shadow @Final private static Logger LOGGER;

    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    public void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerListHud playerListHud = client.inGameHud.getPlayerListHud();

        if (((PlayerListHudAccess)playerListHud).getVisible()) {
            LOGGER.info("here");
            LOGGER.info(String.valueOf(vertical));
            LOGGER.info(String.valueOf(horizontal));
            LOGGER.info(String.valueOf(window));
            ci.cancel();
        }
    }
}
