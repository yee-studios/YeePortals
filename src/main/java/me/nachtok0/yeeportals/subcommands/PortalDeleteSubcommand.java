package me.nachtok0.yeeportals.subcommands;

import me.nachtok0.yeeportals.Portal;
import me.nachtok0.yeeportals.PortalManager;
import me.nachtok0.yeeportals.PortalMessages;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PortalDeleteSubcommand extends PortalPlayerSubcommand {
	PortalManager manager;

	public PortalDeleteSubcommand(PortalManager manager) {
		super("delete", "remove", "del", "rem", "rm");
		this.manager = manager;
	}

	@Override
	public boolean execute(Player player, String[] args) {
		if (args.length < 2) return false;

		String name = args[1];
		Portal portal = manager.getPortal(name);

		if (portal == null) {
			PortalMessages.sendColorLocalized(player, ChatColor.RED, "portal_unknown_name", name);
			return true;
		}

		manager.deletePortal(portal);
		PortalMessages.sendColorLocalized(player, ChatColor.GREEN, "deleted", name);
		return true;
	}

	@Override
	public String getUsage() {
		return "<portal_name>";
	}
}
