package dev.kojode.musicplayer.mixin;

import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Provides access to the client sound engine for the selected mod sound only. */
@Mixin(SoundManager.class)
public interface SoundManagerAccessor {
    @Accessor("soundEngine")
    SoundEngine musicplayer$getSoundEngine();
}
