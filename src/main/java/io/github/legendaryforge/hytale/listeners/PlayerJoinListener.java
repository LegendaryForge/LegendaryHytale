package io.github.legendaryforge.hytale.listeners;

import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import javax.annotation.Nonnull;

public class PlayerJoinListener {

    public void onPlayerConnect(@Nonnull PlayerConnectEvent event) {
        String playerName = event.getPlayerRef().getUsername();
        System.out.println("[LegendaryHytale] Player connected: " + playerName);
        System.out.println("[LegendaryHytale] Stormseeker questline will be available here!");
    }
}
