package love.chihuyu.taste

import love.chihuyu.taste.command.CommandAC
import love.chihuyu.taste.command.CommandHunger
import love.chihuyu.taste.util.HungerUtil
import love.chihuyu.taste.util.ScoreboardUtil
import net.luckperms.api.LuckPerms
import net.luckperms.api.node.Node
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.event.server.ServerListPingEvent
import org.bukkit.plugin.java.JavaPlugin

class Taste : JavaPlugin(), Listener {

    companion object {
        lateinit var plugin: JavaPlugin
    }

    init {
        plugin = this
    }

    override fun onEnable() {
        // Plugin startup logic
        this.server.pluginManager.registerEvents(this, this)
        CommandAC.register()
        CommandHunger.register()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val loc = Location(event.player.world, 0.5, 5.0, 0.5, 0f, 0f)
        val player = event.player
        val provider = Bukkit.getServicesManager().getRegistration(LuckPerms::class.java) ?: return
        val api = provider.provider
        player.teleport(loc)
        player.health = 20.0
        player.foodLevel = 20

        api.userManager.modifyUser(player.uniqueId) {
            ScoreboardUtil.fixNodeTeams(player.scoreboard)
            it.data().add(Node.builder("nocheatplus.shortcut.bypass").build())
            it.data().add(Node.builder("vulcan.bypass.*").build())
            it.data().add(Node.builder("matrix.bypass").build())
        }

        player.scoreboard.getTeam("vanilla").addEntry(player.name)
        ScoreboardUtil.update(player)
        ScoreboardUtil.applyTeamDisplayToOther(player)
    }

    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        val loc = Location(event.player.world, 0.5, 5.0, 0.5, 0f, 0f)
        val player = event.player
        player.teleport(loc)
    }

    @EventHandler()
    fun onClickInventory(event: InventoryClickEvent) {
        val item = event.currentItem
        val player = event.whoClicked as Player
        if (event.clickedInventory?.title != "Select Anti Cheat" || item == null) return
        val provider = Bukkit.getServicesManager().getRegistration(LuckPerms::class.java) ?: return
        val api = provider.provider

        event.isCancelled = true

        fun switchACNode(antiCheats: AntiCheats): Boolean {
            player.scoreboard.getTeam(antiCheats.teamName).addEntry(player.name)
            api.userManager.modifyUser(player.uniqueId) {
                when (antiCheats) {
                    AntiCheats.VANILLA -> {
                        it.data().add(Node.builder("nocheatplus.shortcut.bypass").build())
                        it.data().add(Node.builder("vulcan.bypass.*").build())
                        it.data().add(Node.builder("matrix.bypass").build())
                    }
                    AntiCheats.NCP -> {
                        it.data().remove(Node.builder("nocheatplus.shortcut.bypass").build())
                        it.data().add(Node.builder("vulcan.bypass.*").build())
                        it.data().add(Node.builder("matrix.bypass").build())
                    }
                    AntiCheats.VULCAN -> {
                        it.data().add(Node.builder("nocheatplus.shortcut.bypass").build())
                        it.data().remove(Node.builder("vulcan.bypass.*").build())
                        it.data().add(Node.builder("matrix.bypass").build())
                    }
                    AntiCheats.MATRIX -> {
                        it.data().add(Node.builder("nocheatplus.shortcut.bypass").build())
                        it.data().add(Node.builder("vulcan.bypass.*").build())
                        it.data().remove(Node.builder("matrix.bypass").build())
                    }
                }
            }

            return true
        }

        val successed = when (item.type) {
            Material.GRASS -> switchACNode(AntiCheats.VANILLA)
            Material.QUARTZ -> switchACNode(AntiCheats.NCP)
            Material.BLAZE_POWDER -> switchACNode(AntiCheats.VULCAN)
            Material.ENDER_CHEST -> switchACNode(AntiCheats.MATRIX)
            else -> false
        }

        if (successed) {
            ScoreboardUtil.update(player)
            ScoreboardUtil.applyTeamDisplayToOther(player)
            player.closeInventory()
            player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
        }
    }

    @EventHandler
    fun onFood(event: FoodLevelChangeEvent) {
        val player = event.entity as Player
        event.isCancelled = HungerUtil.isAntiHunger(player)
        if (HungerUtil.isAntiHunger(player)) event.foodLevel = 20
    }
}