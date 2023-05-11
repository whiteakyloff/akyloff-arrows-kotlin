package me.whiteakyloff.arrows.utils.packets

import org.bukkit.Bukkit
import org.bukkit.entity.Player

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer

import java.util.Objects
import java.lang.reflect.InvocationTargetException

@Suppress("UNUSED")
abstract class AbstractPacket protected constructor(handle: PacketContainer?, type: PacketType)
{
    var handle: PacketContainer

    init {
        requireNotNull(handle) { "Packet handle cannot be NULL." }
        require(Objects.equals(handle.type, type)) { handle.handle.toString() + " is not a packet of type " + type }

        this.handle = handle
    }

    fun sendPacket(receiver: Player?) {
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(receiver, handle)
        } catch (e: InvocationTargetException) {
            throw RuntimeException("Cannot send packet.", e)
        }
    }

    fun receivePacket(sender: Player?) {
        try {
            ProtocolLibrary.getProtocolManager().recieveClientPacket(sender, handle)
        } catch (e: Exception) {
            throw RuntimeException("Cannot receive packet.", e)
        }
    }

    fun broadcastPacket() {
        val protocolManager = ProtocolLibrary.getProtocolManager()
        try {
            Bukkit.getOnlinePlayers().forEach { protocolManager.sendServerPacket(it, handle)}
        } catch (e: InvocationTargetException) {
            throw RuntimeException("Cannot send packet.", e)
        }
    }
}