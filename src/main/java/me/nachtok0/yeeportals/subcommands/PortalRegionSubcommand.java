package me.nachtok0.yeeportals.subcommands;

import me.nachtok0.yeeportals.PortalPlayerData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public class PortalRegionSubcommand extends PortalPlayerSubcommand {
	public PortalRegionSubcommand() {
		super("region");
	}

	@Override
	public boolean execute(Player player, String[] args) {
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

	@Override
	public String getUsage() {
		return "";
	}
}
