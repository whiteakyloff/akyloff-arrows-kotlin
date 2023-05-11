package me.whiteakyloff.arrows

import me.whiteakyloff.arrows.utils.setMetadata
import me.whiteakyloff.arrows.arrow.abilities.AimAbility
import me.whiteakyloff.arrows.arrow.abilities.TeleportAbility
import me.whiteakyloff.arrows.arrow.events.ArrowFlyingEvent
import me.whiteakyloff.arrows.arrow.events.ArrowHitEvent

import org.bukkit.Bukkit
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scheduler.BukkitRunnable

import kotlin.random.Random

class ArrowsListener(private val javaPlugin: AkyloffArrows) : Listener
{
    @EventHandler
    fun onLoadArrow(event: EntityShootBowEvent) {
        if (event.entity !is Player) {
            return
        }
        val customArrow = this.javaPlugin.arrowsManager.getArrow(event.arrowItem) ?: return

        event.projectile.apply {
            this.setMetadata("custom-arrow", Random.nextInt())
        }
        Bukkit.getPluginManager().callEvent(ArrowFlyingEvent(event.projectile as Arrow, customArrow))
    }

    @EventHandler
    fun onFlyingArrow(event: ArrowFlyingEvent) {
        object : BukkitRunnable() {
            override fun run() {
                val arrow = event.arrow
                val customArrow = event.customArrow
                val shooter = arrow.shooter as Player

                customArrow.arrowAbilities.asSequence()
                    .filter {
                        it == AimAbility || (it == TeleportAbility && customArrow.arrowData["teleport"].equals("BY_ARROW", ignoreCase = true))
                    }
                    .forEach { it.apply(shooter, arrow, customArrow, null) }
                if (arrow.hasMetadata("hit-arrow")) {
                    val hitEvent = arrow.getMetadata("hit-arrow")[0].value() as ProjectileHitEvent

                    Bukkit.getPluginManager().callEvent(ArrowHitEvent(shooter, arrow, customArrow, hitEvent.hitEntity))
                    this.cancel()
                }
            }
        }.runTaskTimer(this.javaPlugin, 1L, 1L)
    }

    @EventHandler
    fun onFallArrow(event: ProjectileHitEvent) {
        if (event.entity is Arrow && event.entity.hasMetadata("custom-arrow")) {
            event.entity.setMetadata("hit-arrow", event)
        }
    }

    @EventHandler
    fun onHitArrow(event: ArrowHitEvent) {
        val arrow = event.arrow
        val shooter = event.shooter

        event.customArrow.arrowAbilities
            .filterNot { it == AimAbility }.forEach { it.apply(event) }
        if (ArrowsManager.teleportContainer.containsValue(arrow)) {
            ArrowsManager.teleportContainer.remove(shooter, arrow)
        }
        arrow.remove()
    }

    @EventHandler
    fun onFreezeQuit(event: PlayerQuitEvent) {
        ArrowsManager.sphereContainer.remove(event.player)
    }

    @EventHandler(ignoreCancelled = true)
    fun onFreezeMoving(event: PlayerMoveEvent) {
        if (ArrowsManager.sphereContainer.containsKey(event.player)) {
            event.player.teleport(event.from)
        }
    }
}