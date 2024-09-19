package tab.bettertab.mixin;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

import static tab.bettertab.BetterTab.LOGGER;

@Mixin(PlayerListHud.class)
public abstract class PlayerListHudMixin {

	@Shadow @Final private static Comparator<PlayerListEntry> ENTRY_ORDERING;


	@Shadow private MinecraftClient client;


	@Shadow protected abstract List<PlayerListEntry> collectPlayerEntries();

	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	private void onRender(DrawContext context, int scaledWindowWidth, Scoreboard scoreboard, @Nullable ScoreboardObjective objective, CallbackInfo ci) {
		List<PlayerListEntry> list = this.collectPlayerEntries();

		int entryHeight = 11;

		ArrayList<ArrayList<PlayerListEntry>> columns = new ArrayList<>();
		ArrayList<PlayerListEntry> column = new ArrayList<>();
		ArrayList<Integer> widths = new ArrayList<>();
		ArrayList<Integer> maxPerColumn = new ArrayList<>();

		int difference = 0;
		int windowHeight = client.getWindow().getScaledHeight();
		int windowWidth = client.getWindow().getScaledWidth();

		for (int i = 0; i < list.size(); i++) {
			if (entryHeight * (i - difference) + 10 < windowHeight / 2) {
				widths.add(client.textRenderer.getWidth(list.get(i).getProfile().getName()) + 10);
				column.add(list.get(i));
			} else {
				if (maxPerColumn.stream().mapToInt(a -> a).sum() + Collections.max(widths) < windowWidth) {
					maxPerColumn.add(Collections.max(widths));
					widths = new ArrayList<>();
					columns.add(column);
					column = new ArrayList<>();
					difference = i;
				}
			}
		}

		int totalColWidth = maxPerColumn.stream().mapToInt(a -> a).sum();

//		x = (scaledWindowWidth / 2 - rowWidth / 2) + col * (entryWidth + 5);

		int x = (windowWidth - totalColWidth) / 2;
		for (int i = 0; i < columns.size(); i++) {
			ArrayList<PlayerListEntry> col = columns.get(i);

			int y = 10;
			for (int j = 0; j < col.size(); j++) {
				y += entryHeight;
				context.fill(x, y, x + maxPerColumn.get(i), y + entryHeight, this.client.options.getTextBackgroundColor(553648127));
				RenderSystem.enableBlend();

				// Drawing player name
				String playerName = col.get(j).getProfile().getName();
				context.drawTextWithShadow(this.client.textRenderer, playerName, x, y, col.get(j).getGameMode() == GameMode.SPECTATOR ? -1862270977 : -1);
			}

			x += maxPerColumn.get(i);
		}

		// ABOVE IS REAL!!!!!!!!!


//		// Define how many entries per row you want
//		int entriesPerRow = 4; // Change this to your desired number
//
//		// Define how many rows to render
////		int maxRows = 5; // Change this to your desired number
//
//		// Calculate total rows needed
//		int totalRows = (int) Math.ceil((double) list.size() / entriesPerRow);
////		totalRows = Math.min(totalRows, maxRows); // Limit rows to maxRows
//
//		// Calculate the size of each entry and the starting position
//		int entryHeight = 9; // height of each entry
//		int entryWidth = (scaledWindowWidth - 50) / entriesPerRow; // width of each entry
//		int startY = 10;
//
//		// Calculate the width of the entire row
//		int rowWidth = entriesPerRow * entryWidth + (entriesPerRow - 1) * 5;
//
//		// Render entries
//		int maxEntriesPerColumn = (client.getWindow().getHeight() - startY) / (entryHeight + 1); // Calculate how many entries fit in a column
//		int totalColumns = (int) Math.ceil((double) list.size() / maxEntriesPerColumn); // Calculate how many columns are needed
//
//		for (int col = 0; col < totalColumns; col++) {
//			for (int row = 0; row < maxEntriesPerColumn; row++) {
//				int index = col * maxEntriesPerColumn + row;
//				if (index >= list.size()) {
//					break; // Stop if there are no more players to render
//				}
//
//				PlayerListEntry playerListEntry = list.get(index);
//
//				// Calculate x and y position for each player entry
//				int x = (scaledWindowWidth / 2 - rowWidth / 2) + col * (entryWidth + 5); // Columns for horizontal layout
//				int y = startY + row * (entryHeight + 1); // Rows for vertical layout
//
//				if (y + entryHeight > client.getWindow().getHeight()) {
//					break; // Stop rendering if the next entry goes beyond the screen height
//				}
//
//				// Render the player entry here
//				context.fill(x, y, x + entryWidth, y + entryHeight, this.client.options.getTextBackgroundColor(553648127));
//				RenderSystem.enableBlend();
//
//				// Drawing player name
//				String playerName = playerListEntry.getProfile().getName();
//				context.drawTextWithShadow(this.client.textRenderer, playerName, x, y, playerListEntry.getGameMode() == GameMode.SPECTATOR ? -1862270977 : -1);
//
//				// Optionally render scores or other information here
//			}
//		}

		// Optionally render header and footer here if needed

		ci.cancel(); // Prevent the original render method from executing
	}

	// Add an injection at the head of the render method
	@Inject(method = "collectPlayerEntries", at = @At("RETURN"), cancellable = true)
	private void onCollectPlayerEntries(CallbackInfoReturnable<List<PlayerListEntry>> cir) {
		// Get the original list of player entries
		List<PlayerListEntry> playerList = new ArrayList<>(cir.getReturnValue());

		// Add 100 fake players
		for (int i = 1; i <= 200; i++) {
			String fakePlayerName = "EXAMPLEPLAYER" + i;
			UUID fakeUUID = UUID.nameUUIDFromBytes(fakePlayerName.getBytes());

			GameProfile fakeProfile = new GameProfile(fakeUUID, fakePlayerName);
			PlayerListEntry fakeEntry = new PlayerListEntry(fakeProfile, false); // False = not a real player

			// Add fake entry to the list
			playerList.add(fakeEntry);
		}

		// Set the modified list as the return value
//		cir.setReturnValue(playerList.stream().sorted(ENTRY_ORDERING).limit(80L).toList());
		cir.setReturnValue(playerList);
	}
}