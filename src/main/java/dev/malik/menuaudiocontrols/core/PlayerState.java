package dev.malik.menuaudiocontrols.core;

import org.jetbrains.annotations.Nullable;

/**
 * Snapshot of a menu audio player.
 *
 * @param trackFile file name of the current track ("sweden.ogg"), null before the player picked one
 * @param ready     the current track is loaded and can be played/sought
 */
public record PlayerState(@Nullable String trackFile, boolean ready, boolean playing, float playTime, float duration) {

    public static final PlayerState IDLE = new PlayerState(null, false, false, 0f, 0f);

    public boolean seekable() {
        return ready && duration > 0f;
    }
}
