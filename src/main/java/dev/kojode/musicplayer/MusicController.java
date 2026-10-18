package dev.kojode.musicplayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import java.util.List;

public final class MusicController {
    private static Track current;
    private static Track lastTrack;
    private static PlayerSound sound;
    private static boolean paused;
    private static String displayStatus = "";
    private static String displayTitle = "";
    private static long statusUntil;
    private MusicController() {}
    public static Track current() { return current; }
    public static boolean isPlayingManually() { return current != null; }
    public static String displayStatus() { return displayStatus; }
    public static String displayTitle() { return displayTitle; }
    public static boolean shouldShowIsland() { return current != null || System.currentTimeMillis() < statusUntil; }
    public static void play(Minecraft mc, Track track) { stop(mc); mc.getMusicManager().stopPlaying(); current = track; lastTrack = track; displayTitle = track.title(); displayStatus = "Playing"; statusUntil = 0; paused = false; sound = new PlayerSound(track, PlayerConfig.volume, PlayerConfig.loop); mc.getSoundManager().play(sound); }
    public static void stop(Minecraft mc) { boolean hadTrack = current != null; if (sound != null) mc.getSoundManager().stop(sound); sound = null; current = null; paused = false; if (hadTrack) { displayStatus = "Stopped"; statusUntil = System.currentTimeMillis() + 2500; } }
    public static void pauseResume(Minecraft mc) { if (current == null) return; if (!paused) { if (sound != null) mc.getSoundManager().stop(sound); sound = null; paused = true; displayStatus = "Paused"; statusUntil = 0; } else play(mc, current); }
    public static void togglePlayPause(Minecraft mc) {
        if (current != null) { pauseResume(mc); return; }
        Track track = lastTrack;
        if (track == null) {
            List<Track> available = Track.discover();
            if (available.isEmpty()) return;
            track = available.getFirst();
        }
        play(mc, track);
    }
    public static void adjustVolume(float amount) {
        PlayerConfig.volume = Math.max(0, Math.min(1, PlayerConfig.volume + amount));
        if (sound != null) sound.setPlaybackVolume(PlayerConfig.volume);
        PlayerConfig.save();
    }
    public static void next(Minecraft mc, int step) { List<Track> all = Track.discover(); if (all.isEmpty()) return; int i = current == null ? -1 : all.indexOf(current); play(mc, all.get(Math.floorMod(i + step, all.size()))); }
    public static void tick(Minecraft mc) {
        if (sound == null || mc.getSoundManager().isActive(sound)) return;
        sound = null;
        if (paused) return;
        if (PlayerConfig.loop && current != null) play(mc, current);
        else if (PlayerConfig.playlistLoop && current != null) next(mc, 1);
        else { current = null; displayStatus = "Finished"; statusUntil = System.currentTimeMillis() + 2500; }
    }
    private static final class PlayerSound extends AbstractSoundInstance {
        PlayerSound(Track t, float volume, boolean looping) {
            // Each alias in assets/musicplayer/sounds.json points at exactly one vanilla OGG.
            super(t.id(), SoundSource.MUSIC, RandomSource.create());
            this.volume = volume; this.looping = looping; this.relative = true; this.attenuation = Attenuation.NONE;
        }
        void setPlaybackVolume(float volume) { this.volume = volume; }
    }
}
