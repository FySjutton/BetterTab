package tab.bettertab;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tab.bettertab.mixin.PlayerListHudAccess;
import tab.bettertab.mixin.PlayerListHudMixin;

public class BetterTab implements ModInitializer {
	public static final String MOD_ID = "better-tab";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static double tabScroll = 0;

	public static boolean useExamples = false;
	public static boolean renderColumnNumbers = true;
	public static boolean renderPing = true;
	public static boolean useNumericalPing = true;

	public static final KeyBinding rightScroll = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"Scroll Right",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_RIGHT,
			"BetterTab"
	));
	public static final KeyBinding leftScroll = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"Scroll Left",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_LEFT,
			"BetterTab"
	));
	public static final KeyBinding useExamplesBind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"Use Examples",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_KP_3,
			"BetterTab"
	));
	public static final KeyBinding numericalPing = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"Numerical Ping",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_KP_4,
			"BetterTab"
	));

	@Override
	public void onInitialize() {
		new ConfigSystem().checkConfig();

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (((PlayerListHudAccess)client.inGameHud.getPlayerListHud()).getVisible()) {
				if (rightScroll.wasPressed()) {
					tabScroll ++;
				}
				if (leftScroll.wasPressed()) {
					tabScroll --;
				}
				if (useExamplesBind.wasPressed()) {
					useExamples = !useExamples;
				}
				if (numericalPing.wasPressed()) {
					useNumericalPing = !useNumericalPing;
				}
			}
		});
	}
}