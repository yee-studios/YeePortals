package me.nachtok0.yeeportals.subcommands;

import me.nachtok0.yeeportals.Portal;
import me.nachtok0.yeeportals.PortalManager;
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
		if (args.length < 2) {
			player.sendMessage(ChatColor.RED + "Uso: /portal disconnect <nombre>");
			return false;
		}
		Portal portal = manager.getPortal(args[1]);
		if (portal == null) {
			player.sendMessage(ChatColor.RED + "El portal no existe.");
			return true;
		}

		portal.targetName = null;
		manager.savePortals();
		player.sendMessage(ChatColor.GREEN + "El portal '" + portal.name + "' ha sido desconectado.");
		return true;
	}

	@Override
	public String getUsage() {
		return "<portal_name>";
	}
}
