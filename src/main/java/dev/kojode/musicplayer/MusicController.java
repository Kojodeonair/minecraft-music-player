package dev.kojode.musicplayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import java.util.List;

public final class MusicController {
    private static Track current;
    private static PlayerSound sound;
    private static boolean paused;
    private MusicController() {}
    public static Track current() { return current; }
    public static boolean isPlayingManually() { return sound != null; }
    public static void play(Minecraft mc, Track track) { stop(mc); mc.getMusicManager().stopPlaying(); current = track; paused = false; sound = new PlayerSound(track, PlayerConfig.volume, PlayerConfig.loop); mc.getSoundManager().play(sound); }
    public static void stop(Minecraft mc) { if (sound != null) mc.getSoundManager().stop(sound); sound = null; current = null; paused = false; }
    public static void pauseResume(Minecraft mc) { if (current == null) return; if (!paused) { if (sound != null) mc.getSoundManager().stop(sound); sound = null; paused = true; } else play(mc, current); }
    public static void next(Minecraft mc, int step) { List<Track> all = Track.discover(); if (all.isEmpty()) return; int i = current == null ? -1 : all.indexOf(current); play(mc, all.get(Math.floorMod(i + step, all.size()))); }
    public static void tick(Minecraft mc) {
        if (sound == null || mc.getSoundManager().isActive(sound)) return;
        sound = null;
        if (paused) return;
        if (PlayerConfig.loop && current != null) play(mc, current);
        else if (PlayerConfig.playlistLoop && current != null) next(mc, 1);
        else current = null;
    }
    private static final class PlayerSound extends AbstractSoundInstance {
        PlayerSound(Track t, float volume, boolean looping) {
            // Each alias in assets/musicplayer/sounds.json points at exactly one vanilla OGG.
            super(t.id(), SoundSource.MUSIC, RandomSource.create());
            this.volume = volume; this.looping = looping; this.relative = true; this.attenuation = Attenuation.NONE;
        }
    }
}
