package me.nachtok0.yeeportals.subcommands;

import me.nachtok0.yeeportals.PortalManager;
import me.nachtok0.yeeportals.PortalPlayerData;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PortalCreateSubcommand extends PortalPlayerSubcommand {

	PortalManager manager;

	public PortalCreateSubcommand(PortalManager manager) {
		super("create");
		this.manager = manager;
	}

	public boolean execute(Player player, String[] args) {
		if (args.length < 2) {
			player.sendMessage(ChatColor.RED + "Uso: /portal create <nombre>");
			return false;
		}
		String name = args[1];
		if (manager.getPortal(name) != null) {
			player.sendMessage(ChatColor.RED + "Ya existe un portal con ese nombre.");
			return true;
		}
		Location[] sel = PortalPlayerData.getSelections().get(player.getUniqueId());
		if (sel == null || sel[0] == null || sel[1] == null) {
			player.sendMessage(ChatColor.RED + "Debes seleccionar una región primero (/portal region).");
			return true;
		}
		if (!sel[0].getWorld().equals(sel[1].getWorld())) {
			player.sendMessage(ChatColor.RED + "Los puntos deben estar en el mismo mundo.");
			return true;
		}

		manager.createPortal(name, sel[0], sel[1]);
		player.sendMessage(ChatColor.GREEN + "Portal '" + name + "' creado!");
		return true;
	}

	@Override
	public String getUsage() {
		return "<portal_name>";
	}
}
