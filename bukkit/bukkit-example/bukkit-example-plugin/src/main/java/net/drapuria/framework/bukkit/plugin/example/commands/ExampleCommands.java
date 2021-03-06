/*
 * Copyright (c) 2022. Drapuria
 */

package net.drapuria.framework.bukkit.plugin.example.commands;

import net.drapuria.framework.bukkit.impl.command.DrapuriaCommand;
import net.drapuria.framework.command.annotation.Command;
import net.drapuria.framework.command.annotation.DefaultCommand;
import net.drapuria.framework.command.annotation.SubCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@Command(names = "example", useSubCommandsOnly = false)
public class ExampleCommands extends DrapuriaCommand {

    private final Plugin plugin;

    public ExampleCommands(Plugin plugin) {
        super();
        this.plugin = plugin;
    }

    @SubCommand(names = "hello", parameters = "{Spieler}")
    public void helloCommand(Player player, Player target) {
        player.sendMessage("Spieler: " + target.getName());
    }

}
