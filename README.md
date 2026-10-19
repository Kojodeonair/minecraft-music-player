# Minecraft Music Player

A client-side Fabric mod for browsing and manually playing the music included in Minecraft Java Edition 26.3.

> **Development disclosure — vibe-coded:** This project was created iteratively with substantial AI assistance. It builds successfully, but has not received a complete manual security audit or full gameplay QA. AI-assisted development does not guarantee that every feature is bug-free. Please report reproducible issues with your Minecraft, Fabric Loader, and Fabric API versions.

## Features

- Browse, search, and play individual Minecraft soundtrack tracks, including C418 tracks such as *Sweden* and *Wet Hands*.
- Filter to background music and hide tracks that are not present in the active resource packs.
- Pause/resume, stop, skip, repeat a track, or loop the playlist.
- Adjust this player's volume independently of other sound instances, subject to Minecraft's Music and Master volume settings.
- Open the player with a rebindable key; action keys can also be rebound in **Options → Controls**.
- Optional radio-style HUD display, with independently configurable scale.
- Adjustable main window scale. Both UI scales are saved in `config/musicplayer.json`.

The mod is client-only. It uses Minecraft's installed music resources and does not bundle Minecraft audio files.

## Requirements

- Minecraft Java Edition **26.3**
- Java **25**
- Fabric Loader **0.19.5** or newer
- Fabric API **0.161.0+26.3** or compatible

## Build

The Gradle wrapper pins Gradle **9.6.0**. From the project directory, run:

```shell
./gradlew build
```

On Windows:

```powershell
./gradlew.bat build
```

The mod JAR is created in `build/libs/`.

## Install and controls

1. Install Fabric Loader for Minecraft 26.3 and Fabric API.
2. Copy the mod JAR from `build/libs/` into your Fabric instance's `mods` folder.
3. Start the game and press **P** to open the player. Change the open key or action keys in **Options → Controls → Music Player**.
4. Search or scroll the track list, select a song, and use the playback buttons. The default action keys are **K** (play/pause), **J** (stop), **N** (next), **B** (previous), **U** (volume up), and **I** (volume down).
5. Use the **Radio** toggle and adjacent **−/+** buttons for the HUD. Its scale ranges from 25% to 180%. Use **UI −/+** for the main window, which ranges from 65% to 150%.

Pause and resume operate on the selected audio channel to preserve its playback position. Playback gain still respects Minecraft's Music and Master volume settings.

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE). The license applies to this project's code and documentation; Minecraft's own assets and music remain Mojang/Microsoft property and are not included here.
