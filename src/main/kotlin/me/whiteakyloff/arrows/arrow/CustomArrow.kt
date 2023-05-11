package me.whiteakyloff.arrows.arrow

import me.whiteakyloff.arrows.arrow.abilities.*

import org.bukkit.inventory.ItemStack

import java.util.UUID

data class CustomArrow(
    val name: String,
    val UUID: UUID,
    val itemStack: ItemStack,
    val arrowData: Map<String, String>,
    val arrowAbilities: List<CustomArrowAbility>
) {
    enum class CustomArrowType(private val ability: CustomArrowAbility)
    {
        AIM(AimAbility), EXPLODE(ExplodeAbility),
        TELEPORT(TeleportAbility), FREEZE(FreezeAbility);

        fun getAbility() : CustomArrowAbility = ability
    }
}

