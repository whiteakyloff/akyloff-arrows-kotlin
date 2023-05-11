package me.whiteakyloff.arrows.arrow.abilities

import me.whiteakyloff.arrows.ArrowsManager
import me.whiteakyloff.arrows.arrow.CustomArrow
import me.whiteakyloff.arrows.arrow.CustomArrowAbility

import org.bukkit.entity.*
import org.bukkit.util.Vector

object TeleportAbility : CustomArrowAbility
{
    override fun apply(shooter: Player, arrow: Arrow?, customArrow: CustomArrow, hitEntity: Entity?) {
        when (customArrow.arrowData["teleport"]!!.uppercase()) {
            "AT_POINT" -> {
                if (shooter.world != arrow!!.world) {
                    return
                }
                arrow.location.apply {
                    this.yaw = shooter.location.yaw
                    this.pitch = shooter.location.pitch
                }.run { shooter.teleport(this) }
            }
            "BY_ARROW" -> {
                if (!shooter.isSneaking) {
                    if (ArrowsManager.teleportContainer.containsValue(arrow)) {
                        return
                    }
                    shooter.velocity = arrow!!.location.subtract(shooter.location).toVector().normalize().multiply(1.2)
                } else {
                    if (ArrowsManager.teleportContainer.containsValue(arrow)) {
                        return
                    }
                    with(shooter) {
                        this.velocity = Vector(0, 0, 0)
                        ArrowsManager.teleportContainer.put(this, arrow!!)
                    }
                }
            }
        }
    }
}