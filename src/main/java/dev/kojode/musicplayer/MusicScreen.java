package dev.codex.musicplayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import java.util.List;

public final class MusicScreen extends Screen {
    private final List<Track> tracks = Track.discover();
    private EditBox search;
    private int selected, scroll;
    public MusicScreen() { super(Component.literal("Music Player")); }
    @Override protected void init() {
        int x = width / 2 - 150, y = height / 2 - 110;
        search = new EditBox(font, x, y + 18, 300, 20, Component.literal("Search")); search.setHint(Component.literal("Search music...")); addRenderableWidget(search);
        addRenderableWidget(Button.builder(Component.literal("Play"), b -> { Track t = visible().stream().skip(selected).findFirst().orElse(null); if (t != null) MusicController.play(Minecraft.getInstance(), t); }).bounds(x, y + 144, 52, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Stop"), b -> MusicController.stop(Minecraft.getInstance())).bounds(x + 56, y + 144, 52, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Pause/Resume"), b -> MusicController.pauseResume(Minecraft.getInstance())).bounds(x + 112, y + 144, 100, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Prev"), b -> MusicController.next(Minecraft.getInstance(), -1)).bounds(x + 216, y + 144, 40, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Next"), b -> MusicController.next(Minecraft.getInstance(), 1)).bounds(x + 260, y + 144, 40, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Loop " + (PlayerConfig.loop ? "ON" : "OFF")), b -> { PlayerConfig.loop = !PlayerConfig.loop; PlayerConfig.playlistLoop = false; PlayerConfig.save(); clearAndInit(); }).bounds(x, y + 170, 90, 20).build());
        addRenderableWidget(Button.builder(Component.literal("List loop " + (PlayerConfig.playlistLoop ? "ON" : "OFF")), b -> { PlayerConfig.playlistLoop = !PlayerConfig.playlistLoop; PlayerConfig.loop = false; PlayerConfig.save(); clearAndInit(); }).bounds(x + 94, y + 170, 120, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Vol -"), b -> volume(-.1f)).bounds(x + 218, y + 170, 38, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Vol +"), b -> volume(.1f)).bounds(x + 262, y + 170, 38, 20).build());
    }
    private void volume(float n) { PlayerConfig.volume = Math.max(0, Math.min(1, PlayerConfig.volume + n)); PlayerConfig.save(); }
    private List<Track> visible() { String q = search == null ? "" : search.getValue().toLowerCase(); return tracks.stream().filter(t -> t.title().toLowerCase().contains(q) || t.id().toString().contains(q)).toList(); }
    @Override public void extractRenderState(GuiGraphicsExtractor g, int mx, int my, float delta) {
        super.extractRenderState(g, mx, my, delta); int x = width / 2 - 150, y = height / 2 - 110;
        g.fill(x - 8, y - 8, x + 308, y + 202, 0xE0202020); g.text(font, title, x, y, 0xFFFFFFFF, true);
        List<Track> list = visible(); g.text(font, "Tracks: " + list.size() + " | Volume: " + Math.round(PlayerConfig.volume * 100) + "%", x, y + 42, 0xFFCCCCCC, true);
        for (int i = 0; i < 5 && scroll + i < list.size(); i++) { Track t = list.get(scroll + i); int yy = y + 59 + i * 16; g.fill(x - 2, yy - 2, x + 302, yy + 13, scroll + i == selected ? 0xFF555533 : 0x80404040); g.text(font, t.title(), x + 3, yy, 0xFFFFFFFF, true); }
        Track now = MusicController.current(); g.text(font, "Playing: " + (now == null ? "—" : now.title()), x, y + 130, 0xFFFFD966, true);
    }
    @Override public boolean mouseClicked(net.minecraft.client.input.MouseButtonEvent event, boolean doubleClick) {
        int x = width / 2 - 150, y = height / 2 - 110;
        if (event.x() >= x && event.x() <= x + 300 && event.y() >= y + 57 && event.y() < y + 137) { selected = scroll + Math.max(0, (int)(event.y() - (y + 59)) / 16); return true; }
        return super.mouseClicked(event, doubleClick);
    }
    @Override public boolean mouseScrolled(double mx, double my, double horizontal, double vertical) { scroll = Math.max(0, Math.min(Math.max(0, visible().size() - 5), scroll - (int)Math.signum(vertical))); return true; }
}
