package me.nachtok0.yeeportals;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

public class PortalMessages {
	public static YamlConfiguration localeConfig;

	public static void send(CommandSender sender, String message, Object... format) {
		String text = String.format(message, format);
		sender.sendMessage(color(text));
	}

	public static void sendLocalized(CommandSender sender, String key, Object... format) {
		String message = get(key);
		String text = String.format(message, format);
		sender.sendMessage(color(text));
	}

	public static void sendColorLocalized(CommandSender sender, ChatColor color, String key, Object... format) {
		String message = get(key);
		String text = String.format(message, format);
		sender.sendMessage(color + color(text));
	}

	public static String get(String key) {
		return color(localeConfig.getString(key, key));
	}

	public static String color(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}
}
