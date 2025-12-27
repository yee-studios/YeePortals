package me.nachtok0.yeeportals;

import org.bukkit.Location;

import java.util.*;

public class PortalPlayerData {
	private static final Set<UUID> selectionMode = new HashSet<>();
	private static final Map<UUID, Location[]> selections = new HashMap<>();
	private static final Map<UUID, Long> teleportCooldown = new HashMap<>();

	public static Set<UUID> getSelectionMode() {
		return selectionMode;
	}

	public static Map<UUID, Location[]> getSelections() {
		return selections;
	}

	public static Map<UUID, Long> getTeleportCooldown() {
		return teleportCooldown;
	}
}
