import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath("gradle.plugin.com.github.johnrengelman:shadow:7.1.2")
    }
}

plugins {
    kotlin("jvm") version "1.7.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "io.classomatic"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.keycloak:keycloak-server-spi:20.0.1")
    compileOnly("org.keycloak:keycloak-server-spi-private:20.0.1")
    compileOnly("org.keycloak:keycloak-core:20.0.1")
    compileOnly("org.keycloak:keycloak-services:20.0.1")

    testRuntimeOnly(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}