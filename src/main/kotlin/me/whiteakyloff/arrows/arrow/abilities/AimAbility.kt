package me.whiteakyloff.arrows.arrow.abilities

import me.whiteakyloff.arrows.arrow.CustomArrow
import me.whiteakyloff.arrows.arrow.CustomArrowAbility

import org.bukkit.entity.*
import org.bukkit.util.Vector

object AimAbility : CustomArrowAbility
{
    override fun apply(shooter: Player, arrow: Arrow?, customArrow: CustomArrow, hitEntity: Entity?) {
        val aimRadius = customArrow.arrowData["aim"]!!.toDouble()

        val target = shooter.getNearbyEntities(aimRadius, aimRadius, aimRadius)
            .filter { it != shooter && !it.isDead && it.type.isAlive && shooter.hasLineOfSight(it) }
            .minByOrNull { it.location.distance(shooter.location) } ?: return
        val vector = target.location.clone().add(0.0, 0.5, 0.0).subtract(arrow!!.location).toVector()

        arrow.velocity = arrow.velocity.clone().normalize().add(vector.clone().normalize()).normalize().add(Vector(0.0, 0.0, 0.0))
    }
}