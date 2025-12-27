package me.nachtok0.yeeportals;

import me.nachtok0.yeeportals.subcommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class PortalCommand implements CommandExecutor {

	YeePortals plugin;
	PortalManager manager;
	Map<String, PortalSubcommand> subcommands = new HashMap<>();

	public PortalCommand(YeePortals plugin, PortalManager manager) {
		this.plugin = plugin;
		this.manager = manager;
		registerSubcommands();
	}

	private void registerSubcommands() {
		register(new PortalRegionSubcommand());
		register(new PortalListSubcommand(manager));
		register(new PortalCreateSubcommand(manager));
		register(new PortalDeleteSubcommand(manager));
		register(new PortalConnectSubcommand(manager));
		register(new PortalDisconnectSubcommand(manager));
		register(new PortalReloadConfigSubcommand(plugin));
	}

	private void register(PortalSubcommand sub) {
		subcommands.put(sub.getName(), sub);
		for (String alias : sub.getAliases())
			subcommands.put(alias, sub);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("portal.admin")) {
			PortalMessages.sendLocalized(sender, "no_permission");
			return true;
		}

		if (args.length == 0) {
			showHelp(sender);
			return true;
		}

		PortalSubcommand sub = subcommands.get(args[0].toLowerCase());

		if (sub == null) {
			PortalMessages.sendLocalized(sender, "subcommand_unknown");
			return true;
		}

		if (!sub.execute(sender, args))
			PortalMessages.sendLocalized(sender, "subcommand_usage", displayUsage(sub));

		return true;
	}

	private String displayUsage(PortalSubcommand sub) {
		return String.format("&e/portal &b%s", sub.getUsage());
	}

	private void showHelp(CommandSender sender) {
		PortalMessages.send(sender, "&6--- %s ---", "help_menu_title", plugin.getName());
		for (PortalSubcommand sub : new HashSet<>(subcommands.values())) {
			String description = PortalMessages.get(String.format("subcommand_descriptions.%s", sub.getName()));
			PortalMessages.send(sender, String.format("&e%s&r - &f%s", displayUsage(sub), description));
		}
	}
}
