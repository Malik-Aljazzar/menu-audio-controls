package dev.malik.menuaudiocontrols.fancymenu;

import de.keksuccino.fancymenu.customization.element.editor.AbstractEditorElement;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import org.jetbrains.annotations.NotNull;

public class SeekBarEditorElement extends AbstractEditorElement<SeekBarEditorElement, SeekBarElement> {

    public SeekBarEditorElement(@NotNull SeekBarElement element, @NotNull LayoutEditorScreen editor) {
        super(element, editor);
    }

    @Override
    public void init() {
        super.init();
        element.audioElementIdentifier.buildContextMenuEntryAndAddTo(rightClickMenu, this);
    }
}
