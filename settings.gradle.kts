rootProject.name = "io.github.legendaryforge.hytale"

plugins {
    id("dev.scaffoldit") version "0.2.+"
}

hytale {
    usePatchline("release")
    useVersion("latest")

    repositories {
        // Any external repositories
    }

    dependencies {
        // Any external dependency
    }

    manifest {
        Group = "io.github.legendaryforge"
        Name = "LegendaryHytale"
        Main = "io.github.legendaryforge.hytale.LegendaryHytalePlugin"
    }
}
