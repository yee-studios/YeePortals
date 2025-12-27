package me.nachtok0.yeeportals.subcommands;

import me.nachtok0.yeeportals.PortalMessages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class PortalPlayerSubcommand extends PortalSubcommand {
	public PortalPlayerSubcommand(String name) {
		super(name);
	}

	public abstract boolean execute(Player player, String[] args);

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if ((sender instanceof Player player)) {
			execute(player, args);
			return true;
		}
		PortalMessages.send(sender, "player_only");
		return true;
	}
}
