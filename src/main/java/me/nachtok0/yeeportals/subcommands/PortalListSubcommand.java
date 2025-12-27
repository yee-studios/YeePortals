package me.nachtok0.yeeportals.subcommands;

import me.nachtok0.yeeportals.Portal;
import me.nachtok0.yeeportals.PortalManager;
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
			sender.sendMessage(ChatColor.YELLOW + "No hay portales creados.");
			return true;
		}
		sender.sendMessage(ChatColor.GOLD + "--- Lista de Portales (" + portals.size() + ") ---");
		for (Portal portal : portals) {
			String target = (portal.targetName == null || portal.targetName.isEmpty())
					? ChatColor.RED + "Sin destino"
					: ChatColor.GREEN + "-> " + portal.targetName;

			sender.sendMessage(ChatColor.AQUA + portal.name + ChatColor.GRAY + " (" + portal.worldName + ") " + target);
		}
		return true;
	}
}
