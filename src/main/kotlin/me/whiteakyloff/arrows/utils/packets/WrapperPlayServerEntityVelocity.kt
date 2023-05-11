package me.whiteakyloff.arrows.utils.packets

import org.bukkit.World
import org.bukkit.entity.Entity

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer

@Suppress("UNUSED")
class WrapperPlayServerEntityVelocity : AbstractPacket(PacketContainer(TYPE), TYPE)
{
    init {
        handle.modifier.writeDefaults()
    }

    var entityID: Int
        get() = handle.integers.read(0)
        set(value) {
            handle.integers.write(0, value)
        }
    var velocityX: Double
        get() = handle.integers.read(1) / 8000.0
        set(value) {
            handle.integers.write(1, (value * 8000.0).toInt())
        }
    var velocityY: Double
        get() = handle.integers.read(2) / 8000.0
        set(value) {
            handle.integers.write(2, (value * 8000.0).toInt())
        }
    var velocityZ: Double
        get() = handle.integers.read(3) / 8000.0
        set(value) {
            handle.integers.write(3, (value * 8000.0).toInt())
        }

    fun getEntity(world: World?): Entity {
        return handle.getEntityModifier(world!!).read(0)
    }

    companion object {
        val TYPE: PacketType = PacketType.Play.Server.ENTITY_VELOCITY
    }
}