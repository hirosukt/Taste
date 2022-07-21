package love.chihuyu.taste.command

import love.chihuyu.taste.util.HungerUtil
import love.chihuyu.taste.util.ScoreboardUtil
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object CommandHunger : Command("hunger") {
    override fun onCommand(sender: CommandSender, label: String, args: Array<out String>) {
        if (sender !is Player) return
        HungerUtil.toggleHunger(sender)
        sender.foodLevel = 20

        ScoreboardUtil.update(sender)
    }

    override fun onTabComplete(sender: CommandSender, alias: String, args: Array<out String>): List<String>? {
        TODO("Not yet implemented")
    }
}