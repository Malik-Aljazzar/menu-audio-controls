package dev.malik.menuaudiocontrols.fancymenu;

import de.keksuccino.fancymenu.customization.action.Action;
import dev.malik.menuaudiocontrols.MenuAudioControls;
import dev.malik.menuaudiocontrols.core.ActionValues;
import dev.malik.menuaudiocontrols.core.IntroStartTracker;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * "menu_audio_intro_start", value "audio_element_id:track.ogg:seconds": the first play of that track
 * after the game started begins at the given second. Meant for a screen's opening actions - repeated
 * runs in the same session do nothing.
 */
public class IntroStartAction extends Action {

    public static final String IDENTIFIER = "menu_audio_intro_start";

    private final IntroStartTracker tracker;

    public IntroStartAction(@NotNull IntroStartTracker tracker) {
        super(IDENTIFIER);
        this.tracker = tracker;
    }

    @Override
    public boolean hasValue() {
        return true;
    }

    @Override
    public void execute(@Nullable String value) {
        ActionValues.introStart(value).ifPresentOrElse(
                intro -> tracker.request(intro.playerId(), intro.trackFile(), intro.seconds()),
                () -> MenuAudioControls.LOGGER.warn("Ignoring action {} with value '{}' (expected <audio element id>:<track file>:<seconds>)",
                        IDENTIFIER, value));
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("menu_audio_controls.actions.intro_start");
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.translatable("menu_audio_controls.actions.intro_start.desc");
    }

    @Override
    public @NotNull Component getValueDisplayName() {
        return Component.translatable("menu_audio_controls.actions.intro_start.value");
    }

    @Override
    public @NotNull String getValuePreset() {
        return "audio_element_identifier:track.ogg:30";
    }
}
