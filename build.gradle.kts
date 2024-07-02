/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    `java-library`
    `maven-publish`

    id("io.github.goooler.shadow") version "8.1.8"
}

repositories {
    mavenLocal()

    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.maven.apache.org/maven2/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.oraxen.com/releases")
}

dependencies {
    compileOnly(libs.paper.api)
    compileOnly(libs.commandapi)
    implementation(libs.inventory.framework)
    compileOnly(libs.placeholder.api)
    compileOnly(libs.oraxen)
}

group = "de.castcrafter"
version = "1.0-SNAPSHOT"

tasks {
    shadowJar {
        relocate("com.github.stefvanschie.inventoryframework", "de.castcrafter.lootdrop.inventoryframework")
    }
}
description = "loot_drop"

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}
