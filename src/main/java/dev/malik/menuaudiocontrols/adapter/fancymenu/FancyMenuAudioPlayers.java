package dev.malik.menuaudiocontrols.adapter.fancymenu;

import de.keksuccino.fancymenu.customization.element.elements.audio.AudioElement;
import de.keksuccino.fancymenu.customization.layer.ScreenCustomizationLayer;
import de.keksuccino.fancymenu.customization.layer.ScreenCustomizationLayerHandler;
import dev.malik.menuaudiocontrols.port.MenuAudioPlayer;
import dev.malik.menuaudiocontrols.port.MenuAudioPlayers;
import java.util.Optional;

/** Players are FancyMenu Audio elements of the active screen layer, found by instance identifier. */
public final class FancyMenuAudioPlayers implements MenuAudioPlayers {

    @Override
    public Optional<MenuAudioPlayer> find(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            return Optional.empty();
        }
        return FancyMenuGuard.call(() -> lookup(identifier.trim()), Optional.empty());
    }

    private static Optional<MenuAudioPlayer> lookup(String identifier) {
        ScreenCustomizationLayer layer = ScreenCustomizationLayerHandler.getActiveLayer();
        if (layer != null && layer.getElementByInstanceIdentifier(identifier) instanceof AudioElement audio) {
            return Optional.of(new FancyMenuAudioPlayer(audio));
        }
        return Optional.empty();
    }
}
