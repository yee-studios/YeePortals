package me.nachtok0.yeeportals.subcommands;

import me.nachtok0.yeeportals.PortalManager;
import me.nachtok0.yeeportals.PortalMessages;
import me.nachtok0.yeeportals.PortalPlayerData;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PortalCreateSubcommand extends PortalPlayerSubcommand {

	PortalManager manager;

	public PortalCreateSubcommand(PortalManager manager) {
		super("create", "make");
		this.manager = manager;
	}

	public boolean execute(Player player, String[] args) {
		if (args.length < 2) return false;

		String name = args[1];
		if (manager.getPortal(name) != null) {
			PortalMessages.sendColorLocalized(player, ChatColor.RED, "portal_already_exists", name);
			return true;
		}
		Location[] sel = PortalPlayerData.getSelections().get(player.getUniqueId());
		if (sel == null || sel[0] == null || sel[1] == null) {
			PortalMessages.sendColorLocalized(player, ChatColor.RED, "no_region_selected", "&b/portal region");
			return true;
		}
		if (!sel[0].getWorld().equals(sel[1].getWorld())) {
			PortalMessages.sendColorLocalized(player, ChatColor.RED, "points_same_world");
			return true;
		}

		manager.createPortal(name, sel[0], sel[1]);
		PortalMessages.sendColorLocalized(player, ChatColor.GREEN, "created", name);
		return true;
	}

	@Override
	public String getUsage() {
		return "<portal_name>";
	}
}
