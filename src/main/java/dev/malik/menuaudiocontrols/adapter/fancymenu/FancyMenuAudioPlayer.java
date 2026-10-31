package dev.malik.menuaudiocontrols.adapter.fancymenu;

import de.keksuccino.fancymenu.customization.element.elements.audio.AudioElement;
import de.keksuccino.fancymenu.util.resource.resources.audio.IAudio;
import dev.malik.menuaudiocontrols.core.PlayerState;
import dev.malik.menuaudiocontrols.core.SeekMath;
import dev.malik.menuaudiocontrols.port.MenuAudioPlayer;
import org.jetbrains.annotations.Nullable;

/**
 * One Audio element. Seeking uses IAudio#setPlayTime, which FancyMenu's OGG audio implements as an
 * OpenAL AL_SEC_OFFSET on the fully decoded buffer (FancyMenu's own file-browser preview seeks the same way).
 */
final class FancyMenuAudioPlayer implements MenuAudioPlayer {

    private final AudioElement element;

    FancyMenuAudioPlayer(AudioElement element) {
        this.element = element;
    }

    @Override
    public PlayerState state() {
        return FancyMenuGuard.call(this::readState, PlayerState.IDLE);
    }

    @Override
    public void seek(float seconds) {
        FancyMenuGuard.run(() -> {
            IAudio audio = element.currentAudio;
            if (audio != null && audio.isReady()) {
                audio.setPlayTime(SeekMath.clampSeconds(seconds, audio.getDuration()));
            }
        });
    }

    private PlayerState readState() {
        IAudio audio = element.currentAudio;
        AudioElement.AudioInstance track = element.currentAudioInstance;
        String file = track != null ? fileName(track.supplier.getSourceWithoutPrefix()) : null;
        if (audio == null || !audio.isReady()) {
            return new PlayerState(file, false, false, 0f, 0f);
        }
        return new PlayerState(file, true, audio.isPlaying(), audio.getPlayTime(), audio.getDuration());
    }

    /** Same naming as FancyMenu's "current track" placeholder: the part after the last slash. */
    @Nullable
    static String fileName(@Nullable String source) {
        if (source == null) {
            return null;
        }
        int slash = Math.max(source.lastIndexOf('/'), source.lastIndexOf('\\'));
        return source.substring(slash + 1);
    }
}
