# Menu Audio Controls

A small FancyMenu addon that adds seeking to FancyMenu's Audio element.

## Features

- **Audio Seek Bar** (element): shows the progress of the current track. Click or drag to jump to another position.
- **Seek Audio Track** (action): jumps to a position. Value: `<audio element id>:<seconds>`
- **Start Track At, Once Per Game Start** (action): the first play of a track after game start begins at a set second, later plays start from the beginning. Value: `<audio element id>:<track file>:<seconds>`
- **Audio Playtime / Duration (Seconds)** (placeholders): exact time of the current track, e.g. `83.417`. Put it into a variable with a Ticker to sync a GLSL shader to the music (`fmVarFloat_<variable>` uniform).

## Usage

1. Add an Audio element and copy its ID (right-click → Copy Element ID).
2. Add an Audio Seek Bar and set its Audio Element Identifier to that ID.
3. Optional: add Start Track At to the layout's opening actions.

## Notes

- Client-side only.
- OGG and WAV tracks (the formats FancyMenu plays).
- Layouts using this addon still load without it.

## Requirements

- Minecraft 1.21.1, NeoForge
- FancyMenu 3.9.14 or newer

Made for the [Teachings of Zeta](https://www.curseforge.com/minecraft/modpacks/teachings-of-zeta) modpack. MIT license, source on [GitHub](https://github.com/Malik-Aljazzar/menu-audio-controls). Unofficial addon, not affiliated with FancyMenu.
