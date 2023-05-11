plugins {
    id("org.jetbrains.kotlin.jvm") version "1.8.21"
    id("xyz.jpenilla.run-paper") version "2.1.0"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

version = "2.0"
group = "me.whiteakyloff"

repositories {
    mavenLocal()
    mavenCentral()

    maven(url = "https://repo.dmulloy2.net/repository/public/")
    maven(url = "https://repo.papermc.io/repository/maven-public/")
    maven(url = "https://jitpack.io")
    maven(url = "https://oss.sonatype.org/content/groups/public/")
    maven(url = "https://repo.codemc.org/repository/maven-public/")
}

dependencies {
    implementation("de.tr7zw:item-nbt-api:2.11.2")
    implementation("com.github.okkero:skedule:1.2.6")

    compileOnly("com.comphenix.protocol:ProtocolLib:4.8.0")
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:1.5.30")
    compileOnly("com.destroystokyo.paper:paper-api:1.12.2-R0.1-SNAPSHOT")
}

java {
    toolchain {
        this.languageVersion.set(JavaLanguageVersion.of(8))
    }
}

tasks {
    jar {
        enabled = false
        this.dependsOn(shadowJar)
    }
    shadowJar {
        val group = "me.whiteakyloff.arrows"

        this.relocate("com.okkero.skedule", "$group.utils.dependencies.skedule")
        this.relocate("de.tr7zw.changeme.nbtapi", "$group.utils.dependencies.nbtapi")
        this.relocate("de.tr7zw.annotations", "$group.utils.dependencies.nbtapi.annotations")
    }
    runServer {
        minecraftVersion("1.12.2")
    }
}