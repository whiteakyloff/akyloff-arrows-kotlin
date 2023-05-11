package me.whiteakyloff.arrows.arrow.abilities

import me.whiteakyloff.arrows.arrow.CustomArrow
import me.whiteakyloff.arrows.arrow.CustomArrowAbility
import me.whiteakyloff.arrows.arrow.CustomSphere

import org.bukkit.entity.*

import com.okkero.skedule.schedule

object FreezeAbility : CustomArrowAbility
{
    override fun apply(shooter: Player, arrow: Arrow?, customArrow: CustomArrow, hitEntity: Entity?) {
        if (hitEntity !is Player) {
            return
        }
        val sphere = CustomSphere(hitEntity)
        val sphereData = customArrow.arrowData["freeze"]!!.split(", ")
            .map { it.toInt() }.toTypedArray()

        sphere.create()
        this.javaPlugin.schedule { sphere.spawn(); this.waitFor(sphereData[0] * 20L); sphere.explode(); this.waitFor(sphereData[1] * 20L); sphere.destroy() }
    }
}