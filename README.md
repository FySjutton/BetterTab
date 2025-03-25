<details>
<summary>Developer Documentation</summary>
BetterTab also includes a "library" that can help you render icons next to a player on the player list.

This could for example be useful if you want to have a synced logo for everyone using your mod or modpack. BetterTab can then help you with the rendering part.

To insert a badge, simply register a badge provider, and insert your icons into the badge array. This could look like this:
```java
import static tab.bettertab.tabList.BadgeManager.registerBadgeProvider;

public class MyMod implements ModInitializer {
    @Override
    public void onInitialize() {
        // You can easily add your own badges by registering a provider like the following example.
        registerBadgeProvider((player, badgeList) -> {
            if (player.getProfile().getName().equals("Fy17")) {
                badgeList.add(Identifier.of("myMod", "textures/gui/example.png"));
            }
        });
    }
}
```
</details>