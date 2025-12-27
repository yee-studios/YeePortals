package me.nachtok0.yeeportals;

import org.bukkit.plugin.java.JavaPlugin;

public class YeePortals extends JavaPlugin {
	PortalManager manager;

	public PortalManager getManager() {
		return manager;
	}

	@Override
	public void onEnable() {
		saveDefaultConfig();
		manager = new PortalManager(this);
		manager.loadPortals();
		getServer().getPluginManager().registerEvents(new PortalEvents(this), this);
		getCommand("portal").setExecutor(new PortalCommand(manager));
		getLogger().info("YeePortals activado correctamente.");
	}
}