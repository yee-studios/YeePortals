package me.nachtok0.yeeportals.subcommands;

import me.nachtok0.yeeportals.PortalMessages;
import me.nachtok0.yeeportals.YeePortals;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class PortalReloadConfigSubcommand extends PortalSubcommand {
	YeePortals plugin;

	public PortalReloadConfigSubcommand(YeePortals plugin) {
		super("reload", "rel", "rl");
		this.plugin = plugin;
	}

	@Override
	public String getUsage() {
		return "";
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		plugin.reloadConfig();
		PortalMessages.sendColorLocalized(sender, ChatColor.GREEN, "reloaded_config");
		return true;
	}
}
