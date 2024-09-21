package tab.bettertab;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static tab.bettertab.BetterTab.LOGGER;

public class ConfigSystem {
    public static JsonElement configFile;

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