package tab.bettertab.tabList;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.client.multiplayer.PlayerInfo;

public class FakePlayer extends PlayerInfo {
    public FakePlayer(String fakePlayerName) {
        super(new GameProfile(UUID.nameUUIDFromBytes(fakePlayerName.getBytes()), fakePlayerName), false);
    }
}
