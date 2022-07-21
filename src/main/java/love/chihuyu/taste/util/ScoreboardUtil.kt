package love.chihuyu.taste.util

import love.chihuyu.taste.Taste.Companion.plugin
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot

object ScoreboardUtil {

    fun update(player: Player) {
        val objString = player.uniqueId.toString().split('-')[0]
        player.scoreboard.getObjective(objString).unregister()
        val obj = player.scoreboard.registerNewObjective(objString, "")
        obj.displaySlot = DisplaySlot.SIDEBAR
        obj.displayName = "   §c§lHiro Tasting§r   "
        listOf(
            "AC: ${player.scoreboard.getEntryTeam(player.name).prefix.replace("[", "").replace("]", "")}",
            "",
            "Hunger: ${if (HungerUtil.isAntiHunger(player)) "§cfalse" else "§atrue"}"
        ).forEachIndexed { index, s ->
            obj.getScore(s).score = -index
        }

        //apply ac prefix from other scoreboard to my scoreboard
        plugin.server.onlinePlayers.forEach {
            player.scoreboard.getTeam(it.scoreboard.getEntryTeam(it.name).name).addEntry(it.name)
            it.scoreboard.getTeam(player.scoreboard.getEntryTeam(player.name).name).addEntry(player.name)
        }
    }
}