package dev.malik.menuaudiocontrols.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.OptionalDouble;
import org.junit.jupiter.api.Test;

class IntroStartTrackerTest {

    private static PlayerState playing(String file, float at) {
        return new PlayerState(file, true, true, at, 220f);
    }

    @Test
    void firstPlayOfTheTrackSeeksOnce() {
        IntroStartTracker tracker = new IntroStartTracker();
        tracker.request("music", "sweden.ogg", 30f);

        assertEquals(OptionalDouble.of(30f), tracker.onFrame("music", playing("sweden.ogg", 0.02f)));
        assertEquals(OptionalDouble.empty(), tracker.onFrame("music", playing("sweden.ogg", 0.05f)));
        assertTrue(tracker.pendingPlayers().isEmpty());
    }

    @Test
    void waitsUntilTheTrackIsLoadedAndPlaying() {
        IntroStartTracker tracker = new IntroStartTracker();
        tracker.request("music", "sweden.ogg", 30f);

        assertEquals(OptionalDouble.empty(), tracker.onFrame("music", PlayerState.IDLE));
        assertEquals(OptionalDouble.empty(), tracker.onFrame("music", new PlayerState("sweden.ogg", false, false, 0f, 0f)));
        assertEquals(OptionalDouble.empty(), tracker.onFrame("music", new PlayerState("sweden.ogg", true, false, 0f, 220f)));
        assertEquals(List.of("music"), tracker.pendingPlayers());
        assertEquals(OptionalDouble.of(30f), tracker.onFrame("music", playing("sweden.ogg", 0f)));
    }

    @Test
    void anotherFirstTrackUsesTheRequestUp() {
        IntroStartTracker tracker = new IntroStartTracker();
        tracker.request("music", "sweden.ogg", 30f);

        assertEquals(OptionalDouble.empty(), tracker.onFrame("music", playing("cat.ogg", 0f)));
        assertEquals(OptionalDouble.empty(), tracker.onFrame("music", playing("sweden.ogg", 0f)));
    }

    @Test
    void repeatedRequestsInTheSameSessionDoNothing() {
        IntroStartTracker tracker = new IntroStartTracker();
        tracker.request("music", "sweden.ogg", 30f);
        tracker.onFrame("music", playing("sweden.ogg", 0f));

        tracker.request("music", "sweden.ogg", 30f);           // title screen opened again
        assertTrue(tracker.pendingPlayers().isEmpty());
        assertEquals(OptionalDouble.empty(), tracker.onFrame("music", playing("sweden.ogg", 0f)));
    }

    @Test
    void alreadyPastTheIntroIsLeftAlone() {
        IntroStartTracker tracker = new IntroStartTracker();
        tracker.request("music", "sweden.ogg", 30f);

        assertEquals(OptionalDouble.empty(), tracker.onFrame("music", playing("sweden.ogg", 45f)));
    }
}
