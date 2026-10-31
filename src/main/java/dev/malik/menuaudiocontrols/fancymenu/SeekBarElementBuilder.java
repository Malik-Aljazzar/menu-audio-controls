package dev.malik.menuaudiocontrols.fancymenu;

import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.customization.element.SerializedElement;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import dev.malik.menuaudiocontrols.port.MenuAudioPlayers;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Element type "menu_audio_seek_bar". Its only setting is a Property, which FancyMenu (de)serializes itself. */
public class SeekBarElementBuilder extends ElementBuilder<SeekBarElement, SeekBarEditorElement> {

    public static final String IDENTIFIER = "menu_audio_seek_bar";

    private final MenuAudioPlayers players;

    public SeekBarElementBuilder(@NotNull MenuAudioPlayers players) {
        super(IDENTIFIER);
        this.players = players;
    }

    @Override
    public @NotNull SeekBarElement buildDefaultInstance() {
        SeekBarElement element = new SeekBarElement(this, players);
        element.baseWidth = 100;
        element.baseHeight = 7;
        return element;
    }

    @Override
    public SeekBarElement deserializeElement(@NotNull SerializedElement serialized) {
        return buildDefaultInstance();
    }

    @Override
    protected SerializedElement serializeElement(@NotNull SeekBarElement element, @NotNull SerializedElement serializeTo) {
        return serializeTo;
    }

    @Override
    public @NotNull SeekBarEditorElement wrapIntoEditorElement(@NotNull SeekBarElement element, @NotNull LayoutEditorScreen editor) {
        return new SeekBarEditorElement(element, editor);
    }

    @Override
    public @NotNull Component getDisplayName(@Nullable AbstractElement element) {
        return Component.translatable("menu_audio_controls.elements.seek_bar");
    }

    @Override
    public @Nullable Component[] getDescription(@Nullable AbstractElement element) {
        return new Component[]{Component.translatable("menu_audio_controls.elements.seek_bar.desc")};
    }
}
