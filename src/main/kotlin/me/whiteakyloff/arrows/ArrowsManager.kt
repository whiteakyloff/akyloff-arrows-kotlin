package me.whiteakyloff.arrows

import de.tr7zw.changeme.nbtapi.NBTItem

import me.whiteakyloff.arrows.arrow.CustomArrow
import me.whiteakyloff.arrows.arrow.CustomSphere
import me.whiteakyloff.arrows.utils.ItemBuilder
import me.whiteakyloff.arrows.utils.*

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

import java.util.UUID

import kotlin.collections.HashMap

@Suppress("UNCHECKED_CAST")
class ArrowsManager(private val javaPlugin: AkyloffArrows)
{
    private val arrows: MutableList<CustomArrow> = mutableListOf()

    private val fileConfiguration: FileConfiguration = this.javaPlugin.getFile("arrows-data.yml")

    init {
        for (section in javaPlugin.config.getList("arrows") as List<Map<String, Any>>) {
            val arrowName = section["id"].toString()
            val arrowUUID = if (fileConfiguration.getString("arrows-uuids.$arrowName") != null) {
                UUID.fromString(fileConfiguration.getString("arrows-uuids.$arrowName"))
            } else {
                UUID.randomUUID()
            }
            val itemBuilder = ItemBuilder.loadItemBuilder(section["item"] as HashMap<String, Any>)
            val itemStack = NBTItem(itemBuilder.build()).apply { this.setUUID("arrow-uuid", arrowUUID) }.item

            val arrowAbilities = section["arrow-type"].toString().split(", ")
                .map { CustomArrow.CustomArrowType.valueOf(it.uppercase()).getAbility() }
                .toMutableList()
            val arrowData = section.entries
                .filter { it.key.startsWith("arrow-settings") }
                .associateBy({ it.key.replace("arrow-settings-", "") }, { it.value.toString() })
            this.arrows.add(CustomArrow(arrowName, arrowUUID, itemStack, arrowData, arrowAbilities))
        }
    }

    private fun getArrow(uuid: UUID) : CustomArrow? {
        return this.arrows.firstOrNull { it.uuid == uuid }
    }

    fun getArrow(name: String) : CustomArrow? {
        return this.arrows.firstOrNull { it.name.equals(name, true) }
    }

    fun getArrow(itemStack: ItemStack) : CustomArrow? {
        return NBTItem(itemStack).let {
            if (!it.hasTag("arrow-uuid")) {
                null
            } else {
                this.getArrow(it.getUUID("arrow-uuid"))
            }
        }
    }

    fun disableManager() {
        this.arrows.forEach { this.fileConfiguration.set("arrows-uuids.${it.name}", it.uuid.toString()) }
            .also { this.javaPlugin.saveFile(this.fileConfiguration, "arrows-data.yml") }
    }

    companion object {
        val sphereContainer = mutableMapOf<Player?, CustomSphere>()

        val teleportContainer= mutableMapOf<Player?, Arrow>()
    }
}