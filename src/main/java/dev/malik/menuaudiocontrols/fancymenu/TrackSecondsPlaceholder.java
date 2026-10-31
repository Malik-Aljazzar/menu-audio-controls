package dev.malik.menuaudiocontrols.fancymenu;

import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import dev.malik.menuaudiocontrols.core.PlayerState;
import dev.malik.menuaudiocontrols.core.SeekMath;
import dev.malik.menuaudiocontrols.port.MenuAudioPlayer;
import dev.malik.menuaudiocontrols.port.MenuAudioPlayers;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.client.resources.language.I18n;

/**
 * A time of the current track in plain decimal seconds ("83.417"), unlike FancyMenu's own
 * "audio_playtime" (whole seconds as mm:ss). Precise enough to drive a shader: a Ticker copies it into
 * a variable every frame and the shader reads the variable as an fmVarFloat_ uniform.
 * "0" while the element is missing or no track is loaded.
 */
public class TrackSecondsPlaceholder extends Placeholder {

    public static final String ELEMENT_KEY = "element_identifier";

    private final MenuAudioPlayers players;
    private final Function<PlayerState, Float> seconds;
    private final String langKey;

    private TrackSecondsPlaceholder(String id, String langKey, MenuAudioPlayers players,
                                    Function<PlayerState, Float> seconds) {
        super(id);
        this.players = players;
        this.seconds = seconds;
        this.langKey = langKey;
    }

    public static TrackSecondsPlaceholder playTime(MenuAudioPlayers players) {
        return new TrackSecondsPlaceholder("menu_audio_playtime_seconds",
                "menu_audio_controls.placeholders.playtime_seconds", players, PlayerState::playTime);
    }

    public static TrackSecondsPlaceholder duration(MenuAudioPlayers players) {
        return new TrackSecondsPlaceholder("menu_audio_duration_seconds",
                "menu_audio_controls.placeholders.duration_seconds", players, PlayerState::duration);
    }

    @Override
    public boolean canRunAsync() {
        return false;                                         // audio lives on the render thread
    }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString dps) {
        float value = players.find(dps.values.get(ELEMENT_KEY))
                .map(MenuAudioPlayer::state)
                .filter(PlayerState::ready)
                .map(seconds)
                .orElse(0f);
        return SeekMath.decimalSeconds(value);
    }

    @Override
    public List<String> getValueNames() {
        return List.of(ELEMENT_KEY);
    }

    @Override
    public String getDisplayName() {
        return I18n.get(langKey);
    }

    @Override
    public List<String> getDescription() {
        return List.of(I18n.get(langKey + ".desc"));
    }

    @Override
    public String getCategory() {
        return I18n.get("fancymenu.placeholders.categories.audio");
    }

    @Override
    @SuppressWarnings("removal")                              // FancyMenu 3.9.14's own placeholders use build() too
    public DeserializedPlaceholderString getDefaultPlaceholderString() {
        Map<String, String> values = new LinkedHashMap<>();
        values.put(ELEMENT_KEY, "put_identifier_of_audio_element_here");
        return DeserializedPlaceholderString.build(getIdentifier(), values);
    }
}
