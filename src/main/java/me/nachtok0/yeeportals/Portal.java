package me.nachtok0.yeeportals;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Portal {
	String name;
	String worldName;
	Vector min;
	Vector max;
	String targetName;

	public Portal(String name, String worldName, Vector v1, Vector v2) {
		this.name = name;
		this.worldName = worldName;
		this.min = Vector.getMinimum(v1, v2);
		this.max = Vector.getMaximum(v1, v2);
	}

	public boolean isInside(Location loc) {
		if (!loc.getWorld().getName().equals(worldName)) return false;
		return loc.getX() >= min.getX() && loc.getX() <= max.getX() + 1 &&
				loc.getY() >= min.getY() && loc.getY() <= max.getY() + 1 &&
				loc.getZ() >= min.getZ() && loc.getZ() <= max.getZ() + 1;
	}
}
