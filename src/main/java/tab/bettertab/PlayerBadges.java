package tab.bettertab;

import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class PlayerBadges {
    public static List<Identifier> getBadges(PlayerListEntry player) {
        List<Identifier> badges = new ArrayList<>();
        // Other mods can use mixins to insert/add badges next to a player's name.
        // Check if "player" should have it, then add an Identifier to the badges list.
        // BetterTab will then render it for you. This could be useful if you, for example,
        // want to display an icon to everyone using your mod/modpack, then BetterTab could
        // be used as a dependency if you'd like to.
        return badges;
    }
}