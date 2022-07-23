package love.chihuyu.taste.util

import love.chihuyu.taste.AntiCheats
import love.chihuyu.taste.Taste.Companion.plugin
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team

object ScoreboardUtil {

    fun update(player: Player) {
        val scoreboard = plugin.server.scoreboardManager.newScoreboard
        val objString = player.uniqueId.toString().split('-')[0]
        try { scoreboard.getObjective(objString).unregister() } catch (_: Exception) { }
        val obj = scoreboard.registerNewObjective(objString, "")
        val isHunger = !HungerUtil.isAntiHunger(player)
        obj.displaySlot = DisplaySlot.SIDEBAR
        obj.displayName = "   §c§lHiro Tasting§r   "
        prepareNodeTeams(scoreboard)
        listOf(
            "AC: ${AntiCheats.values().first { antiCheats -> antiCheats.teamName == (player.scoreboard.getEntryTeam(player.name) ?: player.scoreboard.getTeam("vanilla").also { it.addEntry(player.name) }).name }.acName}",
            "",
            "${if (isHunger) "§a" else "§c"}Hunger: $isHunger"
        ).forEachIndexed { index, s ->
            obj.getScore(s).score = -index
        }

        scoreboard.getTeam(player.scoreboard.getEntryTeam(player.name).name).addEntry(player.name)
        player.scoreboard = scoreboard
    }

    fun applyTeamDisplayToOther(player: Player) {
        plugin.server.onlinePlayers.filterNot { it == player }.forEach {
            player.scoreboard.getTeam(it.scoreboard.getEntryTeam(it.name).name).addEntry(it.name)
            it.scoreboard.getTeam(player.scoreboard.getEntryTeam(player.name).name).addEntry(player.name)
        }
    }

    private fun registerNodeTeam(scoreboard: Scoreboard, antiCheats: AntiCheats): Team {
        return scoreboard.registerNewTeam(antiCheats.teamName).apply { this.prefix = "§7[${antiCheats.prefix}§7]§f "}
    }

    private fun prepareNodeTeams(scoreboard: Scoreboard) {
        AntiCheats.values().forEach { ac -> registerNodeTeam(scoreboard, ac) }
    }

    fun fixNodeTeams(scoreboard: Scoreboard) {
        AntiCheats.values().forEach { ac -> scoreboard.getTeam(ac.teamName) ?: registerNodeTeam(scoreboard, ac) }
    }
}