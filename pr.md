## Summary

Initial LegendaryHytale plugin setup - proof of concept for Hytale integration.

## Changes

- Created Hytale plugin project using official template
- Configured for LegendaryForge organization
- Implemented PlayerConnectEvent listener
- Plugin successfully builds and is ready for testing

## Rationale

Establishes the integration layer between Legendary quest system and Hytale's game engine:
- Proves we can run code in Hytale
- Validates event system integration
- Creates foundation for Phase 1 (Storm Attunement) implementation

## Verification

- ./gradlew clean build (passes)
- Plugin JAR created successfully
