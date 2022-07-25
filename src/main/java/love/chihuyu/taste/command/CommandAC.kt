package love.chihuyu.taste.command

import love.chihuyu.taste.Taste.Companion.plugin
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object CommandAC : Command("ac") {
    override fun onCommand(sender: CommandSender, label: String, args: Array<out String>) {
        if (sender !is Player) return
        val guiSelectAC = Bukkit.createInventory(null, 27, "Select Anti Cheat")
        guiSelectAC.setItem(10, ItemStack(Material.GRASS).apply { this.itemMeta = this.itemMeta.apply {
            this.displayName = "§7§lVanilla"
            this.lore = listOf(
                "Version: 1.12.2"
            )
        } })
        guiSelectAC.setItem(12, ItemStack(Material.QUARTZ).apply { this.itemMeta = this.itemMeta.apply {
            this.displayName = "§9§lNC§c§lP"
            this.lore = listOf(
                "Version: 3.16.0-RC-sMD5NET-b1134"
            )
        } })
        guiSelectAC.setItem(14, ItemStack(Material.BLAZE_POWDER).apply { this.itemMeta = this.itemMeta.apply {
            this.displayName = "§4§lVulcan"
            this.lore = listOf(
                "Version: 2.6.7-HOTFIX"
            )
        } })
        guiSelectAC.setItem(16, ItemStack(Material.ENDER_CHEST).apply { this.itemMeta = this.itemMeta.apply {
            this.displayName = "§b§lMatrix"
            this.lore = listOf(
                "Version: 6.7.1"
            )
        } })
        sender.openInventory(guiSelectAC)
        sender.playSound(sender.location, Sound.ENTITY_PLAYER_BURP, 1f, 1f)
    }

    override fun onTabComplete(sender: CommandSender, alias: String, args: Array<out String>): List<String>? {
        TODO("Not yet implemented")
    }
}