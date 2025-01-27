package tab.bettertab;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tab.bettertab.config.ConfigScreen;
import tab.bettertab.mixin.PlayerListHudAccess;

import static tab.bettertab.ConfigSystem.configFile;

public class BetterTab implements ModInitializer {
	public static final String MOD_ID = "better-tab";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static PlayerList playerList = new PlayerList();
	public static double tabScroll = 0;

	public static final KeyBinding toggleMod = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			Text.translatable("tab.bettertab.keybind.toggle_mod").getString(),
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_UNKNOWN,
			Text.translatable("tab.bettertab.keybind.title").getString()
	));
	public static final KeyBinding openConfig = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			Text.translatable("tab.bettertab.keybind.open_config").getString(),
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_N,
			Text.translatable("tab.bettertab.keybind.title").getString()
	));
	public static final KeyBinding rightScroll = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			Text.translatable("tab.bettertab.keybind.scroll_right").getString(),
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_RIGHT,
			Text.translatable("tab.bettertab.keybind.title").getString()
	));
	public static final KeyBinding leftScroll = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			Text.translatable("tab.bettertab.keybind.scroll_left").getString(),
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_LEFT,
			Text.translatable("tab.bettertab.keybind.title").getString()
	));

	@Override
	public void onInitialize() {
		new ConfigSystem().checkConfig();

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (openConfig.wasPressed()) {
				MinecraftClient.getInstance().setScreen(new ConfigScreen(null));
			}
			if (toggleMod.wasPressed()) {
				boolean modEnabled = configFile.getAsJsonObject().get("enable_mod").getAsBoolean();
				configFile.getAsJsonObject().addProperty("enable_mod", !modEnabled);
				new ConfigSystem().saveElementFiles(configFile);
				new Tools().sendToast("BetterTab has been toggled!", "The mod is now " + (modEnabled ? "disabled." : "enabled."));
				client.inGameHud.getPlayerListHud().setVisible(false);
			}
			if (((PlayerListHudAccess)client.inGameHud.getPlayerListHud()).getVisible()) {
				if (rightScroll.wasPressed()) {
					tabScroll ++;
				}
				if (leftScroll.wasPressed()) {
					tabScroll --;
				}
			}
		});
	}
}