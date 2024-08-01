package com.undefined.menuutil.util

import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

fun ItemStack.toItemBuilderString(): String {
    val itemMeta = itemMeta
    var string = "ItemBuilder(Material.${type.name})"
    if (itemMeta == null) return "$string.build()"
    if (itemMeta.displayName.isNotEmpty()) string += ".setName(\"${itemMeta.displayName}\".translateColor())"
    itemMeta.lore?.let { string += loreString(it) }
    if (amount > 1) string += ".setAmount($amount)"
    if (itemMeta.hasCustomModelData()) string += ".setCustomModelData(${itemMeta.customModelData})"
    string += enchantments(enchantments)
    if (itemMeta.isUnbreakable) string += ".setUnbreakable(true)"
    string += flags(itemMeta.itemFlags)
    return "$string.build()"
}

private fun flags(flags: Set<ItemFlag>): String {
    if (flags.isEmpty()) return ""
    return flags.joinToString("") { ".addFlags(ItemFlag.${it.name})" }
}

private fun loreString(list: List<String>): String {
    if (list.isEmpty()) return ""
    return list.joinToString("") { ".addLine(\"${it}\".translateColor())" }
}

private fun enchantments(map: Map<Enchantment, Int>): String {
    if (map.isEmpty()) return ""
    return map.map { "Enchantment.${it.key.name}, ${it.value}" }.joinToString("") { ".addEnchant($it)" }
}