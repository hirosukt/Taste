package love.chihuyu.taste

import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.event.server.ServerListPingEvent
import org.bukkit.plugin.java.JavaPlugin

class Taste : JavaPlugin(), Listener {
    override fun onEnable() {
        // Plugin startup logic
        this.server.pluginManager.registerEvents(this, this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val loc = Location(event.player.world, 0.5, 5.0, 0.5, 0f, 0f)
        val player = event.player
        player.teleport(loc)
        player.health = 20.0
        player.foodLevel = 20
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
        event.motd = "          §c§l§nHiro's Hack Tasting§7 ❘ §6§l§n1.12.2\n§r                 §9§lNoCheat§c§lPlus§7, §6§lAAC"
    }
}