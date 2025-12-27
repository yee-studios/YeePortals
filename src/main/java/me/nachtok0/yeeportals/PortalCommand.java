package me.nachtok0.yeeportals;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PortalCommand implements CommandExecutor {

	PortalManager manager;

	public PortalCommand(PortalManager manager) {
		this.manager = manager;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Solo jugadores.");
			return true;
		}

		Player player = (Player) sender;
		if (!player.hasPermission("portal.admin")) {
			player.sendMessage(ChatColor.RED + "No tienes permiso.");
			return true;
		}

		if (args.length == 0) {
			player.sendMessage(ChatColor.GOLD + "--- Ayuda de YeePortals ---");
			player.sendMessage(ChatColor.YELLOW + "/portal region " + ChatColor.WHITE + "- Activa la varita de selección.");
			player.sendMessage(ChatColor.YELLOW + "/portal create <nombre> " + ChatColor.WHITE + "- Crea un portal en la selección.");
			player.sendMessage(ChatColor.YELLOW + "/portal connect <origen> <destino> " + ChatColor.WHITE + "- Conecta dos portales.");
			player.sendMessage(ChatColor.YELLOW + "/portal disconnect <nombre> " + ChatColor.WHITE + "- Quita el destino de un portal.");
			player.sendMessage(ChatColor.YELLOW + "/portal delete <nombre> " + ChatColor.WHITE + "- Borra un portal para siempre.");
			player.sendMessage(ChatColor.YELLOW + "/portal list " + ChatColor.WHITE + "- Ver lista de portales.");
			return true;
		}

		if (args[0].equalsIgnoreCase("region")) {
			Set<UUID> selectionMode = PortalPlayerData.getSelectionMode();
			if (selectionMode.contains(player.getUniqueId())) {
				selectionMode.remove(player.getUniqueId());
				player.sendMessage(ChatColor.YELLOW + "Modo selección desactivado.");
			} else {
				selectionMode.add(player.getUniqueId());
				player.sendMessage(ChatColor.GREEN + "Modo selección activado. Usa Clic Izq/Der para marcar esquinas.");
			}
			return true;
		}

		if (args[0].equalsIgnoreCase("create")) {
			if (args.length < 2) {
				player.sendMessage(ChatColor.RED + "Uso: /portal create <nombre>");
				return true;
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

		if (args[0].equalsIgnoreCase("connect")) {
			if (args.length < 3) {
				player.sendMessage(ChatColor.RED + "Uso: /portal connect <origen> <destino>");
				return true;
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

		List<Portal> portals = manager.getPortals();

		if (args[0].equalsIgnoreCase("list")) {
			if (portals.isEmpty()) {
				player.sendMessage(ChatColor.YELLOW + "No hay portales creados.");
				return true;
			}
			player.sendMessage(ChatColor.GOLD + "--- Lista de Portales (" + portals.size() + ") ---");
			for (Portal portal : portals) {
				String target = (portal.targetName == null || portal.targetName.isEmpty())
						? ChatColor.RED + "Sin destino"
						: ChatColor.GREEN + "-> " + portal.targetName;

				player.sendMessage(ChatColor.AQUA + portal.name + ChatColor.GRAY + " (" + portal.worldName + ") " + target);
			}
			return true;
		}

		if (args[0].equalsIgnoreCase("disconnect")) {
			if (args.length < 2) {
				player.sendMessage(ChatColor.RED + "Uso: /portal disconnect <nombre>");
				return true;
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

		if (args[0].equalsIgnoreCase("delete")) {
			if (args.length < 2) {
				player.sendMessage(ChatColor.RED + "Uso: /portal delete <nombre>");
				return true;
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

		return false;
	}
}
