package tab.bettertab;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.hud.PlayerListHud;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tab.bettertab.mixin.PlayerListHudMixin;

public class BetterTab implements ModInitializer {
	public static final String MOD_ID = "better-tab";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
	}
}