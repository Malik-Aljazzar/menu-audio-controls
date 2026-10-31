package dev.malik.menuaudiocontrols.port;

import dev.malik.menuaudiocontrols.core.PlayerState;

/** A music player on the current screen (implemented on top of FancyMenu's Audio element). */
public interface MenuAudioPlayer {

    PlayerState state();

    /** Jumps within the current track; ignored while no track is loaded. */
    void seek(float seconds);
}
