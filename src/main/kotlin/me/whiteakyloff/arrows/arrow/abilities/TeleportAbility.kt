package me.whiteakyloff.arrows.arrow.abilities

import me.whiteakyloff.arrows.arrow.CustomArrow
import me.whiteakyloff.arrows.arrow.CustomArrowAbility

import org.bukkit.entity.*

object TeleportAbility : CustomArrowAbility
{
    override fun apply(shooter: Player, arrow: Arrow?, customArrow: CustomArrow, hitEntity: Entity?) {
        if (shooter.world != arrow!!.world) {
            return
        }
        arrow.location.apply {
            this.yaw = shooter.location.yaw
            this.pitch = shooter.location.pitch
        }.run { shooter.teleport(this) }
    }
}