package dev.malik.menuaudiocontrols.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SeekMathTest {

    @Test
    void fractionIsZeroUntilATrackIsLoaded() {
        assertEquals(0f, SeekMath.fraction(PlayerState.IDLE));
        assertEquals(0f, SeekMath.fraction(new PlayerState("a.ogg", true, true, 10f, 0f)));
    }

    @Test
    void fractionFollowsPlayTime() {
        assertEquals(0.25f, SeekMath.fraction(new PlayerState("a.ogg", true, true, 50f, 200f)), 1e-6);
        assertEquals(1f, SeekMath.fraction(new PlayerState("a.ogg", true, true, 250f, 200f)));
    }

    @Test
    void seekNeverLandsOnTheEnd() {
        assertEquals(199.5f, SeekMath.secondsAt(1f, 200f), 1e-6);
        assertEquals(199.5f, SeekMath.clampSeconds(500f, 200f), 1e-6);
        assertEquals(0f, SeekMath.clampSeconds(5f, 0.2f));
    }

    @Test
    void invalidInputsClampToTheStart() {
        assertEquals(0f, SeekMath.clampSeconds(Float.NaN, 200f));
        assertEquals(0f, SeekMath.clampSeconds(-3f, 200f));
        assertEquals(0f, SeekMath.secondsAt(-0.5f, 200f));
        assertEquals(0f, SeekMath.clamp01(Float.POSITIVE_INFINITY));
    }

    @Test
    void formatsMinutesAndSeconds() {
        assertEquals("0:00", SeekMath.format(0f));
        assertEquals("0:09", SeekMath.format(9.9f));
        assertEquals("3:42", SeekMath.format(222.4f));
        assertEquals("0:00", SeekMath.format(Float.NaN));
    }
}
