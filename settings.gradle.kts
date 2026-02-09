rootProject.name = "io.github.legendaryforge.hytale"

plugins {
    id("dev.scaffoldit") version "0.2.+"
}

hytale {
    usePatchline("release")
    useVersion("latest")

    repositories {
        mavenLocal()
        // Any external repositories
    }

    dependencies {
        implementation("io.github.legendaryforge:LegendaryCore:0.0.0-SNAPSHOT")
        implementation("io.github.legendaryforge:Legendary:0.0.0-SNAPSHOT")
        // Any external dependency
    }

    manifest {
        Group = "io.github.legendaryforge"
        Name = "LegendaryHytale"
        Main = "io.github.legendaryforge.hytale.LegendaryHytalePlugin"
    }
}
