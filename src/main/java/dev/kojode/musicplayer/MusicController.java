package dev.kojode.musicplayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import java.util.List;
import dev.kojode.musicplayer.mixin.SoundEngineAccessor;
import dev.kojode.musicplayer.mixin.SoundManagerAccessor;

public final class MusicController {
    private static Track current;
    private static Track lastTrack;
    private static PlayerSound sound;
    private static boolean paused;
    private static boolean channelVolumeDirty;
    private static boolean channelPauseDirty;
    private static String displayStatus = "";
    private static String displayTitle = "";
    private static long statusUntil;
    private MusicController() {}
    public static Track current() { return current; }
    public static boolean isPlayingManually() { return current != null; }
    public static String displayStatus() { return displayStatus; }
    public static String displayTitle() { return displayTitle; }
    public static boolean shouldShowIsland() { return current != null || System.currentTimeMillis() < statusUntil; }
    public static void play(Minecraft mc, Track track) { stop(mc); mc.getMusicManager().stopPlaying(); current = track; lastTrack = track; displayTitle = track.title(); displayStatus = "Playing"; statusUntil = 0; paused = false; channelVolumeDirty = true; channelPauseDirty = false; sound = new PlayerSound(track, PlayerConfig.volume, PlayerConfig.loop); mc.getSoundManager().play(sound); }
    public static void stop(Minecraft mc) { boolean hadTrack = current != null; if (sound != null) mc.getSoundManager().stop(sound); sound = null; current = null; paused = false; channelVolumeDirty = false; channelPauseDirty = false; if (hadTrack) { displayStatus = "Stopped"; statusUntil = System.currentTimeMillis() + 2500; } }
    public static void pauseResume(Minecraft mc) {
        if (current == null || sound == null) return;
        paused = !paused;
        channelPauseDirty = true;
        syncChannel(mc);
        displayStatus = paused ? "Paused" : "Playing";
        statusUntil = 0;
    }
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
    public static void adjustVolume(Minecraft mc, float amount) {
        PlayerConfig.volume = Math.max(0, Math.min(1, PlayerConfig.volume + amount));
        if (sound != null) {
            sound.setPlaybackVolume(PlayerConfig.volume);
            channelVolumeDirty = true;
            syncChannel(mc);
        }
        PlayerConfig.save();
    }
    public static void next(Minecraft mc, int step) { List<Track> all = Track.discover(); if (all.isEmpty()) return; int i = current == null ? -1 : all.indexOf(current); play(mc, all.get(Math.floorMod(i + step, all.size()))); }
    public static void tick(Minecraft mc) {
        syncChannel(mc);
        if (sound == null || mc.getSoundManager().isActive(sound)) return;
        sound = null;
        if (paused) return;
        if (PlayerConfig.loop && current != null) play(mc, current);
        else if (PlayerConfig.playlistLoop && current != null) next(mc, 1);
        else { current = null; displayStatus = "Finished"; statusUntil = System.currentTimeMillis() + 2500; }
    }
    private static void syncChannel(Minecraft mc) {
        if (sound == null || (!channelVolumeDirty && !channelPauseDirty) || !mc.getSoundManager().isActive(sound)) return;
        SoundEngine engine = ((SoundManagerAccessor) mc.getSoundManager()).musicplayer$getSoundEngine();
        SoundEngineAccessor access = (SoundEngineAccessor) (Object) engine;
        ChannelAccess.ChannelHandle handle = access.musicplayer$getInstanceToChannel().get((SoundInstance) sound);
        if (handle == null) return;
        boolean applyVolume = channelVolumeDirty;
        boolean applyPause = channelPauseDirty;
        boolean pauseNow = paused;
        float gain = access.musicplayer$calculateVolume(sound);
        handle.execute(channel -> {
            if (applyVolume) channel.setVolume(gain);
            if (applyPause) {
                if (pauseNow) channel.pause();
                else channel.unpause();
            }
        });
        if (applyVolume) channelVolumeDirty = false;
        if (applyPause) channelPauseDirty = false;
    }
    private static final class PlayerSound extends AbstractSoundInstance {
        PlayerSound(Track t, float volume, boolean looping) {
            // Each alias in assets/musicplayer/sounds.json points at exactly one vanilla OGG.
            super(t.id(), SoundSource.MUSIC, RandomSource.create());
            this.looping = looping; this.relative = true; this.attenuation = Attenuation.NONE;
            setPlaybackVolume(volume);
        }
        void setPlaybackVolume(float volume) { this.volume = Math.max(0.0F, Math.min(1.0F, volume)); }
    }
}
