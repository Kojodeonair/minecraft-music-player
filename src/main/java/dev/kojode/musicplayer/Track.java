package dev.codex.musicplayer;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import java.util.Comparator;
import java.util.List;

public record Track(ResourceLocation id, SoundEvent event) {
    public String title() { return id.getPath().replace('.', ' ').replace('_', ' '); }
    public static List<Track> discover() {
        return BuiltInRegistries.SOUND_EVENT.entrySet().stream().filter(e -> {
            String p = e.getKey().location().getPath(); return p.startsWith("music.") || p.startsWith("music_") || p.contains("music_disc");
        }).map(e -> new Track(e.getKey().location(), e.getValue())).sorted(Comparator.comparing(Track::title, String.CASE_INSENSITIVE_ORDER)).toList();
    }
}
