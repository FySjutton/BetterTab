mod info

<details>
<summary>Developer Documentation</summary>
BetterTab also includes a "library" that can help you render icons next to a player on the player list.

This could for example be useful if you want to have a synced logo for everyone using your mod or modpack. BetterTab can then help you with the rendering part.

To insert a badge, simply use a mixin on the `tab.bettertab.PlayerBages` class, and edit the "List<Identifier> getBadges(PlayerListEntry player)" method to add an Identifier to the badge list if the player is supposed to have it, and BetterTab will then take care of the rest! 
</details>