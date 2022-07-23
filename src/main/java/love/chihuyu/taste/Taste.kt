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
            ScoreboardUtil.prepareNodeTeams(player.scoreboard)
            it.data().add(Node.builder("nocheatplus.shortcut.bypass").build())
        }

        ScoreboardUtil.update(player)
    }

    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        val loc = Location(event.player.world, 0.5, 5.0, 0.5, 0f, 0f)
        val player = event.player
        player.teleport(loc)
    }

    @EventHandler
    fun onPing(event: ServerListPingEvent) {
        event.maxPlayers = 0
        event.motd =
            "          §c§l§nHiro's Hack Tasting§7 ❘ §6§l§n1.12.2\n§r                 §9§lNoCheat§c§lPlus§7, §6§lAAC"
    }

    @EventHandler()
    fun onClickInventory(event: InventoryClickEvent) {
        val item = event.currentItem
        val player = event.whoClicked as Player
        if (event.clickedInventory?.title != "Select Anti Cheat" || item == null) return
        val provider = Bukkit.getServicesManager().getRegistration(LuckPerms::class.java) ?: return
        val api = provider.provider

        event.isCancelled = true

        fun switchACNode(antiCheats: AntiCheats) {
            player.scoreboard.getTeam(antiCheats.teamName).addEntry(player.name)
            api.userManager.modifyUser(player.uniqueId) {
                when (antiCheats) {
                    AntiCheats.VANILLA -> it.data().add(Node.builder("nocheatplus.shortcut.bypass").build())
                    AntiCheats.NCP -> it.data().remove(Node.builder("nocheatplus.shortcut.bypass").build())
                }
            }
        }

        when (item.type) {
            Material.GRASS -> switchACNode(AntiCheats.VANILLA)
            Material.QUARTZ -> switchACNode(AntiCheats.NCP)
            else -> {}
        }.also {
            ScoreboardUtil.update(player)
            ScoreboardUtil.applyTeamDisplayToOther(player)
        }
    }

    @EventHandler
    fun onFood(event: FoodLevelChangeEvent) {
        val player = event.entity as Player
        event.isCancelled = HungerUtil.isAntiHunger(player)
        if (HungerUtil.isAntiHunger(player)) event.foodLevel = 20
    }
}