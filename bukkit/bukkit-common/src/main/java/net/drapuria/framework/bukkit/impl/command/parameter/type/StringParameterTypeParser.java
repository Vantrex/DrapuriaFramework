package net.drapuria.framework.bukkit.impl.command.parameter.type;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public class StringParameterTypeParser extends CommandTypeParameter<String>{
    @Override
    public String parseNonPlayer(CommandSender sender, String value) {
        return value;
    }

    @Override
    public List<String> tabComplete(Player player, Set<String> flags, String source) {
        return null;
    }

    @Override
    public String parse(Player sender, String value) {
        return value;
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }

}
