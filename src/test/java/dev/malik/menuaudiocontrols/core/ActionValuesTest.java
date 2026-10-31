package dev.malik.menuaudiocontrols.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class ActionValuesTest {

    @Test
    void parsesSeek() {
        assertEquals(Optional.of(new ActionValues.Seek("toz_menu_music", 42.5f)), ActionValues.seek(" toz_menu_music : 42.5 "));
    }

    @Test
    void parsesIntroStart() {
        assertEquals(Optional.of(new ActionValues.IntroStart("toz_menu_music", "sweden.ogg", 30f)),
                ActionValues.introStart("toz_menu_music:sweden.ogg:30"));
    }

    @Test
    void rejectsMalformedValues() {
        assertTrue(ActionValues.seek(null).isEmpty());
        assertTrue(ActionValues.seek("toz_menu_music").isEmpty());
        assertTrue(ActionValues.seek(":12").isEmpty());
        assertTrue(ActionValues.seek("music:abc").isEmpty());
        assertTrue(ActionValues.seek("music:-1").isEmpty());
        assertTrue(ActionValues.seek("music:NaN").isEmpty());
        assertTrue(ActionValues.seek("music:1:2").isEmpty());
        assertTrue(ActionValues.introStart("music:30").isEmpty());
        assertTrue(ActionValues.introStart("music::30").isEmpty());
    }
}
