package tab.bettertab.tabList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import com.mojang.authlib.services.FriendsService;
import com.mojang.authlib.services.MinecraftServicesFriendsService;
import com.mojang.authlib.services.request.FriendActionRequest;
import com.mojang.authlib.services.response.FriendData;
import com.mojang.authlib.services.response.FriendDto;
import com.mojang.authlib.services.response.FriendsListResponse;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.util.TriConsumer;

public class BadgeManager {
    private static final List<BiConsumer<PlayerInfo, List<Identifier>>> BADGE_PROVIDERS = new ArrayList<>();

    protected static List<Identifier> getBadges(PlayerInfo player) {
        List<Identifier> badges = new ArrayList<>();

        for (BiConsumer<PlayerInfo, List<Identifier>> provider : BADGE_PROVIDERS) {
            provider.accept(player, badges);
        }

        return badges;
    }

    public static void registerBadgeProvider(BiConsumer<PlayerInfo, List<Identifier>> provider) {
        BADGE_PROVIDERS.add(provider);
    }

    // EXAMPLE:
//    public void badgeCallback() {
//        // You can easily add your own badges by registering a provider like the following example.
//        registerBadgeProvider((player, badgeList) -> {
//            if (player.getProfile().getName().equals("Fy17")) {
//                badgeList.add(Identifier.of("myMod", "textures/gui/example.png"));
//            }
//        });
//    }
}