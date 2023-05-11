package me.whiteakyloff.arrows.commands.messages

import me.whiteakyloff.arrows.AkyloffArrows
import me.whiteakyloff.arrows.utils.translateColor

import org.bukkit.command.CommandSender
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.plugin.java.JavaPlugin

import java.util.function.UnaryOperator

class MessagesFactory(private val javaPlugin: AkyloffArrows)
{
    var prefix: String?

    var messages: Map<String, Message>

    init {
        val section = this.javaPlugin.config.getConfigurationSection("messages")

        this.prefix = section.getString("prefix")
        this.messages = this.fromConfigurationToMap(section)
    }

    private fun fromConfigurationToMap(section: ConfigurationSection) : Map<String, Message> {
        val data = mutableMapOf<String, Message>()

        section.getKeys(false).forEach {
            if (section.isConfigurationSection(it)) {
                fromConfigurationToMap(section.getConfigurationSection(it)).onEach { (keyMessage, message) ->
                    data["$it.$keyMessage"] = message
                }
            } else {
                var message: Message? = null

                if (section.isString(it)) {
                    message = Message(section.getString(it))
                }
                else if (section.isList(it)) {
                    message = Message(section.getStringList(it))
                }
                if (message != null) { data[it] = message }
            }
        }
        return data
    }

    data class Message(val value: Any)
    {
        fun sendMessage(sender: CommandSender, replace: UnaryOperator<String>) {
            this.sendMessage(sender, usePrefix = getPrefix() != null, replace)
        }

        @Suppress("UNCHECKED_CAST")
        private fun sendMessage(sender: CommandSender, usePrefix: Boolean, replace: UnaryOperator<String>) {
            when (value) {
                is String -> {
                    var message = replace.apply(value.toString())

                    if (usePrefix && getPrefix() != null) {
                        message = getPrefix() + message
                    }
                    sender.sendMessage(message.translateColor())
                }
                is List<*> -> {
                    (value as List<String>).forEach {
                        var message = replace.apply(it)

                        if (usePrefix && getPrefix() != null) {
                            message = getPrefix() + message
                        }
                        sender.sendMessage(message.translateColor())
                    }
                }
                else -> { sender.sendMessage("unsupported object type") }
            }
        }

        companion object {
            fun getPrefix(): String? = JavaPlugin.getPlugin(AkyloffArrows::class.java)
                .messagesFactory.prefix

            fun getByName(key: String): Message? = JavaPlugin.getPlugin(AkyloffArrows::class.java)
                .messagesFactory.messages[key]
        }
    }
}