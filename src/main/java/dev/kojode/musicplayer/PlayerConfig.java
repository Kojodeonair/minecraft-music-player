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
    private PlayerConfig() {}
    public static void load() { try { if (Files.exists(FILE)) { Data d = GSON.fromJson(Files.readString(FILE), Data.class); if (d != null) { volume = Math.max(0, Math.min(1, d.volume)); loop = d.loop; playlistLoop = d.playlistLoop; } } } catch (Exception ignored) {} }
    public static void save() { try { Files.createDirectories(FILE.getParent()); Files.writeString(FILE, GSON.toJson(new Data(volume, loop, playlistLoop))); } catch (Exception ignored) {} }
    private record Data(float volume, boolean loop, boolean playlistLoop) {}
}
