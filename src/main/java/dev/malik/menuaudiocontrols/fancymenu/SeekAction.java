package dev.malik.menuaudiocontrols.fancymenu;

import de.keksuccino.fancymenu.customization.action.Action;
import dev.malik.menuaudiocontrols.MenuAudioControls;
import dev.malik.menuaudiocontrols.core.ActionValues;
import dev.malik.menuaudiocontrols.port.MenuAudioPlayers;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** "menu_audio_seek", value "audio_element_id:seconds": jumps within the current track. */
public class SeekAction extends Action {

    public static final String IDENTIFIER = "menu_audio_seek";

    private final MenuAudioPlayers players;

    public SeekAction(@NotNull MenuAudioPlayers players) {
        super(IDENTIFIER);
        this.players = players;
    }

    @Override
    public boolean canRunAsync() {
        return false;                                         // audio lives on the render thread
    }

    @Override
    public boolean hasValue() {
        return true;
    }

    @Override
    public void execute(@Nullable String value) {
        ActionValues.seek(value).ifPresentOrElse(
                seek -> players.find(seek.playerId()).ifPresent(p -> p.seek(seek.seconds())),
                () -> MenuAudioControls.LOGGER.warn("Ignoring action {} with value '{}' (expected <audio element id>:<seconds>)",
                        IDENTIFIER, value));
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("menu_audio_controls.actions.seek");
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.translatable("menu_audio_controls.actions.seek.desc");
    }

    @Override
    public @NotNull Component getValueDisplayName() {
        return Component.translatable("menu_audio_controls.actions.seek.value");
    }

    @Override
    public @NotNull String getValuePreset() {
        return "audio_element_identifier:30";
    }
}
