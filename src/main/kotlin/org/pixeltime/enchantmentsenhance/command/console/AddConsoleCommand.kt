/*
 *     Copyright (C) 2017-Present HealPotion
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.pixeltime.enchantmentsenhance.command.console

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.pixeltime.enchantmentsenhance.command.SubConsoleCommand
import org.pixeltime.enchantmentsenhance.event.blacksmith.Backpack
import org.pixeltime.enchantmentsenhance.manager.MM
import org.pixeltime.enchantmentsenhance.manager.SettingsManager
import org.pixeltime.enchantmentsenhance.util.Util

class AddConsoleCommand : SubConsoleCommand() {

    override fun onCommand(sender: CommandSender, args: Array<String>) {
        // /enhance add <player> <stone id> <amounts>
        // /cmd subcommand args[0] args[1] args[2]
        if (args.size == 3) {
            val p = Bukkit.getServer().getPlayer(args[0])
            val stoneType: Int
            val level: Int

            if (p == null) {
                Util.sendMessage(SettingsManager.lang.getString(
                        "Config.playerNotFound"), sender)
                return
            }

            try {
                stoneType = Integer.parseInt(args[1])
                level = Integer.parseInt(args[2])
            } catch (e: Exception) {
                Util.sendMessage(SettingsManager.lang.getString(
                        "Config.invalidNumber"), sender)
                return
            }

            if (stoneType != -1 && level != -1 && stoneType <= MM.stoneTypes.size) {
                Backpack.addLevel(p, stoneType, level)
                Util.sendMessage(SettingsManager.lang.getString(
                        "Add.successful").replace("%player%", p.name).replace(
                        "%number%", Integer.toString(level)).replace("%stone%",
                        SettingsManager.lang.getString("Item.$stoneType")), sender)
            } else {
                Util.sendMessage(SettingsManager.lang.getString(
                        "Config.invalidNumber"), sender)
            }
        } else {
            Util.sendMessage(SettingsManager.lang.getString(
                    "Config.invalidCommand"), sender)
        }
    }


    override fun name(): String {
        return "add"
    }


    override fun usage(): String {
        return "/enhance add { player } { stone } { amount }"
    }


    override fun aliases(): Array<String> {
        return arrayOf("add", "give", "tianjia", "tj")
    }

}
