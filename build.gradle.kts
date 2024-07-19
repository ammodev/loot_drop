plugins {
    java
    `java-library`
    `maven-publish`
    `kotlin-dsl`

    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenLocal()
    mavenCentral()

    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.oraxen.com/releases")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly(libs.paper.api)
    compileOnly(libs.commandapi)
    compileOnly(libs.placeholder.api)
    compileOnly(libs.oraxen)
    compileOnly(libs.betterhud)

    implementation(libs.inventory.framework)
    implementation(libs.configurate.core)
    implementation(libs.configurate.yml)
}

group = "de.castcrafter"
description = "loot_drop"
version = "1.20.4-1.1.0-SNAPSHOT"

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}
tasks {
    shadowJar {
        relocate("com.github.stefvanschie.inventoryframework", "de.castcrafter.lootdrop.inventoryframework")
        relocate("org.spongepowered", "de.castcrafter.lootdrop.spongepowered")
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.compilerArgs.add("-parameters")
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }

    withSourcesJar()
    withJavadocJar()
}
