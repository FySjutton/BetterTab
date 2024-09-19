package tab.bettertab.mixin;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
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

	@Shadow @Nullable private Text header;

	@Shadow @Nullable private Text footer;

	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	private void onRender(DrawContext context, int scaledWindowWidth, Scoreboard scoreboard, @Nullable ScoreboardObjective objective, CallbackInfo ci) {
		List<PlayerListEntry> list = this.collectPlayerEntries();

		int entryHeight = 10;

		ArrayList<ArrayList<PlayerListEntry>> columns = new ArrayList<>();
		ArrayList<PlayerListEntry> column = new ArrayList<>();
		ArrayList<Integer> widths = new ArrayList<>();
		ArrayList<Integer> maxPerColumn = new ArrayList<>();

		int difference = 0;
		int windowHeight = client.getWindow().getScaledHeight();
		int windowWidth = client.getWindow().getScaledWidth();

		int startY = 10;

		for (int i = 0; i < list.size(); i++) {
			if ((entryHeight + 2) * (i - difference) >= windowHeight / 2) {
				if (maxPerColumn.stream().mapToInt(a -> a).sum() + Collections.max(widths) < windowWidth) {
					maxPerColumn.add(Collections.max(widths));
					widths = new ArrayList<>();
					columns.add(column);
					column = new ArrayList<>();
					difference = i;
				}
			}

			Text playerName = list.get(i).getDisplayName() != null ? list.get(i).getDisplayName() : Text.of(list.get(i).getProfile().getName());
			widths.add(client.textRenderer.getWidth(playerName) + 10 + 8 + 2);
			column.add(list.get(i));
		}

		int totalColWidth = maxPerColumn.stream().mapToInt(a -> a + 2).sum();
		int totalRowHeight = columns.get(0).size() * (entryHeight + 1);

		List<OrderedText> headerList = List.of();
		if (this.header != null) {
			headerList = client.textRenderer.wrapLines(this.header, totalColWidth);
		}

		List<OrderedText> footerList = List.of();
		if (this.footer != null) {
			footerList = client.textRenderer.wrapLines(this.footer, totalColWidth);
		}

		int x = (windowWidth - totalColWidth) / 2;
		context.fill(x - 5, startY - 5, x + totalColWidth + 5, startY + headerList.size() * 9 + footerList.size() * 9 + totalRowHeight + 5, Integer.MIN_VALUE);

		for (OrderedText line : headerList) {
			context.drawTextWithShadow(this.client.textRenderer, line, windowWidth / 2 - client.textRenderer.getWidth(line) / 2, startY, -1);
			startY += 9;
		}
		startY -= 9;

		for (int i = 0; i < columns.size(); i++) {
			ArrayList<PlayerListEntry> col = columns.get(i);

			int y = startY;
			for (int j = 0; j < col.size(); j++) {
				y += entryHeight + 1;

				context.fill(x - 1, y, x + maxPerColumn.get(i), y + entryHeight, this.client.options.getTextBackgroundColor(553648127));
				RenderSystem.enableBlend();

				Text playerName = col.get(j).getDisplayName() != null ? col.get(j).getDisplayName() : Text.of(col.get(j).getProfile().getName());

				PlayerSkinDrawer.draw(context, col.get(j).getSkinTextures().texture(), x, y + 1, 8, true, false);
				context.drawTextWithShadow(this.client.textRenderer, playerName, x + 2 + 8, y + 2, col.get(j).getGameMode() == GameMode.SPECTATOR ? -1862270977 : -1);
			}

			x += maxPerColumn.get(i) + 2;
		}

		int y = startY + 5 + totalRowHeight;
		for (OrderedText line : footerList) {
			y += 9;
			context.drawTextWithShadow(this.client.textRenderer, line, windowWidth / 2 - client.textRenderer.getWidth(line) / 2, y, -1);
		}
		ci.cancel();
	}

	@Inject(method = "collectPlayerEntries", at = @At("RETURN"), cancellable = true)
	private void onCollectPlayerEntries(CallbackInfoReturnable<List<PlayerListEntry>> cir) {
		List<PlayerListEntry> playerList = new ArrayList<>(cir.getReturnValue());

		for (int i = 1; i <= 200; i++) {
			String fakePlayerName = "EXAMPLEPLAYER" + i;
			UUID fakeUUID = UUID.nameUUIDFromBytes(fakePlayerName.getBytes());

			GameProfile fakeProfile = new GameProfile(fakeUUID, fakePlayerName);
			PlayerListEntry fakeEntry = new PlayerListEntry(fakeProfile, false);

			playerList.add(fakeEntry);
		}

//		cir.setReturnValue(playerList.stream().sorted(ENTRY_ORDERING).limit(80L).toList());
		cir.setReturnValue(playerList);
	}


}