package me.nachtok0.yeeportals;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class PortalManager {
	private final List<Portal> portals = new ArrayList<>();
	private final YamlConfiguration config = new YamlConfiguration();
	JavaPlugin plugin;

	public PortalManager(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public void createPortal(String name, Location pos1, Location pos2) {
		Portal portal = new Portal(name, pos1.getWorld().getName(), pos1.toVector(), pos2.toVector());
		portals.add(portal);
		savePortals();
	}

	public void deletePortal(Portal portal) {
		portals.remove(portal);
		config.set("portals." + portal.name, null);
		savePortals();
	}

	public List<Portal> getPortals() {
		return portals;
	}

	public Portal getPortal(String name) {
		for (Portal p : portals) {
			if (p.name.equalsIgnoreCase(name)) return p;
		}
		return null;
	}

	public void savePortals() {
		for (Portal p : portals) {
			String path = "portals." + p.name;
			config.set(path + ".world", p.worldName);
			config.set(path + ".min", p.min);
			config.set(path + ".max", p.max);
			config.set(path + ".target", p.targetName);
		}

		try {
			config.save(new File(plugin.getDataFolder(), "portals.yml"));
		} catch (IOException throwable) {
			plugin.getLogger().log(Level.SEVERE, "Could not save portals!", throwable);
		}
	}

	public void loadPortals() {
		portals.clear();
		ConfigurationSection sec = config.getConfigurationSection("portals");
		if (sec == null) return;

		for (String key : sec.getKeys(false)) {
			String world = sec.getString(key + ".world");
			Vector min = sec.getVector(key + ".min");
			Vector max = sec.getVector(key + ".max");
			String target = sec.getString(key + ".target");

			Portal portal = new Portal(key, world, min, max);
			portal.targetName = target;
			portals.add(portal);
		}
	}
}
