package love.chihuyu.taste.util

import org.bukkit.entity.Player

object HungerUtil {

    val antiHunger = mutableListOf<Player>()

    fun toggleHunger(player: Player) {
        if (player in antiHunger) antiHunger.remove(player) else antiHunger.add(player)
    }

    fun isAntiHunger(player: Player): Boolean {
        return player in antiHunger
    }
}