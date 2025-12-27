package me.nachtok0.yeeportals.subcommands;

import me.nachtok0.yeeportals.Portal;
import me.nachtok0.yeeportals.PortalManager;
import me.nachtok0.yeeportals.PortalMessages;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PortalConnectSubcommand extends PortalPlayerSubcommand {

	PortalManager manager;

	public PortalConnectSubcommand(PortalManager manager) {
		super("connect");
		this.manager = manager;
	}

	@Override
	public boolean execute(Player player, String[] args) {
		if (args.length < 3) return false;

		Portal from = manager.getPortal(args[1]);
		Portal to = manager.getPortal(args[2]);

		if (from == null) {
			PortalMessages.sendColorLocalized(player, ChatColor.RED, "portal_unknown_name", args[1]);
			return true;
		}

		if (to == null) {
			PortalMessages.sendColorLocalized(player, ChatColor.RED, "portal_unknown_name", args[2]);
			return true;
		}

		from.targetName = to.name;
		manager.savePortals();
		PortalMessages.sendLocalized(player, "connected",
				String.format("&b%s &e-> &b%s", from.name, to.name));
		return true;
	}

	@Override
	public String getUsage() {
		return "<origin> <destination>";
	}
}
