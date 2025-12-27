package me.nachtok0.yeeportals.subcommands;

import me.nachtok0.yeeportals.PortalMessages;
import me.nachtok0.yeeportals.PortalPlayerData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public class PortalRegionSubcommand extends PortalPlayerSubcommand {
	public PortalRegionSubcommand() {
		super("region", "points");
	}

	@Override
	public boolean execute(Player player, String[] args) {
		Set<UUID> selectionMode = PortalPlayerData.getSelectionMode();
		if (selectionMode.contains(player.getUniqueId())) {
			selectionMode.remove(player.getUniqueId());
			PortalMessages.sendColorLocalized(player, ChatColor.YELLOW, "region.disabled");
		} else {
			selectionMode.add(player.getUniqueId());
			PortalMessages.sendColorLocalized(player, ChatColor.GREEN, "region.enabled");
		}
		return true;
	}

	@Override
	public String getUsage() {
		return "";
	}
}
