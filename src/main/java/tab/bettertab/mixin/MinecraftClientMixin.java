package tab.bettertab.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.hud.PlayerListHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tab.bettertab.PlayerManager;

import static tab.bettertab.BetterTab.tabScroll;
import static tab.bettertab.ConfigSystem.configFile;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "onResolutionChanged", at = @At("HEAD"))
    public void onMouseScroll(CallbackInfo ci) {
        PlayerManager.updateMaxColumns((MinecraftClient) (Object) this);
    }
}