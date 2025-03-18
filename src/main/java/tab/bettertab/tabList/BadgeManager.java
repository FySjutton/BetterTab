package tab.bettertab.tabList;

import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class BadgeManager {
    private static final List<BiConsumer<PlayerListEntry, List<Identifier>>> BADGE_PROVIDERS = new ArrayList<>();

    protected static List<Identifier> getBadges(PlayerListEntry player) {
        List<Identifier> badges = new ArrayList<>();

        for (BiConsumer<PlayerListEntry, List<Identifier>> provider : BADGE_PROVIDERS) {
            provider.accept(player, badges);
        }

        return badges;
    }

    public static void registerBadgeProvider(BiConsumer<PlayerListEntry, List<Identifier>> provider) {
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