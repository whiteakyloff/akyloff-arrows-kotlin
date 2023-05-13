package me.whiteakyloff.arrows.utils

import me.whiteakyloff.arrows.AkyloffArrows
import me.whiteakyloff.arrows.commands.messages.Message

import org.bukkit.ChatColor
import org.bukkit.Color
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.metadata.Metadatable
import org.bukkit.plugin.java.JavaPlugin

import java.io.File
import java.io.IOException
import java.util.function.UnaryOperator

fun CommandSender.sendMessage(key: String, replace: UnaryOperator<String>) {
    Message.getByName(key)!!.sendMessage(this, replace)
}

fun JavaPlugin.saveFile(config: FileConfiguration, fileName: String?): FileConfiguration {
    try {
        config.save(fileName?.let { File(this.dataFolder, it) })
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return config
}

fun JavaPlugin.getFile(fileName: String?): FileConfiguration {
    val file = fileName?.let { File(this.dataFolder, it) }

    if (this.getResource(fileName) == null) {
        return saveFile(YamlConfiguration.loadConfiguration(file), fileName)
    }
    if (!file!!.exists()) {
        this.saveResource(fileName, false)
    }
    return YamlConfiguration.loadConfiguration(file)
}

fun String.translateColor(): String = ChatColor.translateAlternateColorCodes('&', this)

fun List<String>.translateColor(): List<String> = this.map { it.translateColor() }.toCollection(mutableListOf())

fun String.toColor(): Color = Color.fromRGB(
    this.substring(1, 3).toInt(16), this.substring(3, 5).toInt(16), this.substring(5, 7).toInt(16)
)

fun Metadatable.setMetadata(key: String, value: Any) = this.setMetadata(key, FixedMetadataValue(JavaPlugin.getPlugin(AkyloffArrows::class.java), value))

