package dev.kojode.musicplayer;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

public final class MusicPlayerClient implements ClientModInitializer {
    @Override public void onInitializeClient() {
        PlayerConfig.load();
        DynamicIslandHud.register();
        KeyMapping.Category category = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("musicplayer", "controls"));
        KeyMapping open = register("key.musicplayer.open", InputConstants.KEY_P, category);
        KeyMapping playPause = register("key.musicplayer.play_pause", InputConstants.KEY_K, category);
        KeyMapping stop = register("key.musicplayer.stop", InputConstants.KEY_J, category);
        KeyMapping next = register("key.musicplayer.next", InputConstants.KEY_N, category);
        KeyMapping previous = register("key.musicplayer.previous", InputConstants.KEY_B, category);
        KeyMapping volumeUp = register("key.musicplayer.volume_up", InputConstants.KEY_U, category);
        KeyMapping volumeDown = register("key.musicplayer.volume_down", InputConstants.KEY_I, category);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (open.consumeClick()) client.gui.setScreen(new MusicScreen());
            if (client.gui.screen() == null) {
                while (playPause.consumeClick()) MusicController.togglePlayPause(client);
                while (stop.consumeClick()) MusicController.stop(client);
                while (next.consumeClick()) MusicController.next(client, 1);
                while (previous.consumeClick()) MusicController.next(client, -1);
                while (volumeUp.consumeClick()) MusicController.adjustVolume(client, .05f);
                while (volumeDown.consumeClick()) MusicController.adjustVolume(client, -.05f);
            }
            MusicController.tick(client);
        });
    }

    private static KeyMapping register(String translationKey, int key, KeyMapping.Category category) {
        return KeyMappingHelper.registerKeyMapping(new KeyMapping(translationKey, InputConstants.Type.KEYBOARD, key, category));
    }
}
