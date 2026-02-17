# Stormseeker Phase 0 & 1 Redesign — Final Design

## Context

The original Phase 0/1 design relied on a **movement restriction mechanic** — an elemental entity would create directional resistance, making it harder to walk away and easier to walk toward. After a thorough audit of Hytale's server API (`2026.02.06-aa1b071c2`), **movement speed modification is not exposed to plugins**. This document defines the replacement design.

---

## Design Philosophy (Unchanged)

1. No quest markers, no journal entries, no waypoints
2. The world teaches through feel, not UI
3. Player can opt out at any time — no forced participation
4. Environmental storytelling over explicit narrative
5. Each phase should feel earned

---

## The Resonator Structure (Independent Behavior)

The Resonator (Ancient Air Leyline Calibration Station) exists in the world as a permanent structure. **Regardless of any player's quest state**, it exhibits the following behavior:

- **During thunder storms:** The Resonator activates — emits strong DynamicLight, visually glows, becomes alive
- **Outside storms:** Dormant, unremarkable

This is not quest-gated. Any player exploring during a storm can see a glowing structure in the distance. This is intentional.

**Skip path:** Any player who enters the Resonator's radius and steps on a plate during a thunder storm triggers Phase 1.5 (Attunement), regardless of whether they've encountered the elemental or followed a trail. Right place, right time — rewarded. Phases 0 and 1 are skipped.

---

## Phase 0: The Watching Elemental

### Trigger
- Player is Phase 0 (new to the questline)
- A thunder storm is active
- **Pre-validation passes:** A viable Resonator exists within range (~500 blocks) with no ocean crossing required. If no valid path exists, the elemental does not appear. The player never knows about a failed check.

### Behavior
1. An **air/electrical elemental** spawns near the player (~25 blocks away)
2. The elemental **does not approach, does not attack, does not interact** — it hovers and watches
3. It persists for the duration of the storm, drifting subtly to stay visible (e.g., repositioning if the player turns away)
4. **First approach:** When the player comes within ~10 blocks, the elemental reacts immediately:
   - It **streaks away** at high speed toward the Resonator
   - As it flies, it leaves **scorched earth / electrified ground patches** on the natural terrain below
   - After traveling a visible distance, it moves beyond the player's sight and **despawns**
   - The trail remains until the storm ends

### If the player ignores the elemental
- The elemental fades when the storm ends
- Next thunder storm: the elemental appears again, same behavior
- No punishment, no lost progress, no conditioning to ignore it

### Key Design Decisions
- The elemental appears on the **first storm** of a Phase 0 player's experience (pending path validation)
- The elemental **bolts on the first approach** — no multi-storm watching phase, no training the player to ignore it
- Within a single storm, the watching period is brief (~1-2 minutes of hovering before the player approaches)
- The entire Phase 0 → 1 → 1.5 sequence is **completable within a single storm**

---

## Phase 1: The Trek

### Trigger
- The elemental has bolted, leaving a trail of scorched earth

### Behavior
1. The player follows the trail of scorched/electrified ground patches toward the Resonator
2. The trail is **self-directed** — no escort, no guide, no timer pressure beyond the storm's duration
3. The Resonator is already **glowing in the distance** (independent storm behavior), serving as a long-range visual beacon
4. Distant lightning VFX may appear at the Resonator's location for additional atmospheric guidance

### Trail Characteristics

**Placement:**
- Scorched earth patches placed every ~20 blocks (15-25 range) along the elemental's flight path
- Placed on the **highest natural block** at each position (ground-snapped)
- Only placed on **natural blocks** (grass, dirt, sand, stone, snow, etc.)
- **Not placed on artificial/player-built blocks** — trail naturally skips over structures

**Obstacle Handling:**
- **Natural terrain (cliffs, ravines, rivers, small bodies of water):** The trail goes straight through. These are fair game — the player climbs, swims, bridges across. This is part of the adventure.
- **Artificial structures (player builds, walls, castles):** The elemental's flight path curves around these. The elemental is a sentient, natural entity — it wouldn't fly through a building. The trail reflects this intelligence.
- **Large gaps:** Where structures create a gap in the trail, the Resonator's storm glow and distant lightning serve as long-range direction confirmation.

**Trail Intelligence:**
The elemental knows the player can't fly. While it moves through the air, the trail it leaves is designed to be followable on foot. It doesn't pathfind a perfectly walkable route (natural obstacles are fair game), but it avoids leading the player into artificial dead ends.

Implementation approach: scan ahead along the flight vector, detect artificial blocks, nudge the path laterally to go around them. Not full A* pathfinding — more like "smart straight line with structure avoidance."

**Leave No Trace:**
- All scorched earth patches store the original block state before modification
- When the storm ends, **all patches are restored** to their original state
- The world returns to normal — no permanent modification

**Pre-flight Validation (Ocean Check):**
- Before the elemental appears (Phase 0), the system validates that the path to the nearest Resonator doesn't cross an ocean
- Rivers, lakes, and small bodies of water are acceptable — oceans are not
- If no valid Resonator is reachable, the elemental simply doesn't appear during this storm
- A "water cap" rule (~50 blocks of continuous water) defines the ocean threshold

### If the player doesn't reach the Resonator before the storm ends
- Trail dissipates (blocks restored via Leave No Trace)
- No punishment, no lost progress
- Next thunder storm: Phase 0 resets — elemental appears again, same sequence
- Player gets unlimited attempts

---

## Phase 1.5: Attunement

### Trigger
- Player reaches the Resonator during a thunder storm
- Player **steps on one of the structure's plates**

### Behavior
- The 30-second Attunement Ritual begins (per `StormseekerAttunementService.java`):
  - **Spool Up (5s):** Plate activates, visuals and audio intensify
  - **Active Lock (15s):** Player is rooted, full intensity, uninterruptible
  - **Spool Down (5s):** Energy dissipates, ritual completes
- On completion: `AttunementCompleteEvent` fires, Leyline Sight unlocks
- Up to 6 plates can handle independent rituals simultaneously
- Stepping off during Spool Up interrupts the ritual (player can retry)

### Post-Attunement
- Player advances to Phase 2 (Dual Sigil Trials)
- The Flowing and Anchored Trials become available
- Leyline Sight (`PerceptionToggleHandler`) is unlocked but keybind not yet wired

---

## Worldgen Consideration

Resonator structures must be placed at a density that ensures:
- At least one Resonator is likely within ~500 blocks of any given player position
- The Resonator is reachable within the duration of a single thunder storm (accounting for terrain traversal, not straight-line distance)
- Storm duration and Resonator density are balanced so the Phase 0→1→1.5 sequence is completable in one storm without rushing

---

## Hytale API Status

### Confirmed Working
| Capability | Status |
|---|---|
| Weather detection (storm type) | ✅ Tested in-game |
| Player position tracking per tick | ✅ Tested in-game |
| Chat messages to player | ✅ Tested in-game |
| State persistence across restarts | ✅ Tested in-game |
| ECS system registration (tick loop) | ✅ Tested in-game |
| Command registration | ✅ Tested in-game |

### Needs Verification
| Capability | Status |
|---|---|
| Entity registration + spawn | Found in API, untested |
| DynamicLight on entities/structures | Found in API, untested |
| Block state read/write | Likely available (BlockModule exists), untested |
| Camera shake | Found in API, untested |
| Debug shapes (sphere, arrow, cone) | Found in API, untested |

### Not Found / Assumed
| Capability | Fallback |
|---|---|
| Particle spawning | DynamicLight or entity-attached emitters |
| Sound/audio playback | Unknown — may need asset pipeline |
| Post-processing shaders | Unknown — Leyline Sight visual TBD |
| Navigation / pathfinding | Simple lateral-nudge obstacle avoidance |
| Movement speed modification | Removed from design (original blocker) |

---

## Implementation Priorities

1. **API testing sprint:** Entity spawn + DynamicLight, block state read/write, camera shake
2. **Resonator storm glow:** DynamicLight on a structure during thunder storms (simplest standalone feature)
3. **Elemental entity:** Register air/electrical elemental, spawn near player during storms, basic hover behavior
4. **Elemental bolt + trail:** Streak behavior, scorched earth placement, structure avoidance, Leave No Trace cleanup
5. **Pre-flight validation:** Ocean check before elemental appears
6. **Integration:** Wire Phase 0 → 1 → 1.5 transitions into `StormseekerWiring`
7. **In-game testing:** Full sequence in live Hytale server

---

## Supersedes

This document supersedes the earlier `phase-0-1-redesign.md` which presented four options (A/B/C/D). The final design is a combination of Options A, B, and C as discussed.