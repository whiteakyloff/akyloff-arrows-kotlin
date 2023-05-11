package me.whiteakyloff.arrows

import me.whiteakyloff.arrows.commands.ArrowsCommand
import me.whiteakyloff.arrows.commands.messages.MessagesFactory

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class AkyloffArrows : JavaPlugin()
{
    lateinit var arrowsManager: ArrowsManager

    lateinit var messagesFactory: MessagesFactory

    override fun onLoad() {
        this.saveDefaultConfig()
    }

    override fun onEnable() {
        this.arrowsManager = ArrowsManager(this)
        this.messagesFactory = MessagesFactory(this)
        this.getCommand("arrows").executor = ArrowsCommand(this)

        Bukkit.getPluginManager().registerEvents(ArrowsListener(this), this)
    }

    override fun onDisable() {
        this.arrowsManager.disableManager()
    }
}