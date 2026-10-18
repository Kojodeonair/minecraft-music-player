package dev.codex.musicplayer;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public final class MusicPlayerClient implements ClientModInitializer {
    @Override public void onInitializeClient() {
        PlayerConfig.load();
        KeyMapping.Category category = KeyMapping.Category.register(ResourceLocation.fromNamespaceAndPath("musicplayer", "controls"));
        KeyMapping key = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.musicplayer.open", InputConstants.Type.KEYSYM, InputConstants.KEY_P, category));
        ClientTickEvents.END_CLIENT_TICK.register(client -> { while (key.consumeClick()) client.setScreen(new MusicScreen()); MusicController.tick(client); });
    }
}
