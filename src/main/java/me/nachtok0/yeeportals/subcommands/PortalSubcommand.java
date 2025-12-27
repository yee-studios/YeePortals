package me.nachtok0.yeeportals.subcommands;

import org.bukkit.command.CommandSender;

public abstract class PortalSubcommand {
	private final String[] aliases;
	private final String name;

	public PortalSubcommand(String name, String... aliases) {
		this.name = name;
		this.aliases = aliases;
	}

	public String getName() {
		return name;
	}

	public String[] getAliases() {
		return aliases;
	}

	public abstract String getUsage();

	public abstract boolean execute(CommandSender sender, String[] args);
}
