package me.nachtok0.yeeportals.subcommands;

import me.nachtok0.yeeportals.Portal;
import me.nachtok0.yeeportals.PortalManager;
import me.nachtok0.yeeportals.PortalMessages;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PortalDisconnectSubcommand extends PortalPlayerSubcommand {
	PortalManager manager;

	public PortalDisconnectSubcommand(PortalManager manager) {
		super("disconnect");
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

		portal.targetName = null;
		manager.savePortals();
		PortalMessages.sendColorLocalized(player, ChatColor.GREEN, "disconnected", name);
		return true;
	}

	@Override
	public String getUsage() {
		return "<portal_name>";
	}
}
