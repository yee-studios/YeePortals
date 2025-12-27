package me.nachtok0.yeeportals;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PortalEvents implements Listener {
	YeePortals plugin;

	public PortalEvents(YeePortals plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (!PortalPlayerData.getSelectionMode().contains(player.getUniqueId())) return;

		if (event.getAction() != Action.LEFT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

		event.setCancelled(true);
		Location loc = event.getClickedBlock().getLocation();
		Location[] sel = PortalPlayerData.getSelections().getOrDefault(player.getUniqueId(), new Location[2]);

		if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
			sel[0] = loc;
			PortalMessages.sendColorLocalized(player, ChatColor.GOLD, "position_set", 1);
		} else {
			sel[1] = loc;
			PortalMessages.sendColorLocalized(player, ChatColor.GOLD, "position_set", 2);
		}

		PortalPlayerData.getSelections().put(player.getUniqueId(), sel);
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Location from = event.getFrom();
		Location to = event.getTo();

		if (event.isCancelled()) return;

		if ((from.getBlockX() == to.getBlockX()
				&& from.getBlockY() == to.getBlockY()
				&& from.getBlockZ() == to.getBlockZ())) return;

		Player player = event.getPlayer();
		if (PortalPlayerData.getTeleportCooldown().containsKey(player.getUniqueId())) {
			if (System.currentTimeMillis() < PortalPlayerData.getTeleportCooldown().get(player.getUniqueId())) return;
		}

		for (Portal portal : plugin.getManager().getPortals()) {
			if (!portal.isInside(to)) continue;
			if (portal.targetName == null || portal.targetName.isEmpty()) continue;
			handlePortal(portal, player, to);
			return;
		}
	}

	void handlePortal(Portal portal, Player player, Location to) {
		Portal targetPortal = plugin.getManager().getPortal(portal.targetName);
		if (targetPortal == null) {
			PortalMessages.sendColorLocalized(player, ChatColor.RED, "portal_broken");
			return;
		}

		double offsetX = to.getX() - portal.min.getX();
		double offsetY = to.getY() - portal.min.getY();
		double offsetZ = to.getZ() - portal.min.getZ();

		World targetWorld = Bukkit.getWorld(targetPortal.worldName);
		if (targetWorld == null) return;

		Location destLoc = targetPortal.min.toLocation(targetWorld);

		destLoc.add(offsetX, offsetY, offsetZ);
		destLoc.setYaw(player.getLocation().getYaw());
		destLoc.setPitch(player.getLocation().getPitch());

		player.teleport(destLoc);
		plugin.playSoundFromConfig(player, plugin.getConfig().getString("sound"), destLoc);

		long cooldown = plugin.getConfig().getLong("cooldown", 1000);
		PortalPlayerData.getTeleportCooldown().put(player.getUniqueId(), System.currentTimeMillis() + cooldown);
	}

	@EventHandler
	void onQuit(PlayerQuitEvent event) {
		PortalPlayerData.getTeleportCooldown().remove(event.getPlayer().getUniqueId());
	}
}
