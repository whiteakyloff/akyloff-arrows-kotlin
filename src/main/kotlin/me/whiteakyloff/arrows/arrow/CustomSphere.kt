package me.whiteakyloff.arrows.arrow

import me.whiteakyloff.arrows.ArrowsManager
import me.whiteakyloff.arrows.utils.packets.*

import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.util.Vector

import com.comphenix.protocol.wrappers.WrappedDataWatcher
import com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject

import java.util.UUID

import kotlin.math.*
import kotlin.random.Random
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class CustomSphere(private val hitPlayer: Player)
{
    private val players = HashSet<Player>()

    private val blocks = HashMap<Int, Vector>()

    fun create() {
        val radius = 1.5
        val invRadius = 1.0 / radius
        val ceilRadius = ceil(radius).toInt()
        val vectorPos = Vector(hitPlayer.location.x, hitPlayer.location.y + 1.0, hitPlayer.location.z)

        for (x in 0..ceilRadius) {
            val xn = x * invRadius
            val distanceX = this.lengthSq(xn, 0.0, 0.0)

            if (distanceX > 1.0) {
                break
            }
            for (y in 0..ceilRadius) {
                val yn = y * invRadius
                val distanceY = this.lengthSq(xn, yn, 0.0)

                if (distanceY > 1.0) {
                    break
                }
                for (z in 0..ceilRadius) {
                    val zn = z * invRadius
                    val distanceZ = this.lengthSq(xn, yn, zn)

                    if (distanceZ > 1.0) {
                        break
                    }
                    if (this.lengthSq(xn + invRadius, yn, zn) > 1.0 || this.lengthSq(xn, yn + invRadius, zn) > 1.0 || this.lengthSq(xn, yn, zn + invRadius) > 1.0) {
                        val vectors = listOf(
                            Vector(x.toDouble(), y.toDouble(), z.toDouble()),
                            Vector(-x.toDouble(), y.toDouble(), z.toDouble()),
                            Vector(x.toDouble(), -y.toDouble(), z.toDouble()),
                            Vector(x.toDouble(), y.toDouble(), -z.toDouble()),
                            Vector(-x.toDouble(), -y.toDouble(), z.toDouble()),
                            Vector(x.toDouble(), -y.toDouble(), -z.toDouble()),
                            Vector(-x.toDouble(), y.toDouble(), -z.toDouble()),
                            Vector(-x.toDouble(), -y.toDouble(), -z.toDouble())
                        )
                        this.blocks.putAll(vectors.map { it.add(vectorPos.clone()) }.associateBy { Random.nextInt() })
                    }
                }
            }
        }
    }

    fun spawn() {
        this.hitPlayer.world.getNearbyEntities(this.hitPlayer.location, 40.0, 40.0, 40.0)
            .filterIsInstance<Player>().forEach { spawn(it) }
    }

    private fun spawn(player: Player) {
        this.players.add(player)
        ArrowsManager.sphereContainer[this.hitPlayer] = this

        for ((entityID, vector) in this.blocks) {
            val dataWatcher = WrappedDataWatcher().apply {
                this.setObject(WrappedDataWatcherObject(0, BYTE_SERIALIZER), 0.toByte())
                this.setObject(WrappedDataWatcherObject(1, INTEGER_SERIALIZER), 300)
                this.setObject(WrappedDataWatcherObject(2, STRING_SERIALIZER), "")
                this.setObject(WrappedDataWatcherObject(3, BOOLEAN_SERIALIZER), false)
                this.setObject(WrappedDataWatcherObject(4, BOOLEAN_SERIALIZER), false)
                this.setObject(WrappedDataWatcherObject(5, BOOLEAN_SERIALIZER), true)
            }

            WrapperPlayServerSpawnEntity().apply {
                this.entityID = entityID
                this.uniqueID = UUID.randomUUID()

                this.type = 70; this.objectData = 79

                this.x = vector.x; this.y = vector.y; this.z = vector.z
            }.sendPacket(player)
            WrapperPlayServerEntityMetadata().apply {
                this.entityID = entityID

                this.metadata = dataWatcher.deepClone().watchableObjects
            }.sendPacket(player)
            player.playSound(player.location, Sound.BLOCK_GLASS_BREAK, 1.0f, 1.0f)
        }
    }


    fun explode() {
        val packets = blocks.map { (entityID, vector) ->
            val direction = this.getDirection(vector, hitPlayer.location.toVector())?.normalize()?.multiply(80) ?: Vector()

            WrapperPlayServerEntityVelocity().apply {
                this.entityID = entityID
                this.velocityX = direction.x / 250.0; this.velocityY = direction.y / 250.0; this.velocityZ = direction.z / 250.0
            }
        }
        this.players.forEach { player ->
            packets.forEach { packet -> packet.sendPacket(player) }
            player.playSound(player.location, Sound.BLOCK_GLASS_BREAK, 1.0F, 0.0F)
        }
    }

    fun destroy() {
        val destroyPacket = WrapperPlayServerEntityDestroy().apply {
            this.entityIDs = blocks.keys.toIntArray()
        }
        this.players.forEach {
            destroyPacket.sendPacket(it)
            it.playSound(it.location, Sound.BLOCK_GLASS_BREAK, 1.0F, 2.0F)
        }
        this.players.clear()
        ArrowsManager.sphereContainer.remove(hitPlayer, this)
    }

    private fun lengthSq(x: Double, y: Double, z: Double): Double = x * x + y * y + z * z

    private fun toYaw(vector: Vector): Double {
        val x = vector.x
        val z = vector.z

        return Math.toDegrees(atan2(-x, z)) % 360
    }

    private fun toPitch(vector: Vector): Double {
        val x = vector.x
        val y = vector.y
        val z = vector.z

        val xz = sqrt(x.pow(2) + z.pow(2))

        return Math.toDegrees(atan2(-y, xz))
    }

    private fun getDirection(vector: Vector, other: Vector): Vector? {
        return vector.clone().subtract(other).apply {
            val rotX = toYaw(this)
            val rotY = toPitch(this)
            val cosY = cos(Math.toRadians(rotY))
            val sinY = sin(Math.toRadians(rotY))
            val cosX = cos(Math.toRadians(rotX))
            val sinX = sin(Math.toRadians(rotX))

            this.y = -sinY; this.x = -cosY * sinX; this.z = cosY * cosX
        }
    }

    companion object {
        val BYTE_SERIALIZER: WrappedDataWatcher.Serializer = WrappedDataWatcher.Registry.get(java.lang.Byte::class.java)
        val STRING_SERIALIZER: WrappedDataWatcher.Serializer = WrappedDataWatcher.Registry.get(java.lang.String::class.java)
        val INTEGER_SERIALIZER: WrappedDataWatcher.Serializer = WrappedDataWatcher.Registry.get(java.lang.Integer::class.java)
        val BOOLEAN_SERIALIZER: WrappedDataWatcher.Serializer = WrappedDataWatcher.Registry.get(java.lang.Boolean::class.java)
    }
}