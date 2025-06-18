package tab.bettertab;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tab.bettertab.config.BetterTabConfig;
import tab.bettertab.mixin.PlayerListHudAccess;
import tab.bettertab.tabList.BadgeManager;
import tab.bettertab.tabList.TabRenderer;

import static tab.bettertab.tabList.TabRenderer.immediatelyUpdate;

public class BetterTab implements ModInitializer {
	public static final String MOD_ID = "bettertab";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
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
		BetterTabConfig.CONFIG.load();

		BadgeManager.registerBadgeProvider((a, b) -> {
			b.add(Identifier.of("bettertab", "/mod_icon.png"));
		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (openConfig.wasPressed()) {
				MinecraftClient.getInstance().setScreen(BetterTabConfig.configScreen(null));
			}
			if (toggleMod.wasPressed()) {
				boolean currentValue = BetterTabConfig.CONFIG.instance().enableMod;
				BetterTabConfig.CONFIG.instance().enableMod = !currentValue;
				BetterTabConfig.CONFIG.save();
				new Tools().sendToast("BetterTab has been toggled!", "The mod is now " + (currentValue ? "disabled." : "enabled."));
				client.inGameHud.getPlayerListHud().setVisible(false);
			}
			if (((PlayerListHudAccess)client.inGameHud.getPlayerListHud()).getVisible()) {
				if (rightScroll.wasPressed()) {
					tabScroll ++;
					immediatelyUpdate = true;
				}
				if (leftScroll.wasPressed()) {
					tabScroll --;
					immediatelyUpdate = true;
				}
			}
		});
	}
}