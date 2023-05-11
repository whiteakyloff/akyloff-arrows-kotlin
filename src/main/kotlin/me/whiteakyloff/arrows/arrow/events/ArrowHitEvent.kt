package me.whiteakyloff.arrows.arrow.events

import me.whiteakyloff.arrows.arrow.CustomArrow

import org.bukkit.event.*
import org.bukkit.entity.*

data class ArrowHitEvent(val shooter: Player, val arrow: Arrow, val customArrow: CustomArrow, val hitEntity: Entity?) : Event()
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
