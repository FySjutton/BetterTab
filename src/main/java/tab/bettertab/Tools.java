package tab.bettertab;

import org.jetbrains.annotations.NotNull;
import tab.bettertab.config.BetterTabConfig;
import tab.bettertab.tabList.FakePlayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.Optionull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.GameType;
import net.minecraft.world.scores.PlayerTeam;

import static tab.bettertab.BetterTab.LOGGER;

public class Tools {
    public void sendToast(String title, String description) {
        try {
            Minecraft client = Minecraft.getInstance();
            client.getToastManager().clear();
            client.getToastManager().addToast(
                new SystemToast(SystemToast.SystemToastId.PERIODIC_NOTIFICATION,
                    Component.literal(title),
                    Component.literal(description)
                )
            );
        } catch (Exception e) {
            LOGGER.warn("Failed to display toast! Toast title: " + title + ", toast description: " + description + ". Error:");
            LOGGER.error(String.valueOf(e));
        }
    }

    public static List<PlayerInfo> getPlayerEntries(Minecraft client, boolean ENABLE_MOD, boolean USE_EXAMPLES, int EXAMPLE_AMOUNT, String EXAMPLE_TEXT, Comparator<PlayerInfo> ENTRY_ORDERING) {
        Comparator<PlayerInfo> comparator = getPlayerListEntryComparator(client);

        if (client.player == null) return new ArrayList<>();
        List<PlayerInfo> playerList = new ArrayList<>(client.player.connection.getListedOnlinePlayers().stream().sorted(comparator).toList());

        if (ENABLE_MOD) {
            if (USE_EXAMPLES) {
                for (int i = 0; i < EXAMPLE_AMOUNT; i++) {
                    playerList.add(new FakePlayer(String.format(EXAMPLE_TEXT, i + 1)));
                }
            }
            return playerList;
        } else {
           return playerList.stream().sorted(ENTRY_ORDERING).limit(80L).toList();
        }
    }

    private static @NotNull Comparator<PlayerInfo> getPlayerListEntryComparator(Minecraft client) {
        UUID clientUUID;
        if (client.player != null) {
            clientUUID = client.player.getUUID();
        } else {
            clientUUID = UUID.randomUUID();
        }

        boolean forceClientFirst = BetterTabConfig.CONFIG.instance().forceClientFirst;

        return Comparator
                .comparingInt((PlayerInfo entry) -> (forceClientFirst && entry.getProfile().id().equals(clientUUID)) ? Integer.MIN_VALUE : -entry.getTabListOrder())
                .thenComparingInt((entry) -> entry.getGameMode() == GameType.SPECTATOR ? 1 : 0)
                .thenComparing((entry) -> Optionull.mapOrDefault(entry.getTeam(), PlayerTeam::getName, ""))
                .thenComparing((entry) -> entry.getProfile().name(), String::compareToIgnoreCase);
    }

    public static Component getPlayerName(PlayerInfo entry) {
        return entry.getTabListDisplayName() != null ? applyGameModeFormatting(entry, entry.getTabListDisplayName().copy()) : applyGameModeFormatting(entry, PlayerTeam.formatNameForTeam(entry.getTeam(), Component.literal(entry.getProfile().name())));
    }

    private static Component applyGameModeFormatting(PlayerInfo entry, MutableComponent name) {
        return entry.getGameMode() == GameType.SPECTATOR ? name.withStyle(ChatFormatting.ITALIC) : name;
    }

    public static int numericalColorizer(int ping) {
        if (ping <= 0) {
            return BetterTabConfig.CONFIG.instance().pingColorNone.getRGB();
        } else if (ping >= BetterTabConfig.CONFIG.instance().highPingMin) {
            return BetterTabConfig.CONFIG.instance().pingColorHigh.getRGB();
        } else if (ping >= BetterTabConfig.CONFIG.instance().mediumPingMin) {
            return BetterTabConfig.CONFIG.instance().pingColorMedium.getRGB();
        }
        return BetterTabConfig.CONFIG.instance().pingColorLow.getRGB();
    }
}
