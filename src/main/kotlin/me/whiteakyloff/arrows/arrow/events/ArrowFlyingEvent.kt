package me.whiteakyloff.arrows.arrow.events

import me.whiteakyloff.arrows.arrow.CustomArrow

import org.bukkit.event.*
import org.bukkit.entity.Arrow

data class ArrowFlyingEvent(
    val arrow: Arrow, val customArrow: CustomArrow
) : Event() {
    override fun getHandlers(): HandlerList = handlerList

    companion object { val handlerList = HandlerList() }
}