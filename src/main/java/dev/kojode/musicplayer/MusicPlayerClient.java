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
        KeyMapping.Category category = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("musicplayer", "controls"));
        KeyMapping key = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.musicplayer.open", InputConstants.Type.KEYBOARD, InputConstants.KEY_P, category));
        ClientTickEvents.END_CLIENT_TICK.register(client -> { while (key.consumeClick()) client.gui.setScreen(new MusicScreen()); MusicController.tick(client); });
    }
}
