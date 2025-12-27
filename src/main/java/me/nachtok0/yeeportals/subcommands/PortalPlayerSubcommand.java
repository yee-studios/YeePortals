package me.nachtok0.yeeportals.subcommands;

import me.nachtok0.yeeportals.PortalMessages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class PortalPlayerSubcommand extends PortalSubcommand {
	public PortalPlayerSubcommand(String name, String... aliases) {
		super(name, aliases);
	}

	public abstract boolean execute(Player player, String[] args);

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if ((sender instanceof Player player)) {
			execute(player, args);
			return true;
		}
		PortalMessages.sendColorLocalized(sender, ChatColor.RED, "player_only");
		return true;
	}
}
