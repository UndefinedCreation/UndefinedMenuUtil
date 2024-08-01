package com.undefined.menuutil

import com.undefined.api.command.UndefinedCommand
import com.undefined.api.extension.string.translateColor
import com.undefined.menuutil.util.toItemBuilderString
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.hover.content.Text
import org.bukkit.Material
import org.bukkit.block.Barrel
import org.bukkit.block.Chest
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class MenuUtilCommand {

    init {
        UndefinedCommand("MenuUtil", permission = "undefined.menu.util.command")
            .addNumberSubCommand()
            .addNumberExecute {
                val player = sender as? Player ?: return@addNumberExecute false
                val block = player.getTargetBlockExact(5) ?: run {
                    player.sendMessage("<red>You're not looking at a block".translateColor())
                    return@addNumberExecute false
                }

                if (block.type != Material.CHEST && block.type != Material.BARREL) {
                    player.sendMessage("<red>This block is not a chest or barrel.".translateColor())
                    return@addNumberExecute false
                }

                var menuName = "Custom"

                val inventory: Inventory = when (block.type) {
                    Material.CHEST -> {
                        val chestState = block.state as Chest
                        chestState.customName?.let { menuName = it }
                        chestState.inventory
                    }
                    else -> {
                        val barrelState = block.state as Barrel
                        barrelState.customName?.let { menuName = it }
                        barrelState.inventory
                    }
                }

                if (inventory.size > number) {
                    player.sendMessage("<red>You are trying to create a bigger inventory than the block has.".translateColor())
                    return@addNumberExecute false
                }

                var codeString: String = "import com.undefined.api.builders.ItemBuilder\n" +
                        "import com.undefined.api.extension.string.translateColor\n" +
                        "import com.undefined.api.menu.normal.UndefinedMenu\n" +
                        "import org.bukkit.Material\n" +
                        "import org.bukkit.inventory.Inventory\n\n"

                codeString += "class ${menuName}Menu() : UndefinedMenu(\"$menuName\", ${number.toInt()}) {\n"
                codeString += "override fun generateInventory(): Inventory = createInventory {\n"
                val itemSlots: MutableMap<ItemStack, MutableList<Int>> = mutableMapOf()
                inventory.contents.forEachIndexed { index, itemStack ->
                    if (itemStack != null && index <= number) {
                        itemSlots.getOrPut(itemStack) { mutableListOf() }.add(index)
                    }
                }

                itemSlots.forEach {
                    codeString += when {
                        it.value.size == 1 -> "setItem(${it.value[0]}, ${it.key.toItemBuilderString()})"
                        else -> "setItems(${it.key.toItemBuilderString()}, ${it.value.joinToString(", ")})"
                    }
                    codeString += "\n"
                }

                codeString += "}\n}"

                val copyCommand = TextComponent("Click to copy code.").apply {
                    color = ChatColor.GREEN
                    clickEvent = ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, codeString)
                    hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, Text("Click to copy"))
                }
                player.spigot().sendMessage(copyCommand)

                return@addNumberExecute false
            }
    }

}