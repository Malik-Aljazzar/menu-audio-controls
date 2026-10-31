package dev.malik.menuaudiocontrols.fancymenu;

import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.util.properties.Property;
import dev.malik.menuaudiocontrols.port.MenuAudioPlayers;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Seek bar for the current track of the Audio element named in "audio_element_identifier". */
public class SeekBarElement extends AbstractElement {

    public final Property.StringProperty audioElementIdentifier = putProperty(Property.stringProperty(
            "audio_element_identifier", "", false, false, "menu_audio_controls.elements.seek_bar.audio_element"));

    private final SeekBarWidget widget;

    public SeekBarElement(@NotNull ElementBuilder<?, ?> builder, @NotNull MenuAudioPlayers players) {
        super(builder);
        this.widget = new SeekBarWidget(() -> players.find(audioElementIdentifier.get()));
    }

    @Override
    public @Nullable List<GuiEventListener> getWidgetsToRegister() {
        return isEditor() ? null : List.of(widget);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        widget.setRectangle(getAbsoluteWidth(), getAbsoluteHeight(), getAbsoluteX(), getAbsoluteY());
        widget.visible = shouldRender();
        if (!widget.visible) {
            return;
        }
        if (isEditor()) {
            widget.renderPreview(graphics);
        } else {
            widget.render(graphics, mouseX, mouseY, partial);
        }
    }
}
