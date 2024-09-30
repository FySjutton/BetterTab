package tab.bettertab;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;

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

    public int parseColor(String colorString) {
        try {
            if (colorString.startsWith("#")) {
                colorString = colorString.substring(1);
            }

            int alpha = 255;
            if (colorString.length() == 8) {
                alpha = Integer.parseInt(colorString.substring(6, 8), 16);
                colorString = colorString.substring(0, 6);
            }

            int red = Integer.parseInt(colorString.substring(0, 2), 16);
            int green = Integer.parseInt(colorString.substring(2, 4), 16);
            int blue = Integer.parseInt(colorString.substring(4, 6), 16);

            return (alpha << 24) | (red << 16) | (green << 8) | blue;
        } catch (Exception e) {
            return 0;
        }
    }
}
