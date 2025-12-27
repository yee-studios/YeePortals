package me.nachtok0.yeeportals;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Path;

public class YeePortals extends JavaPlugin {
	PortalManager manager;

	public PortalManager getManager() {
		return manager;
	}

	@Override
	public void onEnable() {
		saveDefaultConfig();
		manager = new PortalManager(this);

		load();

		getServer().getPluginManager().registerEvents(new PortalEvents(this), this);
		getCommand("portal").setExecutor(new PortalCommand(this, manager));
		getLogger().info("YeePortals activado correctamente.");
	}

	public void load() {
		reloadConfig();
		manager.loadPortals();
		String localeFilename = String.format("%s.yml", getConfig().getString("locale", "en"));
		File localeConfigFile = Path.of(getDataFolder().toString(), localeFilename).toFile();
		PortalMessages.localeConfig = YamlConfiguration.loadConfiguration(localeConfigFile);
	}
}