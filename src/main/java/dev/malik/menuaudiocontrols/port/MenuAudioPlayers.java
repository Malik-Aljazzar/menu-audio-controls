package dev.malik.menuaudiocontrols.port;

import java.util.Optional;

public interface MenuAudioPlayers {

    /** The player with this identifier on the screen that is open right now, if there is one. */
    Optional<MenuAudioPlayer> find(String identifier);
}
