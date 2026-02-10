package io.github.legendaryforge.hytale.command;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.FormattedMessage;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import io.github.legendaryforge.hytale.stormseeker.HytaleStormseekerHost;
import io.github.legendaryforge.legendary.mod.stormseeker.quest.StormseekerProgress;
import io.github.legendaryforge.legendary.mod.stormseeker.trial.flowing.MotionSample;

/**
 * Debug command: /stormseeker status
 *
 * <p>Shows the executing player's current Stormseeker quest state including
 * phase, sigils, motion sample, and position.
 */
public class StormseekerStatusCommand extends AbstractPlayerCommand {

    private final HytaleStormseekerHost host;

    public StormseekerStatusCommand(HytaleStormseekerHost host) {
        super("stormseeker", "Shows Stormseeker quest debug info");
        this.host = host;
    }

    @Override
    protected void execute(CommandContext context, Store<EntityStore> store,
                           Ref<EntityStore> ref, PlayerRef playerRef, World world) {
        String playerId = playerRef.getUuid().toString();
        String username = playerRef.getUsername();

        StormseekerProgress progress = host.progress(playerId);
        MotionSample motion = host.motionSample(playerId);

        StringBuilder sb = new StringBuilder();
        sb.append("--- Stormseeker Status: ").append(username).append(" ---\n");
        sb.append("Phase: ").append(progress.phase()).append("\n");
        sb.append("Sigil A (Flowing): ").append(progress.hasSigilA() ? "GRANTED" : "not earned").append("\n");
        sb.append("Sigil B (Anchored): ").append(progress.hasSigilB() ? "GRANTED" : "not earned").append("\n");
        sb.append("Motion: dx=").append(String.format("%.3f", motion.dx()));
        sb.append(" dy=").append(String.format("%.3f", motion.dy()));
        sb.append(" dz=").append(String.format("%.3f", motion.dz()));
        sb.append(" moving=").append(motion.moving()).append("\n");

        var pos = playerRef.getTransform();
        if (pos != null) {
            var p = pos.getPosition();
            sb.append("Position: ").append(String.format("%.1f, %.1f, %.1f", p.getX(), p.getY(), p.getZ()));
        }

        sendText(context, sb.toString());
    }

    private void sendText(CommandContext context, String text) {
        FormattedMessage fmt = new FormattedMessage();
        fmt.rawText = text;
        context.sendMessage(new Message(fmt));
    }
}
