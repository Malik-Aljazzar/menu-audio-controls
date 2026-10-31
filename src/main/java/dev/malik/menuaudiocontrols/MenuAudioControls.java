package dev.malik.menuaudiocontrols;

import com.mojang.logging.LogUtils;
import dev.malik.menuaudiocontrols.adapter.fancymenu.FancyMenuAudioPlayers;
import dev.malik.menuaudiocontrols.client.IntroStartHandler;
import dev.malik.menuaudiocontrols.core.IntroStartTracker;
import dev.malik.menuaudiocontrols.fancymenu.FancyMenuIntegration;
import dev.malik.menuaudiocontrols.port.MenuAudioPlayers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(value = MenuAudioControls.MODID, dist = Dist.CLIENT)
public class MenuAudioControls {
    public static final String MODID = "menu_audio_controls";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MenuAudioControls(IEventBus modEventBus, ModContainer modContainer) {
        MenuAudioPlayers players = new FancyMenuAudioPlayers();
        IntroStartTracker introStarts = new IntroStartTracker();
        FancyMenuIntegration.register(players, introStarts);
        NeoForge.EVENT_BUS.register(new IntroStartHandler(players, introStarts));
        LOGGER.info("Menu Audio Controls loaded (FancyMenu: audio seek bar, seek + intro start actions)");
    }
}
