package tab.bettertab.tabList;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.PlayerListEntry;

import java.util.UUID;

public class FakePlayer extends PlayerListEntry {
    public FakePlayer(String fakePlayerName) {
        super(new GameProfile(UUID.nameUUIDFromBytes(fakePlayerName.getBytes()), fakePlayerName), false);
    }
}
