package love.chihuyu.taste

import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
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
        val loc = Location(event.player.world, 0.0, 5.0, 0.0, 0f, 90f)
        val player = event.player
        player.teleport(loc)
        player.bedSpawnLocation = loc
        player.health = 20.0
        player.foodLevel = 20
    }
}