package me.nachtok0.yeeportals;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PortalEvents implements Listener {
	YeePortals plugin;

	public PortalEvents(YeePortals plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (!PortalPlayerData.getSelectionMode().contains(p.getUniqueId())) return;

		if (e.getAction() != Action.LEFT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

		e.setCancelled(true);
		Location loc = e.getClickedBlock().getLocation();
		Location[] sel = PortalPlayerData.getSelections().getOrDefault(p.getUniqueId(), new Location[2]);

		if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
			sel[0] = loc;
			p.sendMessage(ChatColor.GOLD + "Posición 1 establecida.");
		} else {
			sel[1] = loc;
			p.sendMessage(ChatColor.GOLD + "Posición 2 establecida.");
		}
		PortalPlayerData.getSelections().put(p.getUniqueId(), sel);
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
			player.sendMessage(ChatColor.RED + "El portal de destino no existe o está roto.");
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

		String[] splitted = plugin.getConfig().getString("sound").split(";");

		Sound sound = Sound.ORB_PICKUP;
		try {
			Sound.valueOf(splitted[0]);
		} catch (Exception ignored) {
		}

		float volume = 1;
		try {
			volume = Float.parseFloat(splitted[1]);
		} catch (Exception ignored) {
		}

		float pitch = 1;
		try {
			pitch = Float.parseFloat(splitted[2]);
		} catch (Exception ignored) {
		}

		player.playSound(destLoc, sound, volume, pitch);

		long cooldown = plugin.getConfig().getLong("cooldown", 1000);
		PortalPlayerData.getTeleportCooldown().put(player.getUniqueId(), System.currentTimeMillis() + cooldown);
	}
}
