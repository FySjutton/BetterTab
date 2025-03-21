package tab.bettertab;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Nullables;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.NotNull;
import tab.bettertab.config.BetterTabConfig;
import tab.bettertab.tabList.FakePlayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static tab.bettertab.BetterTab.LOGGER;

public class Tools {
    public void sendToast(String title, String description) {
        try {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.textRenderer != null) {
                client.getToastManager().clear();
                client.getToastManager().add(
                        new SystemToast(SystemToast.Type.PERIODIC_NOTIFICATION,
                                Text.literal(title),
                                Text.literal(description)
                        )
                );
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to display toast! Toast title: " + title + ", toast description: " + description + ". Error:");
            LOGGER.error(String.valueOf(e));
        }
    }

    public static List<PlayerListEntry> getPlayerEntries(MinecraftClient client, boolean ENABLE_MOD, boolean USE_EXAMPLES, int EXAMPLE_AMOUNT, String EXAMPLE_TEXT, Comparator<PlayerListEntry> ENTRY_ORDERING) {
        Comparator<PlayerListEntry> comparator = getPlayerListEntryComparator(client);

        List<PlayerListEntry> playerList = new ArrayList<>(client.player.networkHandler.getListedPlayerListEntries().stream().sorted(comparator).toList());

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

    private static @NotNull Comparator<PlayerListEntry> getPlayerListEntryComparator(MinecraftClient client) {
        UUID clientUUID = client.player.getUuid();
        boolean forceClientFirst = BetterTabConfig.CONFIG.instance().forceClientFirst;

        return Comparator
                .comparingInt((PlayerListEntry entry) -> (forceClientFirst && entry.getProfile().getId().equals(clientUUID)) ? Integer.MIN_VALUE : -entry.getListOrder())
                .thenComparingInt((entry) -> entry.getGameMode() == GameMode.SPECTATOR ? 1 : 0)
                .thenComparing((entry) -> Nullables.mapOrElse(entry.getScoreboardTeam(), Team::getName, ""))
                .thenComparing((entry) -> entry.getProfile().getName(), String::compareToIgnoreCase);
    }

    public static Text getPlayerName(PlayerListEntry entry) {
        return entry.getDisplayName() != null ? applyGameModeFormatting(entry, entry.getDisplayName().copy()) : applyGameModeFormatting(entry, Team.decorateName(entry.getScoreboardTeam(), Text.literal(entry.getProfile().getName())));
    }

    private static Text applyGameModeFormatting(PlayerListEntry entry, MutableText name) {
        return entry.getGameMode() == GameMode.SPECTATOR ? name.formatted(Formatting.ITALIC) : name;
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
