package io.github.legendaryforge.hytale;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import io.github.legendaryforge.hytale.listeners.PlayerJoinListener;
import javax.annotation.Nonnull;

public class LegendaryHytalePlugin extends JavaPlugin {

    private PlayerJoinListener playerJoinListener;

    public LegendaryHytalePlugin(@Nonnull JavaPluginInit init) {
        super(init);
        getLogger().atInfo().log("LegendaryHytale plugin initializing...");
    }

    @Override
    protected void start() {
        getLogger().atInfo().log("LegendaryHytale plugin enabled!");
        getLogger().atInfo().log("Stormseeker questline integration active.");
        
        // Register event listener
        playerJoinListener = new PlayerJoinListener();
        getEventRegistry().registerGlobal(PlayerConnectEvent.class, playerJoinListener::onPlayerConnect);
        
        getLogger().atInfo().log("Player connect listener registered.");
    }

    @Override
    protected void shutdown() {
        getLogger().atInfo().log("LegendaryHytale plugin disabled.");
    }
}
