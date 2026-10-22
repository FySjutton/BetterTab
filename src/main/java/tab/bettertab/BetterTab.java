package tab.bettertab;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tab.bettertab.config.BetterTabConfig;
import tab.bettertab.mixin.PlayerListHudAccess;
import tab.bettertab.tabList.BadgeManager;

import java.util.List;

import static tab.bettertab.tabList.TabRenderer.immediatelyUpdate;

public class BetterTab implements ModInitializer {
	public static final String MOD_ID = "bettertab";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static double tabScroll = 0;
    private static final KeyMapping.Category category = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("bettertab", "category.bettertab.tab"));

	public static final KeyMapping toggleMod = new KeyMapping(Component.translatable("tab.bettertab.keybind.toggle_mod").getString(), InputConstants.UNKNOWN.getValue(), category);
	public static final KeyMapping openConfig = new KeyMapping(Component.translatable("tab.bettertab.keybind.open_config").getString(), InputConstants.KEY_N, category);
	public static final KeyMapping rightScroll = new KeyMapping(Component.translatable("tab.bettertab.keybind.scroll_right").getString(), InputConstants.KEY_RIGHT, category);
	public static final KeyMapping leftScroll = new KeyMapping(Component.translatable("tab.bettertab.keybind.scroll_left").getString(), InputConstants.KEY_LEFT, category);

	@Override
	public void onInitialize() {
		BetterTabConfig.CONFIG.load();

//		BadgeManager.registerBadgeProvider((a, b) -> {
//			b.add(Identifier.of("bettertab", "/mod_icon.png"));
//		});

		Minecraft mc = Minecraft.getInstance();
		BadgeManager.registerBadgeProvider((player, badgeList) -> friendCallback(mc, player, badgeList));

		KeyMappingHelper.registerKeyMapping(toggleMod);
		KeyMappingHelper.registerKeyMapping(openConfig);
		KeyMappingHelper.registerKeyMapping(rightScroll);
		KeyMappingHelper.registerKeyMapping(leftScroll);

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (openConfig.consumeClick()) {
				Minecraft.getInstance().gui.setScreen(BetterTabConfig.configScreen(null));
			}
			if (toggleMod.consumeClick()) {
				boolean currentValue = BetterTabConfig.CONFIG.instance().enableMod;
				BetterTabConfig.CONFIG.instance().enableMod = !currentValue;
				BetterTabConfig.CONFIG.save();
				new Tools().sendToast("BetterTab has been toggled!", "The mod is now " + (currentValue ? "disabled." : "enabled."));
				client.gui.hud.getTabList().setVisible(false);
			}
			if (((PlayerListHudAccess)client.gui.hud.getTabList()).getVisible()) {
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

	private void friendCallback(Minecraft mc, PlayerInfo player, List<Identifier> badgeList) {
		if (BetterTabConfig.CONFIG.instance().showFriends) {
			if (!mc.getPlayerSocialManager().getFriends().stream().filter(v -> v.id().equals(player.getProfile().id())).toList().isEmpty()) {
				badgeList.add(Identifier.fromNamespaceAndPath("bettertab", "textures/gui/friend.png"));
			}
		}
	}
}
