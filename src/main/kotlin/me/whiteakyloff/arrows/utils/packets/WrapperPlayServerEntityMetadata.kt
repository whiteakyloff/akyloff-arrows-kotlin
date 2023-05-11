package me.whiteakyloff.arrows.utils.packets

import org.bukkit.World
import org.bukkit.entity.Entity

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.WrappedWatchableObject

@Suppress("UNUSED")
class WrapperPlayServerEntityMetadata : AbstractPacket(PacketContainer(TYPE), TYPE)
{
    init {
        handle.modifier.writeDefaults()
    }

    var entityID: Int
        get() = handle.integers.read(0)
        set(value) {
            handle.integers.write(0, value)
        }
    var metadata: List<WrappedWatchableObject>?
        get() = handle.watchableCollectionModifier.read(0)
        set(value) {
            handle.watchableCollectionModifier.write(0, value)
        }

    fun getEntity(world: World?): Entity {
        return handle.getEntityModifier(world!!).read(0)
    }

    companion object {
        val TYPE: PacketType = PacketType.Play.Server.ENTITY_METADATA
    }
}