package dev.kojode.musicplayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import java.util.List;

public final class MusicScreen extends Screen {
    private final List<Track> tracks = Track.discover();
    private EditBox search;
    private int selected, scroll;
    private boolean backgroundOnly;
    public MusicScreen() { super(Component.literal("Music Player")); }
    private int top() { return height / 2 - 117; }
    @Override protected void init() {
        int x = width / 2 - 150, y = top();
        search = new EditBox(font, x, y + 18, 300, 20, Component.literal("Search")); search.setHint(Component.literal("Search music...")); addRenderableWidget(search);
        addRenderableWidget(Button.builder(Component.literal(backgroundOnly ? "Background Music [x]" : "Background Music"), b -> { backgroundOnly = !backgroundOnly; selected = 0; scroll = 0; rebuildWidgets(); }).bounds(x, y + 42, 146, 20).build());
        addRenderableWidget(Button.builder(Component.literal("All Tracks"), b -> { backgroundOnly = false; selected = 0; scroll = 0; rebuildWidgets(); }).bounds(x + 150, y + 42, 150, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Play"), b -> { Track t = visible().stream().skip(selected).findFirst().orElse(null); if (t != null) MusicController.play(Minecraft.getInstance(), t); }).bounds(x, y + 144, 52, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Stop"), b -> MusicController.stop(Minecraft.getInstance())).bounds(x + 56, y + 144, 52, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Pause/Resume"), b -> MusicController.pauseResume(Minecraft.getInstance())).bounds(x + 112, y + 144, 100, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Prev"), b -> MusicController.next(Minecraft.getInstance(), -1)).bounds(x + 216, y + 144, 40, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Next"), b -> MusicController.next(Minecraft.getInstance(), 1)).bounds(x + 260, y + 144, 40, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Loop " + (PlayerConfig.loop ? "ON" : "OFF")), b -> { PlayerConfig.loop = !PlayerConfig.loop; PlayerConfig.playlistLoop = false; PlayerConfig.save(); rebuildWidgets(); }).bounds(x, y + 170, 90, 20).build());
        addRenderableWidget(Button.builder(Component.literal("List loop " + (PlayerConfig.playlistLoop ? "ON" : "OFF")), b -> { PlayerConfig.playlistLoop = !PlayerConfig.playlistLoop; PlayerConfig.loop = false; PlayerConfig.save(); rebuildWidgets(); }).bounds(x + 94, y + 170, 120, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Vol -"), b -> volume(-.1f)).bounds(x + 218, y + 170, 38, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Vol +"), b -> volume(.1f)).bounds(x + 262, y + 170, 38, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Radio: " + (PlayerConfig.dynamicIsland ? "ON" : "OFF")), b -> { PlayerConfig.dynamicIsland = !PlayerConfig.dynamicIsland; PlayerConfig.save(); rebuildWidgets(); }).bounds(x, y + 196, 140, 20).build());
        addRenderableWidget(Button.builder(Component.literal("-"), b -> scaleIsland(-.1f)).bounds(x + 146, y + 196, 32, 20).build());
        addRenderableWidget(Button.builder(Component.literal("+"), b -> scaleIsland(.1f)).bounds(x + 260, y + 196, 32, 20).build());
        addRenderableWidget(Button.builder(Component.literal("UI -"), b -> scaleUi(-.1f)).bounds(x, y + 222, 78, 20).build());
        addRenderableWidget(Button.builder(Component.literal("UI +"), b -> scaleUi(.1f)).bounds(x + 82, y + 222, 78, 20).build());
    }
    private void volume(float n) { MusicController.adjustVolume(Minecraft.getInstance(), n); }
    private void scaleIsland(float amount) { PlayerConfig.islandScale = PlayerConfig.clampIslandScale(PlayerConfig.islandScale + amount); PlayerConfig.save(); }
    private void scaleUi(float amount) { PlayerConfig.uiScale = PlayerConfig.clampUiScale(PlayerConfig.uiScale + amount); PlayerConfig.save(); }
    private List<Track> visible() { String q = search == null ? "" : search.getValue().toLowerCase(); return tracks.stream().filter(t -> !backgroundOnly || t.isBackgroundMusic()).filter(t -> t.title().toLowerCase().contains(q) || t.id().toString().contains(q)).toList(); }
    @Override public void extractRenderState(GuiGraphicsExtractor g, int mx, int my, float delta) {
        int x = width / 2 - 150, y = top();
        double logicalMouseX = logicalX(mx), logicalMouseY = logicalY(my);
        g.pose().pushMatrix();
        g.pose().translate(width / 2.0f, height / 2.0f);
        g.pose().scale(PlayerConfig.uiScale, PlayerConfig.uiScale);
        g.pose().translate(-width / 2.0f, -height / 2.0f);
        // Draw the frame first so vanilla buttons and text fields remain visible above it.
        g.fill(x - 8, y - 8, x + 308, y + 248, 0xFF171512);
        g.fill(x - 5, y - 5, x + 305, y + 245, 0xFF80613A);
        g.fill(x - 3, y - 3, x + 303, y + 243, 0xFF332D25);
        super.extractRenderState(g, (int) logicalMouseX, (int) logicalMouseY, delta);
        g.text(font, title, x, y, 0xFFFFD978, true);
        List<Track> list = visible(); g.text(font, "Titel: " + list.size() + " | Lautstärke: " + Math.round(PlayerConfig.volume * 100) + "%", x, y + 64, 0xFFE3D7C2, true);
        for (int i = 0; i < 4 && scroll + i < list.size(); i++) { Track t = list.get(scroll + i); int yy = y + 78 + i * 13; g.fill(x - 2, yy - 2, x + 302, yy + 11, scroll + i == selected ? 0xFF9C742C : 0xFF51483A); g.text(font, t.title(), x + 3, yy, 0xFFF4E8D2, true); }
        g.fill(x - 2, y + 127, x + 302, y + 141, 0xFF211E19);
        Track now = MusicController.current(); g.text(font, "Jetzt läuft: " + (now == null ? "—" : now.title()), x + 3, y + 130, 0xFFFFE08A, true);
        String islandSize = "Size: " + Math.round(PlayerConfig.islandScale * 100) + "%";
        g.text(font, islandSize, x + 184 + (70 - font.width(islandSize)) / 2, y + 202, 0xFFE3D7C2, true);
        g.fill(x, y + 63, x + 300, y + 77, 0xFF332D25);
        String trackCount = "Tracks: " + list.size() + " | Volume: " + Math.round(PlayerConfig.volume * 100) + "%";
        g.text(font, trackCount, x, y + 64, 0xFFE3D7C2, true);
        g.fill(x - 2, y + 127, x + 302, y + 141, 0xFF211E19);
        String nowPlaying = "Now Playing: " + (now == null ? "-" : now.title());
        g.text(font, nowPlaying, x + 3, y + 130, 0xFFFFE08A, true);
        String uiSize = "Main UI: " + Math.round(PlayerConfig.uiScale * 100) + "%";
        g.text(font, uiSize, x + 174 + (124 - font.width(uiSize)) / 2, y + 228, 0xFFE3D7C2, true);
        g.pose().popMatrix();
    }
    private double logicalX(double x) { return width / 2.0 + (x - width / 2.0) / PlayerConfig.uiScale; }
    private double logicalY(double y) { return height / 2.0 + (y - height / 2.0) / PlayerConfig.uiScale; }
    private MouseButtonEvent logical(MouseButtonEvent event) { return new MouseButtonEvent(logicalX(event.x()), logicalY(event.y()), event.buttonInfo()); }
    @Override public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        MouseButtonEvent input = logical(event);
        int x = width / 2 - 150, y = top();
        if (input.x() >= x && input.x() <= x + 300 && input.y() >= y + 76 && input.y() < y + 129) {
            selected = scroll + Math.max(0, (int)(input.y() - (y + 78)) / 13);
            playUiClick();
            return true;
        }
        if (children().stream().anyMatch(child -> child instanceof Button button && button.isMouseOver(input.x(), input.y()))) playUiClick();
        return super.mouseClicked(input, doubleClick);
    }
    @Override public boolean mouseReleased(MouseButtonEvent event) { return super.mouseReleased(logical(event)); }
    @Override public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) { return super.mouseDragged(logical(event), dragX / PlayerConfig.uiScale, dragY / PlayerConfig.uiScale); }
    @Override public void mouseMoved(double mouseX, double mouseY) { super.mouseMoved(logicalX(mouseX), logicalY(mouseY)); }
    private void playUiClick() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
    @Override public boolean mouseScrolled(double mx, double my, double horizontal, double vertical) { return super.mouseScrolled(logicalX(mx), logicalY(my), horizontal, vertical) || scrollTracks(vertical); }
    private boolean scrollTracks(double vertical) { scroll = Math.max(0, Math.min(Math.max(0, visible().size() - 4), scroll - (int)Math.signum(vertical))); return true; }
}
