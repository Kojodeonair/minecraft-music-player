package dev.kojode.musicplayer;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import java.util.Comparator;
import java.util.List;

public record Track(Identifier id, SoundEvent event) {
    public String title() { return id.getPath().replace('.', ' ').replace('_', ' '); }
    public boolean isBackgroundMusic() {
        String path = id.getPath();
        return (path.startsWith("music.") || path.startsWith("music_")) && !path.contains("music_disc");
    }
    public static List<Track> discover() {
        return BuiltInRegistries.SOUND_EVENT.entrySet().stream().filter(e -> {
            String p = BuiltInRegistries.SOUND_EVENT.getKey(e.getValue()).getPath(); return p.startsWith("music.") || p.startsWith("music_") || p.contains("music_disc");
        }).map(e -> new Track(BuiltInRegistries.SOUND_EVENT.getKey(e.getValue()), e.getValue())).sorted(Comparator.comparing(Track::title, String.CASE_INSENSITIVE_ORDER)).toList();
    }
}
