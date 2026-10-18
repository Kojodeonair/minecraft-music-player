# Minecraft Music Player

Client-only Fabric project targeting Minecraft Java 26.3.

## Build
Use JDK 25 and Gradle 9.6.0 (Loom 1.17.18 requires Gradle plugin API 9.5.0 or newer); run `gradle build`. Gradle 9.3.0 is too old and fails during plugin resolution. Dependencies: Fabric Loader 0.19.5, Fabric API 0.161.0+26.3, Loom 1.17.18.

## Install and use
Install Fabric Loader for 26.3 and Fabric API 0.161.0+26.3. Copy the built jar from `build/libs/` to the instance's `mods` folder. Press P to open the player; the key is rebindable under Options > Controls. Search and scroll the list, select a track, then use Play, Stop, Pause/Resume, Next/Previous, loop, and volume controls.

Pause is best implemented as stop then restart because the shared sound manager cannot reliably pause one instance alone. Volume is limited by Minecraft's Music category setting too. Vanilla may schedule background music independently; suppressing that needs a version-specific hook into its music manager.
