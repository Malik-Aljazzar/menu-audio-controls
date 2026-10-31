package dev.malik.menuaudiocontrols.client;

import dev.malik.menuaudiocontrols.core.IntroStartTracker;
import dev.malik.menuaudiocontrols.port.MenuAudioPlayers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

/**
 * Applies pending intro starts right after a screen rendered: FancyMenu starts the track while
 * rendering the screen, so the seek lands in the same frame and the skipped part is never heard.
 */
public final class IntroStartHandler {

    private final MenuAudioPlayers players;
    private final IntroStartTracker tracker;

    public IntroStartHandler(MenuAudioPlayers players, IntroStartTracker tracker) {
        this.players = players;
        this.tracker = tracker;
    }

    @SubscribeEvent
    public void onScreenRendered(ScreenEvent.Render.Post event) {
        for (String id : tracker.pendingPlayers()) {
            players.find(id).ifPresent(player ->
                    tracker.onFrame(id, player.state()).ifPresent(seconds -> player.seek((float) seconds)));
        }
    }
}
