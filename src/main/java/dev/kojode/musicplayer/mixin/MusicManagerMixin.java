package dev.kojode.musicplayer.mixin;

import dev.kojode.musicplayer.MusicController;
import net.minecraft.client.sounds.MusicManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MusicManager.class)
public abstract class MusicManagerMixin {
    @Shadow public abstract void stopPlaying();

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void musicplayer$pauseVanillaMusicWhileTrackPlays(CallbackInfo callback) {
        if (MusicController.isPlayingManually()) {
            stopPlaying();
            callback.cancel();
        }
    }
}
