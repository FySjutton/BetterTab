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
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

import static tab.bettertab.BetterTab.*;

@Mixin(PlayerListHud.class)
public abstract class PlayerListHudMixin {

	@Shadow
	@Final
	private static Comparator<PlayerListEntry> ENTRY_ORDERING;
	@Shadow
	private MinecraftClient client;

	@Shadow
	protected abstract List<PlayerListEntry> collectPlayerEntries();

	@Shadow
	@Nullable
	private Text header;
	@Shadow
	@Nullable
	private Text footer;

	@Shadow private boolean visible;

	@Shadow public abstract Text getPlayerName(PlayerListEntry entry);

	@Unique
	private boolean showArrows = false;
	@Unique
	private long lastCheck = 0;

	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	private void onRender(DrawContext context, int scaledWindowWidth, Scoreboard scoreboard, @Nullable ScoreboardObjective objective, CallbackInfo ci) {
		List<PlayerListEntry> list = this.collectPlayerEntries();

		if (Calendar.getInstance().getTimeInMillis() - lastCheck > 530) {
			lastCheck = Calendar.getInstance().getTimeInMillis();
			showArrows = !showArrows;
		}

		int entryHeight = 10;

		boolean canScrollLeft;
		boolean canScrollRight = false;
		boolean pRenderColumnNumbers = renderColumnNumbers;

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
				maxPerColumn.add(Collections.max(widths));
				widths = new ArrayList<>();
				columns.add(column);
				column = new ArrayList<>();
				difference = i;
			}

			Text playerName = this.getPlayerName(list.get(i));
			widths.add(client.textRenderer.getWidth(playerName) + 10 + 8 + 2);
			column.add(list.get(i));
		}
		if (!columns.isEmpty()) {

			int emptyLinesNeeded = columns.getFirst().size() - column.size();
			for (int k = 0; k < emptyLinesNeeded; k++) {
				String fakePlayerName = "";
				UUID fakeUUID = UUID.nameUUIDFromBytes(fakePlayerName.getBytes());

				GameProfile fakeProfile = new GameProfile(fakeUUID, fakePlayerName);
				PlayerListEntry fakeEntry = new PlayerListEntry(fakeProfile, false);
				column.add(fakeEntry);

			}
		}
		columns.add(column);
		maxPerColumn.add(Collections.max(widths));

		List<Integer> useMaxes;
		List<ArrayList<PlayerListEntry>> useColumns;

		int scroll = tabScroll >= 0 ? (int) Math.floor(tabScroll) : 0;
		scroll = Math.min(scroll, maxPerColumn.size()); // Makes sure the scroll doesn't get above the max amount of columns
		tabScroll = scroll;

		canScrollLeft = scroll > 0;

		List<Integer> testScrollList = maxPerColumn.subList(scroll, maxPerColumn.size());
		int a = 0;
		int extraCols = 0;
		boolean found = false;
		for (int b : testScrollList) {
			if (a + b + 20 < windowWidth) { // 20 for extra padding
				a += b + 2;
				extraCols++;
			} else {
				found = true;
				break;
			}
		}
		if (found) {
			useMaxes = maxPerColumn.subList(scroll, scroll + extraCols);
			useColumns = columns.subList(scroll, scroll + extraCols);
			canScrollRight = true;
		} else {
			int d = 0;
			int index = maxPerColumn.size();
			for (Integer c : maxPerColumn.reversed()) {
				if (d + c + 20 < windowWidth) { // 10 for extra padding/
					d += c + 2;
					index--;
				} else {
					break;
				}
			}
			useMaxes = maxPerColumn.subList(index, maxPerColumn.size());
			useColumns = columns.subList(index, maxPerColumn.size());
			tabScroll = maxPerColumn.size() - useMaxes.size();
		}

		pRenderColumnNumbers = (canScrollRight || canScrollLeft) && pRenderColumnNumbers;
		int totalColWidth = useMaxes.stream().mapToInt(b -> b + 2).sum();
		int totalRowHeight = useColumns.getFirst().size() * (entryHeight + 1);

		List<OrderedText> headerList = List.of();
		if (this.header != null) {
			headerList = client.textRenderer.wrapLines(this.header, totalColWidth);
		}

		List<OrderedText> footerList = List.of();
		if (this.footer != null) {
			footerList = client.textRenderer.wrapLines(this.footer, totalColWidth);
		}

		int x = (windowWidth - totalColWidth) / 2;

		int charWidth = client.textRenderer.getWidth("<");
		context.fill(x - 5 - (canScrollLeft ? 5 + charWidth : 0), startY - 5, x + totalColWidth + 5 + (canScrollRight ? 5 + charWidth : 0), startY + headerList.size() * 9 + footerList.size() * 9 + totalRowHeight + 5 + (pRenderColumnNumbers ? 3 + 5 : 0), 0x80000000);
		if (showArrows) {
			if (canScrollLeft) {
				context.drawTextWrapped(client.textRenderer, StringVisitable.plain("<<<"), x - 5 - charWidth, startY + headerList.size() * 9 + totalRowHeight / 2 - 4 - 9, charWidth, 0xFFFFFFFF);
			}
			if (canScrollRight) {
				context.drawTextWrapped(client.textRenderer, StringVisitable.plain(">>>"), x + totalColWidth + 5, startY + headerList.size() * 9 + totalRowHeight / 2 - 4 - 9, charWidth, 0xFFFFFFFF);
			}
		}

		for (OrderedText line : headerList) {
			context.drawTextWithShadow(this.client.textRenderer, line, windowWidth / 2 - client.textRenderer.getWidth(line) / 2, startY, -1);
			startY += 9;
		}
		startY -= 9;

		for (int i = 0; i < useColumns.size(); i++) {
			ArrayList<PlayerListEntry> col = useColumns.get(i);

			int y = startY;
			for (int j = 0; j < col.size(); j++) {
				y += entryHeight + 1;

				context.fill(x - 1, y, x + useMaxes.get(i), y + entryHeight, 0x20FFFFFF);
				RenderSystem.enableBlend();

				Text playerName = this.getPlayerName(col.get(j));

				if (!playerName.getString().isEmpty()) {
					PlayerSkinDrawer.draw(context, col.get(j).getSkinTextures().texture(), x, y + 1, 8, true, false);
					context.drawTextWithShadow(this.client.textRenderer, playerName, x + 2 + 8, y + 2, col.get(j).getGameMode() == GameMode.SPECTATOR ? 0x90FFFFFF : -1);
				} else {
					context.fill(x + 2, y + 2 + 3, x + useMaxes.get(i) - 3, y + 2 + 4, 0x66FFFFFF);
				}
			}
			if (pRenderColumnNumbers) {
				context.drawCenteredTextWithShadow(this.client.textRenderer, String.valueOf(columns.indexOf(useColumns.get(i)) + 1), x + useMaxes.get(i) / 2 + 1, y + entryHeight + 3, 0x66FFFFFF);
			}

			x += useMaxes.get(i) + 2;
		}

		int y = startY + 5 + totalRowHeight + (pRenderColumnNumbers ? 3 + 5 : 0);
		for (OrderedText line : footerList) {
			y += 9;
			context.drawTextWithShadow(this.client.textRenderer, line, windowWidth / 2 - client.textRenderer.getWidth(line) / 2, y, -1);
		}
		ci.cancel();
	}

	@Inject(method = "collectPlayerEntries", at = @At("HEAD"), cancellable = true)
	private void onCollectPlayerEntries(CallbackInfoReturnable<List<PlayerListEntry>> cir) {
		List<PlayerListEntry> playerList = new ArrayList<>(client.player.networkHandler.getListedPlayerListEntries().stream().sorted(ENTRY_ORDERING).toList());

		if (useExamples) {
			for (int i = 1; i <= 200; i++) {
				String fakePlayerName = "ExamplePlayer" + i;
				UUID fakeUUID = UUID.nameUUIDFromBytes(fakePlayerName.getBytes());

				GameProfile fakeProfile = new GameProfile(fakeUUID, fakePlayerName);
				PlayerListEntry fakeEntry = new PlayerListEntry(fakeProfile, false);

				playerList.add(fakeEntry);
			}
		}

//		cir.setReturnValue(playerList.stream().sorted(ENTRY_ORDERING).limit(80L).toList());
		cir.setReturnValue(playerList);
	}

	@Inject(method = "setVisible", at = @At("HEAD"))
	private void onEnable(boolean visible, CallbackInfo ci) {
		if (this.visible != visible) {
			tabScroll = 0;
		}
	}
}