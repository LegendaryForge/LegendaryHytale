package io.github.legendaryforge.hytale.stormseeker;

import io.github.legendaryforge.legendary.mod.runtime.StormseekerHostRuntime;
import io.github.legendaryforge.legendary.mod.stormseeker.quest.StormseekerMilestoneOutcome;
import io.github.legendaryforge.legendary.mod.stormseeker.quest.StormseekerPhase1Outcome;
import io.github.legendaryforge.legendary.mod.stormseeker.quest.StormseekerPhase1TickView;
import io.github.legendaryforge.legendary.mod.stormseeker.quest.StormseekerProgress;
import io.github.legendaryforge.legendary.mod.stormseeker.trial.anchored.AnchoredTrialSessionStep;
import io.github.legendaryforge.legendary.mod.stormseeker.trial.flowing.FlowHintIntent;
import io.github.legendaryforge.legendary.mod.stormseeker.trial.flowing.FlowingTrialSessionStep;
import io.github.legendaryforge.legendary.mod.stormseeker.trial.flowing.MotionSample;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class HytaleStormseekerHost implements StormseekerHostRuntime {

private static final MotionSample ZERO_MOTION = new MotionSample(0, 0, 0, false);
private static final double MOVING_THRESHOLD = 0.01;

private final Map<String, PlayerState> players = new ConcurrentHashMap<>();

public void addPlayer(String playerId) {
players.putIfAbsent(playerId, new PlayerState());
}

public void removePlayer(String playerId) {
players.remove(playerId);
}

public void updatePosition(String playerId, double x, double y, double z) {
PlayerState state = players.get(playerId);
if (state != null) {
state.updatePosition(x, y, z);
}
}

@Override
public Iterable<String> playerIds() {
return players.keySet();
}

@Override
public MotionSample motionSample(String playerId) {
PlayerState state = players.get(playerId);
if (state == null) {
return ZERO_MOTION;
}
return state.lastMotion;
}

@Override
public StormseekerProgress progress(String playerId) {
PlayerState state = players.get(playerId);
if (state == null) {
return new StormseekerProgress();
}
return state.progress;
}

@Override
public void emitFlowHint(String playerId, FlowHintIntent hint) {
// Presentation hook — future particle/sound effects
}

@Override
public void emitStormseekerMilestone(StormseekerMilestoneOutcome outcome) {
System.out.println("[LegendaryHytale] Milestone: " + outcome);
}

@Override
public void emitPhase1TickView(StormseekerPhase1TickView view) {
// Presentation hook — no-op for now
}

@Override
public void emitPhase1Outcome(StormseekerPhase1Outcome outcome) {
StormseekerHostRuntime.super.emitPhase1Outcome(outcome);
}

@Override
public void onFlowingTrialStep(String playerId, FlowingTrialSessionStep step) {
// Presentation hook — no-op for now
}

@Override
public void onAnchoredTrialStep(String playerId, AnchoredTrialSessionStep step) {
// Presentation hook — no-op for now
}

private static final class PlayerState {
final StormseekerProgress progress = new StormseekerProgress();
MotionSample lastMotion = ZERO_MOTION;

double prevX = Double.NaN;
double prevY = Double.NaN;
double prevZ = Double.NaN;

void updatePosition(double x, double y, double z) {
if (Double.isNaN(prevX)) {
prevX = x;
prevY = y;
prevZ = z;
lastMotion = ZERO_MOTION;
return;
}

double dx = x - prevX;
double dy = y - prevY;
double dz = z - prevZ;
boolean moving = Math.sqrt(dx * dx + dy * dy + dz * dz) > MOVING_THRESHOLD;

lastMotion = new MotionSample(dx, dy, dz, moving);
prevX = x;
prevY = y;
prevZ = z;
}
}
}
