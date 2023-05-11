package me.whiteakyloff.arrows.arrow.events

import me.whiteakyloff.arrows.arrow.CustomArrow

import org.bukkit.event.*
import org.bukkit.entity.Arrow

data class ArrowFlyingEvent(val arrow: Arrow, val customArrow: CustomArrow) : Event()
{
    override fun getHandlers(): HandlerList {
        return getHandlerList()
    }

    companion object {
        private val handlers = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlers
        }
    }
}