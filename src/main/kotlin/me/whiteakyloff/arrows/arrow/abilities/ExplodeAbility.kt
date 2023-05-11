package me.whiteakyloff.arrows.arrow.abilities

import me.whiteakyloff.arrows.arrow.CustomArrow
import me.whiteakyloff.arrows.arrow.CustomArrowAbility

import org.bukkit.entity.*

object ExplodeAbility: CustomArrowAbility
{
    override fun apply(shooter: Player, arrow: Arrow?, customArrow: CustomArrow, hitEntity: Entity?) {
        customArrow.arrowData["explode"]!!.split(", ").let {
            arrow!!.location.createExplosion(it[0].toFloat(), it[1].toBoolean(), it[2].toBoolean())
        }
    }
}