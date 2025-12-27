package me.nachtok0.yeeportals.subcommands;

import me.nachtok0.yeeportals.Portal;
import me.nachtok0.yeeportals.PortalManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PortalDeleteSubcommand extends PortalPlayerSubcommand {
	PortalManager manager;

	public PortalDeleteSubcommand(PortalManager manager) {
		super("delete");
		this.manager = manager;
	}

	@Override
	public boolean execute(Player player, String[] args) {
		if (args.length < 2) {
			player.sendMessage(ChatColor.RED + "Uso: /portal delete <nombre>");
			return false;
		}
		String name = args[1];
		Portal portal = manager.getPortal(name);

		if (portal == null) {
			player.sendMessage(ChatColor.RED + "El portal no existe.");
			return true;
		}

		manager.deletePortal(portal);

		player.sendMessage(ChatColor.GREEN + "Portal '" + name + "' eliminado correctamente.");
		return true;
	}

	@Override
	public String getUsage() {
		return "<portal_name>";
	}
}
