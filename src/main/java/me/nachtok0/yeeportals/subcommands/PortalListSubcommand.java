package me.nachtok0.yeeportals.subcommands;

import me.nachtok0.yeeportals.Portal;
import me.nachtok0.yeeportals.PortalManager;
import me.nachtok0.yeeportals.PortalMessages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class PortalListSubcommand extends PortalSubcommand {
	PortalManager manager;

	public PortalListSubcommand(PortalManager manager) {
		super("list");
		this.manager = manager;
	}

	@Override
	public String getUsage() {
		return "";
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		List<Portal> portals = manager.getPortals();
		if (portals.isEmpty()) {
			PortalMessages.sendColorLocalized(sender, ChatColor.RED, "list.no_portals");
			return true;
		}
		PortalMessages.send(sender, "&6--- %s --- (%s)", PortalMessages.get("list.portals"), portals.size());
		for (Portal portal : portals) {
			String target = (portal.targetName == null || portal.targetName.isEmpty())
					? ChatColor.RED + PortalMessages.get("no_destination")
					: ChatColor.GREEN + "-> " + portal.targetName;

			PortalMessages.send(sender, "&b%s &7(%s) %s", portal.name, portal.worldName, target);
		}
		return true;
	}
}
