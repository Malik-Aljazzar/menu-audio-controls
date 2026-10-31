# Changelog

All notable changes to Menu Audio Controls are documented in this file.

## [1.0.0]

### New
- "Audio Seek Bar" element for FancyMenu layouts: shows how far the current track of an Audio element has played. Click or drag it to jump to another position; hovering shows the time under the cursor.
- "Seek Audio Track" action: jumps the current track of an Audio element to a position (`<audio element id>:<seconds>`).
- "Start Track At (Once Per Game Start)" action: the first time a given track plays after the game started, it begins at a set second (`<audio element id>:<track file>:<seconds>`), e.g. to skip a long intro on the title screen. Every later play of that track starts from the beginning.
- If a future FancyMenu version changes the audio internals this addon relies on, it switches itself off with a log message instead of crashing your menus.
