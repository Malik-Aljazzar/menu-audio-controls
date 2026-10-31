package dev.malik.menuaudiocontrols.fancymenu;

import de.keksuccino.fancymenu.customization.action.ActionRegistry;
import de.keksuccino.fancymenu.customization.element.ElementRegistry;
import de.keksuccino.fancymenu.customization.placeholder.PlaceholderRegistry;
import dev.malik.menuaudiocontrols.core.IntroStartTracker;
import dev.malik.menuaudiocontrols.port.MenuAudioPlayers;

/**
 * Registers the element, actions and placeholders with FancyMenu. Runs in the mod constructor (after FancyMenu's,
 * see mods.toml ordering) - before FancyMenu loads any layout that uses them.
 */
public final class FancyMenuIntegration {

    private FancyMenuIntegration() {
    }

    public static void register(MenuAudioPlayers players, IntroStartTracker introStarts) {
        ElementRegistry.register(new SeekBarElementBuilder(players));
        ActionRegistry.register(new SeekAction(players));
        ActionRegistry.register(new IntroStartAction(introStarts));
        PlaceholderRegistry.register(TrackSecondsPlaceholder.playTime(players));
        PlaceholderRegistry.register(TrackSecondsPlaceholder.duration(players));
    }
}
