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
        obj.displaySlot = DisplaySlot.SIDEBAR
        obj.displayName = "   §c§lHiro Tasting§r   "
        prepareNodeTeams(scoreboard)
        listOf(
            "AC: ${player.scoreboard.getEntryTeam(player.name) ?: player.scoreboard.getTeam("vanilla").also { it.addEntry(player.name) }.prefix.replace("[", "").replace("]", "")}",
            "",
            "Hunger: ${if (HungerUtil.isAntiHunger(player)) "§cfalse" else "§atrue"}"
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
        return scoreboard.registerNewTeam(antiCheats.teamName).apply {
            this.prefix = when (antiCheats) {
                AntiCheats.VANILLA -> "§7[§aVNL§7]§f "
                AntiCheats.NCP -> "§7[§9NC§cP§7]§f "
            }
        }
    }

    fun prepareNodeTeams(scoreboard: Scoreboard) {
        AntiCheats.values().forEach { ac -> registerNodeTeam(scoreboard, ac) }
    }

    private fun fixNodeTeams(scoreboard: Scoreboard) {
        AntiCheats.values().forEach { ac -> scoreboard.getTeam(ac.teamName) ?: registerNodeTeam(scoreboard, ac) }
    }
}