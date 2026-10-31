package dev.malik.menuaudiocontrols.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

/**
 * "The first time this track plays after the game started, begin at N seconds."
 *
 * <p>One request per player and game session: later requests for the same player are ignored, so
 * the action can sit in a screen's opening actions and still only act on the very first play.
 * The request is used up by the first track the player actually plays - if that is another
 * track (skipped before it started), nothing is sought and later plays start normally.
 */
public final class IntroStartTracker {

    private final Map<String, Request> requests = new HashMap<>();

    public synchronized void request(String playerId, String trackFile, float seconds) {
        requests.putIfAbsent(playerId, new Request(trackFile, seconds));
    }

    public synchronized List<String> pendingPlayers() {
        List<String> pending = new ArrayList<>();
        requests.forEach((id, request) -> {
            if (!request.done) {
                pending.add(id);
            }
        });
        return pending;
    }

    /** Called every frame for a pending player; returns where to seek, once. */
    public synchronized OptionalDouble onFrame(String playerId, PlayerState state) {
        Request request = requests.get(playerId);
        if (request == null || request.done || state.trackFile() == null || !state.ready()) {
            return OptionalDouble.empty();
        }
        if (!state.trackFile().equals(request.trackFile)) {
            request.done = true;
            return OptionalDouble.empty();
        }
        if (!state.playing()) {
            return OptionalDouble.empty();            // the player starts it a moment later
        }
        request.done = true;
        return state.playTime() < request.seconds ? OptionalDouble.of(request.seconds) : OptionalDouble.empty();
    }

    private static final class Request {
        final String trackFile;
        final float seconds;
        boolean done;

        Request(String trackFile, float seconds) {
            this.trackFile = trackFile;
            this.seconds = seconds;
        }
    }
}
