package tab.bettertab;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tab.bettertab.config.BetterTabConfig;
import tab.bettertab.mixin.PlayerListHudAccess;

import static tab.bettertab.tabList.TabRenderer.immediatelyUpdate;

import com.mojang.blaze3d.platform.InputConstants;

public class BetterTab implements ModInitializer {
	public static final String MOD_ID = "bettertab";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static double tabScroll = 0;
    private static final KeyMapping.Category category = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("bettertab", "category.bettertab.tab"));

	public static final KeyMapping toggleMod = KeyBindingHelper.registerKeyBinding(new KeyMapping(
			Component.translatable("tab.bettertab.keybind.toggle_mod").getString(),
			InputConstants.Type.KEYSYM,
			GLFW.GLFW_KEY_UNKNOWN,
            category
	));
	public static final KeyMapping openConfig = KeyBindingHelper.registerKeyBinding(new KeyMapping(
			Component.translatable("tab.bettertab.keybind.open_config").getString(),
			InputConstants.Type.KEYSYM,
			GLFW.GLFW_KEY_N,
            category
	));
	public static final KeyMapping rightScroll = KeyBindingHelper.registerKeyBinding(new KeyMapping(
			Component.translatable("tab.bettertab.keybind.scroll_right").getString(),
			InputConstants.Type.KEYSYM,
			GLFW.GLFW_KEY_RIGHT,
            category
	));
	public static final KeyMapping leftScroll = KeyBindingHelper.registerKeyBinding(new KeyMapping(
			Component.translatable("tab.bettertab.keybind.scroll_left").getString(),
			InputConstants.Type.KEYSYM,
			GLFW.GLFW_KEY_LEFT,
            category
	));

	@Override
	public void onInitialize() {
		BetterTabConfig.CONFIG.load();

//		BadgeManager.registerBadgeProvider((a, b) -> {
//			b.add(Identifier.of("bettertab", "/mod_icon.png"));
//		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (openConfig.consumeClick()) {
				Minecraft.getInstance().setScreen(BetterTabConfig.configScreen(null));
			}
			if (toggleMod.consumeClick()) {
				boolean currentValue = BetterTabConfig.CONFIG.instance().enableMod;
				BetterTabConfig.CONFIG.instance().enableMod = !currentValue;
				BetterTabConfig.CONFIG.save();
				new Tools().sendToast("BetterTab has been toggled!", "The mod is now " + (currentValue ? "disabled." : "enabled."));
				client.gui.getTabList().setVisible(false);
			}
			if (((PlayerListHudAccess)client.gui.getTabList()).getVisible()) {
				if (rightScroll.consumeClick()) {
					tabScroll ++;
					immediatelyUpdate = true;
				}
				if (leftScroll.consumeClick()) {
					tabScroll --;
					immediatelyUpdate = true;
				}
			}
		});
	}
}