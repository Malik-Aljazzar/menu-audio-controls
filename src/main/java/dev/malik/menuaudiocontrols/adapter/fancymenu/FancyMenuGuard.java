package dev.malik.menuaudiocontrols.adapter.fancymenu;

import dev.malik.menuaudiocontrols.MenuAudioControls;
import java.util.function.Supplier;

/**
 * The adapter reads FancyMenu internals (Audio element fields, IAudio). If a FancyMenu update
 * changes them, the first LinkageError switches this mod off for the session - a missing seek bar
 * instead of a menu that crashes every frame.
 */
final class FancyMenuGuard {

    private static volatile boolean disabled;

    private FancyMenuGuard() {
    }

    static boolean disabled() {
        return disabled;
    }

    static <T> T call(Supplier<T> access, T fallback) {
        if (disabled) {
            return fallback;
        }
        try {
            return access.get();
        } catch (LinkageError e) {
            disable(e);
            return fallback;
        }
    }

    static void run(Runnable access) {
        call(() -> {
            access.run();
            return null;
        }, null);
    }

    private static void disable(LinkageError e) {
        disabled = true;
        MenuAudioControls.LOGGER.error("FancyMenu's audio internals changed - menu audio controls are disabled. "
                + "Update Menu Audio Controls for this FancyMenu version.", e);
    }
}
