package me.nachtok0.yeeportals.subcommands;

import me.nachtok0.yeeportals.Portal;
import me.nachtok0.yeeportals.PortalManager;
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
		if (args.length < 3) {
			player.sendMessage(ChatColor.RED + "Uso: /portal connect <origen> <destino>");
			return false;
		}

		Portal from = manager.getPortal(args[1]);
		Portal to = manager.getPortal(args[2]);

		if (from == null || to == null) {
			player.sendMessage(ChatColor.RED + "Uno de los portales no existe.");
			return true;
		}

		from.targetName = to.name;
		manager.savePortals();
		player.sendMessage(ChatColor.GREEN + "Portales conectados: " + from.name + " -> " + to.name);
		return true;
	}

	@Override
	public String getUsage() {
		return "<origin> <destination>";
	}
}
