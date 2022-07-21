package love.chihuyu.taste.command

import love.chihuyu.taste.Taste.Companion.plugin
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter


/**
 * ベースとなるコマンド
 *
 * @property name コマンド名
 */
abstract class Command(private val name: String) : CommandExecutor, TabCompleter {
    /**
     * コマンドを登録する
     */
    fun register() {
        val command = plugin.getCommand(name) ?: throw IllegalStateException()
        command.executor = this
        command.tabCompleter = this
    }

    override fun onCommand(sender: CommandSender, command: org.bukkit.command.Command, label: String, args: Array<out String>): Boolean {
        onCommand(sender, label, args)
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: org.bukkit.command.Command, alias: String, args: Array<out String>): List<String>? {
        return onTabComplete(sender, alias, args)
    }

    /**
     * コマンドの実行処理
     */
    abstract fun onCommand(sender: CommandSender, label: String, args: Array<out String>)

    /**
     * コマンド引数の補完処理
     */
    abstract fun onTabComplete(sender: CommandSender, alias: String, args: Array<out String>): List<String>?
}
