package tab.bettertab;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static tab.bettertab.BetterTab.LOGGER;

public class ConfigSystem {
    public static JsonElement configFile;
    public static JsonObject defaultConfig;

    public void checkConfig() {
        Path configDir = FabricLoader.getInstance().getConfigDir();

        if (Files.notExists(configDir.resolve("betterTab.json"))) {
            LOGGER.warn("BetterTab: Configuration file not found - generating new config file.");
            try {
                InputStream resource = ConfigSystem.class.getResourceAsStream("/assets/better-tab/default_config/betterTab.json");
                FileUtils.copyInputStreamToFile(resource, new File(configDir + "/betterTab.json"));
            } catch (Exception e) {
                LOGGER.error("BetterTab - Could not generate a new betterTab.json file (config), the program will now close. This error should not normally occur, and if you need help, please join our discord server. This error indicates that there's something wrong with the jar file, or the program doesn't have access to write files.");
                LOGGER.error("Shutting down minecraft..."); // Should just inactivate mod instead?
                e.printStackTrace();
                MinecraftClient.getInstance().stop();
            }
        }
        generateConfigArray();
    }

    private void generateConfigArray() {
        InputStream resource = ConfigSystem.class.getResourceAsStream("/assets/better-tab/default_config/betterTab.json");
        Reader reader = new InputStreamReader(resource);
        defaultConfig = JsonParser.parseReader(reader).getAsJsonObject();
        try {
            reader.close();
        } catch (IOException e) {
            LOGGER.error("BetterTab: Failed to close reader?");
            e.printStackTrace();
        }
        Path configDir = FabricLoader.getInstance().getConfigDir();

        try {
            File config = new File(configDir + "/betterTab.json");
            FileReader fileReader = new FileReader(config);
            JsonElement elm = JsonParser.parseReader(fileReader);
            fileReader.close();

            try {
                // VALIDATE THE "elm"
                JsonObject obj = elm.getAsJsonObject();
                obj.get("enable_mod").getAsBoolean();
                obj.get("render_heads").getAsBoolean();
                obj.get("use_numeric").getAsBoolean();
                obj.get("numeric_format").getAsBoolean();
                obj.get("render_ping").getAsBoolean();
                obj.get("render_header").getAsBoolean();
                obj.get("render_footer").getAsBoolean();
                obj.get("scroll_indicators").getAsBoolean();
                obj.get("background_color").getAsString();
                obj.get("cell_color").getAsString();
                obj.get("name_color").getAsString();
                obj.get("spectator_color").getAsString();
                obj.get("ping_color_none").getAsString();
                obj.get("ping_color_low").getAsString();
                obj.get("ping_color_medium").getAsString();
                obj.get("ping_color_high").getAsString();
                obj.get("scroll_with_mouse").getAsBoolean();
                obj.get("empty_cell_line_color").getAsString();
                obj.get("column_number_color").getAsString();
                obj.get("scroll_indicator_color").getAsString();
                if (!new ArrayList<>(List.of(0, 1, 2)).contains(obj.get("column_numbers").getAsInt())) {
                    // 0: Disabled, 1: Render on scroll, 2: Render Always (1 default)
                    throw new RuntimeException("Invalid column number");
                }
                if (!new ArrayList<>(List.of(0, 1)).contains(obj.get("scroll_type").getAsInt())) {
                    // 0: Column, 1: Page (0 default)
                    throw new RuntimeException("Invalid column number");
                }
                if (!(obj.get("max_row_height").getAsDouble() > 0 && obj.get("max_row_height").getAsDouble() < 2)) {
                    throw new RuntimeException("Invalid max height, 0 < max_row_height < 2");
                }
                if (!(obj.get("max_width").getAsDouble() > 0 && obj.get("max_width").getAsDouble() < 2)) {
                    throw new RuntimeException("Invalid max width, 0 < max_width < 2");
                }
                obj.get("save_scroll").getAsBoolean();
                obj.get("use_examples").getAsBoolean();
                obj.get("example_text").getAsString();
                if (!(obj.get("example_amount").getAsInt() > 0 && obj.get("example_amount").getAsInt() <= 500)) {
                    throw new RuntimeException("Invalid example amount, 0 < example_amount <= 500");
                }
                if (!(obj.get("start_y").getAsInt() >= 0 && obj.get("start_y").getAsInt() <= 200)) {
                    throw new RuntimeException("Invalid example amount, 0 <= start_y <= 200");
                }
                if (!(obj.get("scroll_indicator_flash_speed").getAsInt() >= 1 && obj.get("scroll_indicator_flash_speed").getAsInt() <= 4000)) {
                    throw new RuntimeException("Invalid example amount, 1 <= scroll_indicator_flash_speed <= 4000");
                }
                if (!(obj.get("medium_ping_minimum").getAsInt() >= 5 && obj.get("medium_ping_minimum").getAsInt() <= 2000)) {
                    throw new RuntimeException("Invalid example amount, 5 <= medium_ping_minimum <= 2000");
                }
                if (!(obj.get("high_ping_minimum").getAsInt() >= 10 && obj.get("high_ping_minimum").getAsInt() <= 4000)) {
                    throw new RuntimeException("Invalid example amount, 10 <= high_ping_minimum <= 4000");
                }
            } catch (Exception e) {
                LOGGER.error("BetterTab: The configuration file does not appear to follow the required format. This might be caused by a missing key or similar. For help, join our discord server. You can try to delete the configuration file and than restart your game.");
                LOGGER.error("The error above is critical, and the game will automatically close now.");
                e.printStackTrace();
                MinecraftClient.getInstance().stop();
            }

            configFile = elm;
        } catch (Exception e) {
            LOGGER.error("BetterTab - Could not load configuration file, this is most likely because of the file not following proper json syntax, like a missing comma or similar. For help, please seek help in Fy's Development discord server.");
            LOGGER.error(e.getMessage());
            MinecraftClient.getInstance().stop();
        }
    }

    public boolean saveElementFiles(JsonElement newConfigFile) {
        Path configDir = FabricLoader.getInstance().getConfigDir();
        try {
            File elmFile = new File(configDir + "/betterTab.json");
            if (!elmFile.exists()) {
                Files.createDirectories(elmFile.getParentFile().toPath());
            }
            FileWriter writer = new FileWriter(elmFile);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonWriter jsonWriter = gson.newJsonWriter(writer);
            gson.toJson(newConfigFile, jsonWriter);
            jsonWriter.flush();
            jsonWriter.close();
            return true;
        } catch (Exception e) {
            LOGGER.error("Error saving config file! " + e.getMessage());
            return false;
        }
    }
}