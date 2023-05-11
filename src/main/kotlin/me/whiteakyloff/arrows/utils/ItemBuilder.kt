package me.whiteakyloff.arrows.utils

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class ItemBuilder
{
    private val item: ItemStack

    constructor(item: ItemStack) {
        this.item = item
    }

    constructor(material: Material?) {
        item = ItemStack(material)
    }

    fun build(): ItemStack {
        return item
    }

    fun setTitle(title: String?): ItemBuilder {
        if (title == null) {
            return this
        }
        item.itemMeta.apply {
            this.displayName = title.translateColor()
        }.let { this.item.itemMeta = it }
        return this
    }

    fun setLore(lore: List<String>?): ItemBuilder {
        if (lore == null) {
            return this
        }
        item.itemMeta.apply {
            this.lore = lore.translateColor()
        }.let { this.item.itemMeta = it }
        return this
    }

    fun setAmount(amount: Int): ItemBuilder {
        item.amount = amount
        return this
    }

    fun addFlag(flag: ItemFlag?): ItemBuilder {
        item.itemMeta.apply {
            this.addItemFlags(flag)
        }.let { this.item.itemMeta = it }
        return this
    }

    fun addEnchant(enchantment: Enchantment?, level: Int): ItemBuilder {
        item.itemMeta.apply {
            this.addEnchant(enchantment, level, true)
        }.let { this.item.itemMeta = it }
        return this
    }

    fun setPotionColor(color: String): ItemBuilder {
        if (item.itemMeta !is PotionMeta) {
            return this
        }
        (item.itemMeta as PotionMeta).apply {
            this.color = color.toColor()
        }.let { this.item.itemMeta = it }
        return this
    }

    fun addPotionEffect(effect: PotionEffect?): ItemBuilder {
        if (item.itemMeta !is PotionMeta) {
            return this
        }
        (item.itemMeta as PotionMeta).apply {
            this.addCustomEffect(effect, true)
        }.let { this.item.itemMeta = it }
        return this
    }

    companion object {
        fun of(item: ItemStack): ItemBuilder {
            return ItemBuilder(item)
        }

        fun of(material: Material?): ItemBuilder {
            return ItemBuilder(material)
        }

        @Suppress("UNCHECKED_CAST")
        fun loadItemBuilder(section: HashMap<String, Any>): ItemBuilder {
            return ItemBuilder(Material.TIPPED_ARROW).apply {
                if (section.containsKey("title")) {
                    this.setTitle(section["title"].toString())
                }
                if (section.containsKey("lore")) {
                    this.setLore(section["lore"] as List<String>?)
                }
                if (section.containsKey("color")) {
                    this.setPotionColor(section["color"].toString())
                }
                this.setAmount(if (section.containsKey("amount")) section["amount"] as Int else 1)

                if (section.containsKey("flags")) {
                    (section["flags"] as List<String>).forEach {
                        this.addFlag(ItemFlag.valueOf(it.uppercase()))
                    }
                }
                if (section.containsKey("enchants")) {
                    for (enchant in (section["enchants"] as List<String>)) {
                        enchant.split(":").dropLastWhile { it.isEmpty() }.toTypedArray().let {
                            this.addEnchant(Enchantment.getByName(it[0].uppercase()), it[1].toInt())
                        }
                    }
                }
                if (section.containsKey("effects")) {
                    for (effect in (section["effects"] as List<String>)) {
                        effect.split(":").dropLastWhile { it.isEmpty() }.toTypedArray().let {
                            val potionType = PotionEffectType.getByName(it[0].uppercase())
                            val level = it[1].toInt() - 1
                            val duration = it[2].toInt() * 20

                            this.addPotionEffect(PotionEffect(potionType, duration, level))
                        }
                    }
                }
            }
        }
    }
}