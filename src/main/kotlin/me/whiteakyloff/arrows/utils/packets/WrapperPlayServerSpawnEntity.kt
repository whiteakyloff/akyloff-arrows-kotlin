package me.whiteakyloff.arrows.utils.packets

import org.bukkit.World
import org.bukkit.entity.Entity

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.injector.PacketConstructor

import java.util.*

@Suppress("UNUSED")
class WrapperPlayServerSpawnEntity : AbstractPacket(PacketContainer(TYPE), TYPE)
{
    init {
        handle.modifier.writeDefaults()
    }

    var entityID: Int
        get() = handle.integers.read(0)
        set(value) {
            handle.integers.write(0, value)
        }
    var uniqueID: UUID
        get() = handle.uuiDs.read(0)
        set(value) {
            handle.uuiDs.write(0, value)
        }
    var x: Double
        get() = handle.doubles.read(0)
        set(value) {
            handle.doubles.write(0, value)
        }
    var y: Double
        get() = handle.doubles.read(1)
        set(value) {
            handle.doubles.write(1, value)
        }
    var z: Double
        get() = handle.doubles.read(2)
        set(value) {
            handle.doubles.write(2, value)
        }
    var optionalSpeedX: Double
        get() = handle.integers.read(1) / 8000.0
        set(value) {
            handle.integers.write(1, (value * 8000.0).toInt())
        }
    var optionalSpeedY: Double
        get() = handle.integers.read(2) / 8000.0
        set(value) {
            handle.integers.write(2, (value * 8000.0).toInt())
        }
    var optionalSpeedZ: Double
        get() = handle.integers.read(3) / 8000.0
        set(value) {
            handle.integers.write(3, (value * 8000.0).toInt())
        }
    var pitch: Float
        get() = handle.integers.read(4) * 360.0f / 256.0f
        set(value) {
            handle.integers.write(4, (value * 256.0f / 360.0f).toInt())
        }
    var yaw: Float
        get() = handle.integers.read(5) * 360.0f / 256.0f
        set(value) {
            handle.integers.write(5, (value * 256.0f / 360.0f).toInt())
        }
    var type: Int
        get() = handle.integers.read(6)
        set(value) {
            handle.integers.write(6, value)
        }
    var objectData: Int
        get() = handle.integers.read(7)
        set(value) {
            handle.integers.write(7, value)
        }

    fun getEntity(world: World?): Entity {
        return handle.getEntityModifier(world!!).read(0)
    }

    companion object {
        val TYPE: PacketType = PacketType.Play.Server.SPAWN_ENTITY

        private var entityConstructor: PacketConstructor? = null

        private fun fromEntity(entity: Entity, type: Int, objectData: Int): PacketContainer {
            if (entityConstructor == null) {
                entityConstructor = ProtocolLibrary.getProtocolManager().createPacketConstructor(TYPE, entity, type, objectData)
            }
            return entityConstructor!!.createPacket(entity, type, objectData)
        }
    }
}