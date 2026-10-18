package dev.kojode.musicplayer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import java.nio.file.Files;
import java.nio.file.Path;

public final class PlayerConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("musicplayer.json");
    public static float volume = .65f;
    public static boolean loop, playlistLoop;
    public static boolean dynamicIsland = true;
    public static float islandScale = 1.0f;
    public static float uiScale = 1.0f;
    private PlayerConfig() {}
    public static void load() { try { if (Files.exists(FILE)) { Data d = GSON.fromJson(Files.readString(FILE), Data.class); if (d != null) { volume = Math.max(0, Math.min(1, d.volume)); loop = d.loop; playlistLoop = d.playlistLoop; if (d.dynamicIsland != null) dynamicIsland = d.dynamicIsland; if (d.islandScale != null) islandScale = clampIslandScale(d.islandScale); if (d.uiScale != null) uiScale = clampUiScale(d.uiScale); } } } catch (Exception ignored) {} }
    public static void save() { try { Files.createDirectories(FILE.getParent()); Files.writeString(FILE, GSON.toJson(new Data(volume, loop, playlistLoop, dynamicIsland, islandScale, uiScale))); } catch (Exception ignored) {} }
    public static float clampIslandScale(float scale) { return Math.max(0.25f, Math.min(1.8f, scale)); }
    public static float clampUiScale(float scale) { return Math.max(0.65f, Math.min(1.5f, scale)); }
    private record Data(float volume, boolean loop, boolean playlistLoop, Boolean dynamicIsland, Float islandScale, Float uiScale) {}
}
