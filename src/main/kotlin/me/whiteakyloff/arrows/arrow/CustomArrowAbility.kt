package me.whiteakyloff.arrows.arrow

import me.whiteakyloff.arrows.AkyloffArrows
import me.whiteakyloff.arrows.arrow.events.ArrowHitEvent

import org.bukkit.entity.Arrow
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

interface CustomArrowAbility
{
    fun apply(event: ArrowHitEvent) {
        this.apply(event.shooter, event.arrow, event.customArrow, event.hitEntity)
    }

    fun apply(shooter: Player, arrow: Arrow?, customArrow: CustomArrow, hitEntity: Entity?)

    val javaPlugin: AkyloffArrows
        get() = JavaPlugin.getPlugin(AkyloffArrows::class.java)
}