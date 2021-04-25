package ladysnake.illuminations.updater;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import ladysnake.illuminations.client.Config;
import ladysnake.illuminations.client.IlluminationsClient;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.api.VersionParsingException;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IlluminationsUpdater {
    private static final String UPDATES_URL = "https://illuminations.uuid.gg/latest?version=";
    private static final String UNINSTALLER = "illuminations-uninstaller.jar";
    static final ArrayList<String> UNINSTALLER_PARAMS = new ArrayList<>();
    public static boolean NEW_UPDATE = false;

    public static void init() {
        // delete uninstaller
        if (Files.exists(Paths.get("mods/" + UNINSTALLER))) {
            try {
                Files.delete(Paths.get("mods/" + UNINSTALLER));
            } catch (IOException e) {
                IlluminationsClient.logger.log(Level.WARN, "Could not remove uninstaller because of I/O Error: " + e.getMessage());
            }
        }

        if (Config.isAutoUpdate()) {
            // .future file
            Pattern pattern = Pattern.compile("^illuminations.+\\.future$");
            for (File mod : new File("mods").listFiles()) {
                Matcher matcher = pattern.matcher(mod.getName());
                if (matcher.find()) {
                    mod.delete();
                }
            }

            if (!FabricLoader.getInstance().isDevelopmentEnvironment()) {
                IlluminationsClient.logger.info("Looking for updates for Illuminations");

                String minecraftVersion = SharedConstants.getGameVersion().getName();
                String modVersion = FabricLoader.getInstance().getModContainer(IlluminationsClient.MODID).get().getMetadata().getVersion().getFriendlyString();
                CompletableFuture.supplyAsync(() -> {
                    try (Reader reader = new InputStreamReader(new URL(UPDATES_URL + minecraftVersion).openStream())) {
                        JsonParser jp = new JsonParser();
                        JsonElement jsonElement = jp.parse(reader);
                        return jsonElement.getAsJsonObject();
                    } catch (MalformedURLException e) {
                        IlluminationsClient.logger.log(Level.ERROR, "Could not get update information because of malformed URL: " + e.getMessage());
                    } catch (IOException e) {
                        IlluminationsClient.logger.log(Level.ERROR, "Could not get update information because of I/O Error: " + e.getMessage());
                    }

                    return null;
                }).thenAcceptAsync(latestVersionJson -> {
                    if (latestVersionJson != null) {
                        String latestVersion = latestVersionJson.get("version").getAsString();
                        String latestFileName = latestVersionJson.get("filename").getAsString() + ".future";
                        // if not the latest version, update toast
                        try {
                            if (SemanticVersion.parse(latestVersion).compareTo(SemanticVersion.parse(modVersion)) > 0) {
                                IlluminationsClient.logger.log(Level.INFO, "Currently present version of Illuminations is " + modVersion + " while the latest version is " + latestVersion + "; downloading update");

                                try {
                                    // download new jar
                                    URL website = new URL(latestVersionJson.get("download").getAsString());
                                    ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                                    FileOutputStream fos = new FileOutputStream("mods/" + latestFileName);
                                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                                    IlluminationsClient.logger.log(Level.INFO, latestFileName + " downloaded");

                                    ModContainer mod = FabricLoader.getInstance().getModContainer(IlluminationsClient.MODID).get();
                                    URL rootUrl = mod.getRootPath().toUri().toURL();
                                    URLConnection connection = rootUrl.openConnection();
                                    if (connection instanceof JarURLConnection) {
                                        URI uri = ((JarURLConnection) connection).getJarFileURL().toURI();
                                        if (uri.getScheme().equals("file")) {
                                            // add the old jar to uninstaller params
                                            String oldFilePath = Paths.get(uri).toString();
                                            String oldFile = Paths.get(oldFilePath).getFileName().toString();
                                            UNINSTALLER_PARAMS.add(oldFile);

                                            // add the new jar to uninstaller params
                                            UNINSTALLER_PARAMS.add(latestFileName);

                                            NEW_UPDATE = true;
                                        }
                                    }
                                } catch (MalformedURLException e) {
                                    IlluminationsClient.logger.log(Level.ERROR, "Could not download update because of malformed URL: " + e.getMessage());
                                } catch (IOException e) {
                                    IlluminationsClient.logger.log(Level.ERROR, "Could not download update because of I/O Error: " + e.getMessage());
                                } catch (URISyntaxException e) {
                                    IlluminationsClient.logger.log(Level.ERROR, "Could not download update because of URI Syntax Error: " + e.getMessage());
                                }
                            }
                        } catch (VersionParsingException e) {
                            e.printStackTrace();
                        }
                    } else {
                        IlluminationsClient.logger.log(Level.WARN, "Update information could not be retrieved, auto-update will not be available");
                    }
                }, MinecraftClient.getInstance());
            }

            IlluminationsClient.logger.log(Level.INFO, "Adding shutdown hook for uninstaller to update Illuminations");

            // extract the uninstaller and add a shutdown hook to uninstall old files and install new ones
            InputStream in = IlluminationsClient.class.getResourceAsStream("/" + UNINSTALLER);
            try {
                Files.copy(in, Paths.get("mods/" + UNINSTALLER), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    IlluminationsClient.logger.log(Level.INFO, "Minecraft instance shutting down, starting the Illuminations uninstaller");

                    StringBuilder commandParams = new StringBuilder();
                    for (String uninstallerParam : UNINSTALLER_PARAMS) {
                        commandParams.append(" ").append(uninstallerParam);
                    }

                    Runtime.getRuntime().exec("java -jar mods/" + UNINSTALLER + commandParams);
                } catch (IOException e) {
                    IlluminationsClient.logger.log(Level.ERROR, "Could not run uninstaller");
                    e.printStackTrace();
                }
            }));
        }
    }

}
