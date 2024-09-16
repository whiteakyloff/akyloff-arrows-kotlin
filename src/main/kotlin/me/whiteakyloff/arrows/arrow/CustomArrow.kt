package me.whiteakyloff.arrows.arrow

import me.whiteakyloff.arrows.arrow.abilities.*

import org.bukkit.inventory.ItemStack

import java.util.UUID

data class CustomArrow(
    val name: String,
    val uuid: UUID,
    val itemStack: ItemStack,
    val arrowData: Map<String, String>,
    val arrowAbilities: List<CustomArrowAbility>
) {
    enum class CustomArrowType(val ability: CustomArrowAbility)
    {
        AIM(AimAbility), EXPLODE(ExplodeAbility),
        TELEPORT(TeleportAbility), FREEZE(FreezeAbility);
    }
}

