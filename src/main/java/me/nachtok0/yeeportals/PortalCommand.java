package me.nachtok0.yeeportals;

import me.nachtok0.yeeportals.subcommands.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class PortalCommand implements CommandExecutor {

	PortalManager manager;
	Map<String, PortalSubcommand> subcommands = new HashMap<>();

	public PortalCommand(PortalManager manager) {
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
			sender.sendMessage(ChatColor.YELLOW + "/portal region " + ChatColor.WHITE + "- Activa la varita de selección.");
			sender.sendMessage(ChatColor.YELLOW + "/portal create <nombre> " + ChatColor.WHITE + "- Crea un portal en la selección.");
			sender.sendMessage(ChatColor.YELLOW + "/portal connect <origen> <destino> " + ChatColor.WHITE + "- Conecta dos portales.");
			sender.sendMessage(ChatColor.YELLOW + "/portal disconnect <nombre> " + ChatColor.WHITE + "- Quita el destino de un portal.");
			sender.sendMessage(ChatColor.YELLOW + "/portal delete <nombre> " + ChatColor.WHITE + "- Borra un portal para siempre.");
			sender.sendMessage(ChatColor.YELLOW + "/portal list " + ChatColor.WHITE + "- Ver lista de portales.");
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
		PortalMessages.send(sender, "&6--- %s ---", "help_menu_title");
		for (PortalSubcommand sub : new HashSet<>(subcommands.values())) {
			String description = PortalMessages.get(String.format("subcommand_descriptions.%s", sub.getName()));
			PortalMessages.send(sender, String.format("&e%s&r - &f%s", displayUsage(sub), description));
		}
	}
}
