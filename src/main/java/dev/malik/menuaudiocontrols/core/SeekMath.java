package dev.malik.menuaudiocontrols.core;

import java.util.Locale;

/** Positions on a track: bar fraction <-> seconds, clamped so a seek never ends the track. */
public final class SeekMath {

    /** Seeking onto the very end would stop the track (and skip to the next one) at once. */
    public static final float END_MARGIN_SECONDS = 0.5f;

    private SeekMath() {
    }

    /** How far the current track has played, 0..1 (0 while nothing seekable is loaded). */
    public static float fraction(PlayerState state) {
        return state.seekable() ? clamp01(state.playTime() / state.duration()) : 0f;
    }

    public static float secondsAt(float fraction, float duration) {
        return clampSeconds(clamp01(fraction) * duration, duration);
    }

    public static float clampSeconds(float seconds, float duration) {
        if (!Float.isFinite(seconds) || seconds < 0f) {
            return 0f;
        }
        return Math.min(seconds, Math.max(0f, duration - END_MARGIN_SECONDS));
    }

    public static float clamp01(float value) {
        if (!Float.isFinite(value) || value < 0f) {
            return 0f;
        }
        return Math.min(value, 1f);
    }

    /** Plain decimal seconds with millisecond precision, e.g. 83.417; "0" for invalid or negative times. */
    public static String decimalSeconds(float seconds) {
        if (!Float.isFinite(seconds) || seconds <= 0f) {
            return "0";
        }
        return String.format(Locale.ROOT, "%.3f", seconds);
    }

    /** "m:ss", e.g. 3:07. */
    public static String format(float seconds) {
        int total = Float.isFinite(seconds) ? Math.max(0, (int) seconds) : 0;
        int rest = total % 60;
        return total / 60 + ":" + (rest < 10 ? "0" : "") + rest;
    }
}
