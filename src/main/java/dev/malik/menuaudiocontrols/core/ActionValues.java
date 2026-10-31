package dev.malik.menuaudiocontrols.core;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;

/** Parses the values of this mod's FancyMenu actions ("element:seconds", "element:file:seconds"). */
public final class ActionValues {

    public record Seek(String playerId, float seconds) {
    }

    public record IntroStart(String playerId, String trackFile, float seconds) {
    }

    private ActionValues() {
    }

    public static Optional<Seek> seek(@Nullable String value) {
        String[] parts = split(value, 2);
        if (parts == null) {
            return Optional.empty();
        }
        return seconds(parts[1]).map(s -> new Seek(parts[0], s));
    }

    public static Optional<IntroStart> introStart(@Nullable String value) {
        String[] parts = split(value, 3);
        if (parts == null) {
            return Optional.empty();
        }
        return seconds(parts[2]).map(s -> new IntroStart(parts[0], parts[1], s));
    }

    @Nullable
    private static String[] split(@Nullable String value, int count) {
        if (value == null) {
            return null;
        }
        String[] parts = value.split(":", -1);
        if (parts.length != count) {
            return null;
        }
        for (int i = 0; i < count; i++) {
            parts[i] = parts[i].trim();
            if (parts[i].isEmpty()) {
                return null;
            }
        }
        return parts;
    }

    private static Optional<Float> seconds(String raw) {
        try {
            float seconds = Float.parseFloat(raw);
            return Float.isFinite(seconds) && seconds >= 0f ? Optional.of(seconds) : Optional.empty();
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
