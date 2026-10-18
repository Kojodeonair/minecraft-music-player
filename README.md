# Minecraft Music Player

Client-only Fabric project targeting Minecraft Java 26.3.

## Build
Use JDK 25 and Gradle 9.6.0 (Loom 1.17.18 requires Gradle plugin API 9.5.0 or newer); run `gradle build`. Gradle 9.3.0 is too old and fails during plugin resolution. Minecraft 26.1+ uses Loom's non-remapping mode, so the Loader and Fabric API dependencies use Gradle's regular `implementation` configuration. Dependencies: Fabric Loader 0.19.5, Fabric API 0.161.0+26.3, Loom 1.17.18.

## Install and use
Install Fabric Loader for 26.3 and Fabric API 0.161.0+26.3. Copy the built jar from `build/libs/` to the instance's `mods` folder. Press P to open the player; the key is rebindable under Options > Controls. The list now offers individual vanilla soundtrack files, including C418 tracks such as Sweden, Wet Hands, and Subwoofer Lullaby, alongside creative, End, Nether, water, swamp, and menu music. Each entry is a separate selectable track, not a random music event containing multiple alternatives. The catalog checks installed resources at runtime, so tracks absent from the active game resources/resource packs are hidden. Search and scroll the list, select a track, then use Play, Stop, Pause/Resume, Next/Previous, loop, and volume controls. The UI uses a dark wood and stone panel with gold highlights; its background is drawn behind the standard Minecraft widgets so buttons stay readable.

Pause is best implemented as stop then restart because the shared sound manager cannot reliably pause one instance alone. Volume is limited by Minecraft's Music category setting too. A small client-only mixin stops the vanilla music manager and skips its scheduling tick while a manually started mod track is active; after stop or pause, vanilla music scheduling resumes.
