# Menu Audio Controls - specification

Client-only FancyMenu addon. Built for the Teachings of Zeta title-menu music player, usable in any
FancyMenu layout.

## What it adds

| FancyMenu id | Kind | Value / properties |
|---|---|---|
| `menu_audio_seek_bar` | element | `audio_element_identifier` = instance id of an Audio element on the same screen |
| `menu_audio_seek` | action | `<audio element id>:<seconds>` |
| `menu_audio_intro_start` | action | `<audio element id>:<track file>:<seconds>` - once per game session |

Seek bar: 2 px rail + knob, drawn in the pack's brass palette. Click/drag moves the knob, the seek
happens on release (no stutter while dragging). A release outside the screen's listeners is caught by
checking the left mouse button every frame. Seeks are clamped to `duration - 0.5 s`, because landing on
the end stops the track and FancyMenu would skip to the next one.

Intro start: the request is used up by the first track the element actually plays. Same track ->
seek to the given second (unless already past it); another track -> nothing. Repeated requests in the
same session are ignored, so the action can sit in the title screen's opening actions. The seek runs in
`ScreenEvent.Render.Post`, the same frame FancyMenu starts the track while rendering the screen.

## FancyMenu internals relied on (verified against 3.9.14 bytecode)

- `ElementRegistry.register(ElementBuilder)`, `ActionRegistry.register(Action)`: plain maps, no lock;
  called from the mod constructor (`ordering="AFTER"` fancymenu), before any layout is loaded.
- Unknown element types / actions in a layout are skipped by FancyMenu (`Layout#buildElementInstances`),
  so layouts using this addon still load without it.
- Properties put into an element's property map are (de)serialized by `ElementBuilder`
  (`deserializeElementInternal` / `serializeElementInternal`); the instance id is set there too.
- `ScreenCustomizationLayerHandler.getActiveLayer().getElementByInstanceIdentifier(id)` ->
  `AudioElement` with public fields `currentAudio` (`IAudio`) and `currentAudioInstance`
  (`supplier.getSourceWithoutPrefix()` = track source; file name = part after the last `/`,
  same as FancyMenu's `audio_element_current_track` placeholder).
- `IAudio#getPlayTime/getDuration/setPlayTime`. `OggAudio` decodes the whole file into one OpenAL buffer
  and seeks with `AL_SEC_OFFSET` (FancyMenu's file-browser preview uses the same call). WAV likewise.
- All of this goes through `adapter.fancymenu.FancyMenuGuard`: the first `LinkageError` (FancyMenu
  changed a field/method) disables the addon for the session with one log line.

## Layers

- `core` - pure Java (PlayerState, SeekMath, IntroStartTracker, ActionValues), unit tested.
- `port` - `MenuAudioPlayer(s)`: what the UI needs from a player.
- `adapter.fancymenu` - the only code touching FancyMenu's audio internals.
- `fancymenu` - element/action classes registered with FancyMenu's public registries.
- `client` - NeoForge event hook for intro starts.
