package dev.malik.menuaudiocontrols.fancymenu;

import dev.malik.menuaudiocontrols.core.PlayerState;
import dev.malik.menuaudiocontrols.core.SeekMath;
import dev.malik.menuaudiocontrols.port.MenuAudioPlayer;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;

/**
 * Pixel-art progress rail with a knob. Click or drag moves the knob; the seek happens on release,
 * so dragging doesn't stutter the audio. Hovering shows the time under the cursor.
 */
final class SeekBarWidget extends AbstractWidget {

    private static final int OUTLINE = 0xFF1A1224;
    private static final int RAIL = 0xFF34244A;
    private static final int FILL = 0xFFB0782E;
    private static final int FILL_LIGHT = 0xFFE8B450;
    private static final int KNOB = 0xFFFFE08A;
    private static final int KNOB_HOT = 0xFFFFF4DC;
    private static final float PREVIEW_FRACTION = 0.35f;

    private final Supplier<Optional<MenuAudioPlayer>> player;
    private float dragFraction = -1f;                       // >= 0 while the knob is held

    SeekBarWidget(Supplier<Optional<MenuAudioPlayer>> player) {
        super(0, 0, 0, 0, Component.translatable("menu_audio_controls.seek_bar.narration"));
        this.player = player;
    }

    void renderPreview(GuiGraphics graphics) {
        draw(graphics, PREVIEW_FRACTION, false);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        PlayerState state = state();
        active = state.seekable();
        if (dragFraction >= 0f && !Minecraft.getInstance().mouseHandler.isLeftPressed()) {
            onRelease(mouseX, mouseY);                       // release happened outside the screen's listeners
        }
        boolean hot = active && (isHovered() || dragFraction >= 0f);
        draw(graphics, dragFraction >= 0f ? dragFraction : SeekMath.fraction(state), hot);
        if (hot) {
            float at = dragFraction >= 0f ? dragFraction : fractionAt(mouseX);
            Component time = Component.literal(SeekMath.format(SeekMath.secondsAt(at, state.duration())));
            graphics.renderTooltip(Minecraft.getInstance().font, time, mouseX, mouseY);
        }
    }

    private void draw(GuiGraphics graphics, float fraction, boolean hot) {
        int x = getX();
        int w = getWidth();
        int top = getY() + getHeight() / 2 - 1;                // 2 px rail inside a 1 px outline
        int knobX = x + 1 + Math.round(fraction * Math.max(0, w - 3));
        graphics.fill(x, top - 1, x + w, top + 3, OUTLINE);
        graphics.fill(x + 1, top, x + w - 1, top + 2, RAIL);
        graphics.fill(x + 1, top, knobX, top + 2, FILL);
        graphics.fill(x + 1, top, knobX, top + 1, FILL_LIGHT);
        graphics.fill(knobX - 1, getY(), knobX + 2, getY() + getHeight(), OUTLINE);
        graphics.fill(knobX, getY() + 1, knobX + 1, getY() + getHeight() - 1, hot ? KNOB_HOT : KNOB);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        dragFraction = fractionAt(mouseX);
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
        if (dragFraction >= 0f) {
            dragFraction = fractionAt(mouseX);
        }
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        if (dragFraction < 0f) {
            return;
        }
        float target = dragFraction;
        dragFraction = -1f;
        player.get().ifPresent(p -> {
            PlayerState state = p.state();
            if (state.seekable()) {
                p.seek(SeekMath.secondsAt(target, state.duration()));
            }
        });
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        // a seek bar doesn't click
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        defaultButtonNarrationText(output);
    }

    private PlayerState state() {
        return player.get().map(MenuAudioPlayer::state).orElse(PlayerState.IDLE);
    }

    private float fractionAt(double mouseX) {
        return SeekMath.clamp01((float) ((mouseX - getX() - 1) / Math.max(1, getWidth() - 3)));
    }
}
