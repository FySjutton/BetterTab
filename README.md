<center>

![BetterTab logo](https://cdn.modrinth.com/data/cached_images/df2a8480033b27d741794e79a200d64611ce0e47.png)

---

[<img alt="discord-plural" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/social/discord-plural_64h.png">](https://discord.gg/tqn38v6w7k)
[<img alt="fabric-api" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/requires/fabric-api_64h.png">](https://modrinth.com/mod/fabric-api)
[<img alt="yacl" height="56" src="https://i.ibb.co/HTLrwVft/cozy-64h-1.png" alt="cozy-64h-1">](https://modrinth.com/mod/yacl)
[<img alt="modmenu" height="56" src="https://i.postimg.cc/MTv30Q1c/cozy-64h.png">](https://modrinth.com/mod/modmenu)

</center>

Better Tab is a mod that improves the player list, making it scrollable and adding a numerical ping display. The default minecraft player list only shows up to 80 players, and on smaller screens they will overlap making it difficult to read. Better Tab makes the player list scrollable, making sure no entries overlap and infinite players can be seen.

---

## Showcase (Without & with the mod)
Comparison between when using the mod, and when not. Without the mod comes first.
<iframe width="560" height="315" src="https://www.youtube-nocookie.com/embed/_1Sxm7B5l-Q" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" allowfullscreen></iframe>

---

## For developers

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

<center>

## Check out my other mods

[<img alt="adaptivehud" height="56" src="https://i.ibb.co/wrB80v6Q/compact-46h.png">](https://modrinth.com/mod/adaptivehud)
[<img alt="adaptivehud" height="56" src="https://i.ibb.co/JRvKxsZP/compact-46h-1.png">](https://modrinth.com/mod/anti-item-break)
</center>