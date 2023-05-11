package me.whiteakyloff.arrows.utils.packets

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer

@Suppress("UNUSED")
class WrapperPlayServerEntityDestroy : AbstractPacket(PacketContainer(TYPE), TYPE)
{
    init {
        handle.modifier.writeDefaults()
    }

    val count: Int
        get() = handle.integerArrays.read(0).size
    var entityIDs: IntArray
        get() = handle.integerArrays.read(0)
        set(value) {
            handle.integerArrays.write(0, value)
        }

    companion object {
        val TYPE: PacketType = PacketType.Play.Server.ENTITY_DESTROY
    }
}