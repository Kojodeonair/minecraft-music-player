package dev.kojode.musicplayer;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

import java.util.Comparator;
import java.util.List;

/** One directly selectable vanilla soundtrack file, rather than a random multi-file event. */
public record Track(Identifier id, Identifier soundFile, String title) {
    private static Track of(String title, String path) {
        String alias = path.replace('/', '_');
        return new Track(Identifier.fromNamespaceAndPath("musicplayer", "track." + alias),
                Identifier.fromNamespaceAndPath("minecraft", "sounds/" + path + ".ogg"), title);
    }

    private static final List<Track> CATALOG = List.of(
            of("A Familiar Room", "music/game/a_familiar_room"), of("An Ordinary Day", "music/game/an_ordinary_day"),
            of("Ancestry", "music/game/ancestry"), of("Below and Above", "music/game/below_and_above"),
            of("Broken Clocks", "music/game/broken_clocks"), of("Bromeliad", "music/game/bromeliad"),
            of("Clark", "music/game/clark"), of("Comforting Memories", "music/game/comforting_memories"),
            of("Crescent Dunes", "music/game/crescent_dunes"), of("Danny", "music/game/danny"),
            of("Deeper", "music/game/deeper"), of("Dry Hands", "music/game/dry_hands"),
            of("Echo in the Wind", "music/game/echo_in_the_wind"), of("Eld Unknown", "music/game/eld_unknown"),
            of("Endless", "music/game/endless"), of("Featherfall", "music/game/featherfall"),
            of("Fireflies", "music/game/fireflies"), of("Floating Dream", "music/game/floating_dream"),
            of("Haggstrom", "music/game/haggstrom"), of("Infinite Amethyst", "music/game/infinite_amethyst"),
            of("Key", "music/game/key"), of("Komorebi", "music/game/komorebi"),
            of("Left to Bloom", "music/game/left_to_bloom"), of("Lilypad", "music/game/lilypad"),
            of("Living Mice", "music/game/living_mice"), of("Mice on Venus", "music/game/mice_on_venus"),
            of("Minecraft", "music/game/minecraft"), of("One More Day", "music/game/one_more_day"),
            of("O's Piano", "music/game/os_piano"), of("Oxygène", "music/game/oxygene"),
            of("Pokopoko", "music/game/pokopoko"), of("Puzzlebox", "music/game/puzzlebox"),
            of("Stand Tall", "music/game/stand_tall"), of("Subwoofer Lullaby", "music/game/subwoofer_lullaby"),
            of("Sweden", "music/game/sweden"), of("Watcher", "music/game/watcher"),
            of("Wending", "music/game/wending"), of("Wet Hands", "music/game/wet_hands"),
            of("Yakusoku", "music/game/yakusoku"),
            of("Aria Math", "music/game/creative/aria_math"), of("Biome Fest", "music/game/creative/biome_fest"),
            of("Blind Spots", "music/game/creative/blind_spots"), of("Dreiton", "music/game/creative/dreiton"),
            of("Haunt Muskie", "music/game/creative/haunt_muskie"), of("Taswell", "music/game/creative/taswell"),
            of("Alpha", "music/game/end/alpha"), of("Boss", "music/game/end/boss"),
            of("The End", "music/game/end/the_end"),
            of("Ballad of the Cats", "music/game/nether/ballad_of_the_cats"),
            of("Concrete Halls", "music/game/nether/concrete_halls"), of("Dead Voxel", "music/game/nether/dead_voxel"),
            of("Warmth", "music/game/nether/warmth"),
            of("Aerie", "music/game/swamp/aerie"), of("Firebugs", "music/game/swamp/firebugs"),
            of("Labyrinthine", "music/game/swamp/labyrinthine"),
            of("Axolotl", "music/game/water/axolotl"), of("Dragon Fish", "music/game/water/dragon_fish"),
            of("Shuniji", "music/game/water/shuniji"),
            of("Beginning 2", "music/menu/beginning_2"), of("Floating Trees", "music/menu/floating_trees"),
            of("Moog City 2", "music/menu/moog_city_2"), of("Mutation", "music/menu/mutation")
    );

    public boolean isBackgroundMusic() { return true; }

    /** Only list files present in the active resource packs/game assets. */
    public static List<Track> discover() {
        var resources = Minecraft.getInstance().getResourceManager();
        return CATALOG.stream().filter(track -> resources.getResource(track.soundFile()).isPresent())
                .sorted(Comparator.comparing(Track::title, String.CASE_INSENSITIVE_ORDER)).toList();
    }
}
