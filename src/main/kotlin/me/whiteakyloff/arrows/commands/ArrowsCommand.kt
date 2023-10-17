package me.whiteakyloff.arrows.commands

import me.whiteakyloff.arrows.AkyloffArrows
import me.whiteakyloff.arrows.utils.sendMessage

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ArrowsCommand(private val javaPlugin: AkyloffArrows) : CommandExecutor
{
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!sender.hasPermission("arrows.give")) {
            sender.sendMessage(key = "no-permission") { it }
            return true
        }
        if (args.size != 2) {
            sender.sendMessage(key = "usage") { it }
            return true
        }
        val player = Bukkit.getPlayer(args[0])
        val customArrow = this.javaPlugin.arrowsManager.getArrow(args[1])

        if (player == null || customArrow == null) {
            sender.sendMessage(key = "null-args.${if (player == null) "player" else "custom-arrow"}") { it }
            return true
        }
        this.giveItem(player, customArrow.itemStack)
        sender.sendMessage(key = "successfully") { it.replace("{player}", player.name).replace("{custom-arrow}", customArrow.name) }
        return true
    }

    private fun giveItem(player: Player, itemStack: ItemStack) {
        if (player.inventory.firstEmpty() == -1) {
            player.world.dropItem(player.location, itemStack)
        } else {
            player.inventory.addItem(itemStack)
        }
    }
}