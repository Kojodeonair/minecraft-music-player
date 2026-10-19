package dev.kojode.musicplayer;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

/** Optional compact radio-style playback display. */
public final class DynamicIslandHud {
    private DynamicIslandHud() {}

    public static void register() {
        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("musicplayer", "dynamic_island"), DynamicIslandHud::render);
    }

    private static void render(GuiGraphicsExtractor graphics, net.minecraft.client.DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!PlayerConfig.dynamicIsland || minecraft.player == null || minecraft.gui.screen() != null || !MusicController.shouldShowIsland()) return;

        String status = MusicController.displayStatus();
        String title = MusicController.displayTitle();
        float scale = PlayerConfig.islandScale;
        int width = 238;
        int left = (minecraft.getWindow().getGuiScaledWidth() - (int) (width * scale)) / 2;
        int top = 8;
        while (!title.isEmpty() && minecraft.font.width(title) > 142) title = title.substring(0, title.length() - 1);
        if (!title.equals(MusicController.displayTitle()) && !title.isEmpty()) title += "…";

        graphics.pose().pushMatrix();
        graphics.pose().translate(left, top);
        graphics.pose().scale(scale, scale);
        // Antenna and dark walnut radio case.
        graphics.fill(22, 0, 56, 3, 0xFFB6A17B);
        graphics.fill(40, -3, 43, 1, 0xFFB6A17B);
        graphics.fill(-2, 7, width + 2, 43, 0xFF100E0B);
        graphics.fill(0, 5, width, 41, 0xFF3A2C1E);
        graphics.fill(2, 7, width - 2, 39, 0xFF76502C);
        graphics.fill(5, 10, 46, 36, 0xFF241B14);
        // Speaker grille.
        for (int i = 0; i < 5; i++) graphics.fill(10 + i * 7, 13, 13 + i * 7, 33, 0xFF5C4935);
        // Illuminated station display.
        graphics.fill(51, 10, 205, 36, 0xFF171F1A);
        graphics.fill(53, 12, 203, 34, 0xFF26372A);
        graphics.text(minecraft.font, "FM  MUSIC PLAYER", 58, 13, 0xFFB5D58B, false);
        String radioTitle = title.isEmpty() ? "No station" : title;
        graphics.text(minecraft.font, radioTitle, 58, 24, 0xFFFFE8A6, false);
        // Tune and volume knobs, plus a status lamp.
        graphics.fill(211, 12, 229, 30, 0xFF241A12);
        graphics.fill(214, 15, 226, 27, 0xFFD5B879);
        graphics.fill(216, 17, 224, 25, 0xFF62472A);
        graphics.fill(233, 13, 235, 15, "Paused".equals(status) ? 0xFFFFC857 : 0xFF79E28D);
        // Small signal meter animates while playing.
        for (int i = 0; i < 4; i++) {
            int height = "Paused".equals(status) ? 3 : 3 + (int) ((System.currentTimeMillis() / 140L + i * 2L) % 9L);
            graphics.fill(210 + i * 5, 37 - height, 213 + i * 5, 37, 0xFFE9C46A);
        }
        graphics.text(minecraft.font, status.toUpperCase(java.util.Locale.ROOT), 9, 36, 0xFFFFD989, false);
        graphics.pose().popMatrix();
    }
}
