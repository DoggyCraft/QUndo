package com.eci.quickundo;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;

public class CommandManager implements CommandExecutor {

	public Plugin plugin; // In use when setting metadata

	public CommandManager(Plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (label.equalsIgnoreCase("q") || label.equalsIgnoreCase("qundo")) {
			if (sender.hasPermission("qundo.use")) {
				if (args.length == 1) {
					if (sender instanceof Player) {
						Player player = (Player) sender;
						// SET

						if (args[0].equalsIgnoreCase("set")) { // This should probably be a switch, but fuck that. It'd
																// be a bit annoying for ignoring the case
							ChangeManager changeManager = new ChangeManager(player);
							player.setMetadata("qundo.ability", new FixedMetadataValue(plugin, changeManager)); // Add the class responsible for keeping track of changes to the players metadata
							QUndo.formatMessage(player, ChatColor.GREEN + "Time point set");
						}

						// UNDO

						else if (args[0].equalsIgnoreCase("undo")) {
							if (player.hasMetadata("qundo.ability") && player.getMetadata("qundo.ability").get(0).value() instanceof ChangeManager) { // Making sure the player has the right metadata, if at all
								ChangeManager changeManager = (ChangeManager) player.getMetadata("qundo.ability").get(0).value(); // Get the changeManager from the metadata
								changeManager.undo(); // This could have been done directly on the .value() in a cast with parenthesis, however this is better for readability
								return true;
							}
							QUndo.formatMessage(player, ChatColor.RED + "No time point set");
						}

						// REDO

						else if (args[0].equalsIgnoreCase("redo")) {
							if (player.hasMetadata("qundo.ability") && player.getMetadata("qundo.ability").get(0).value() instanceof ChangeManager) {
								ChangeManager changeManager = (ChangeManager) player.getMetadata("qundo.ability").get(0).value();
								changeManager.redo();
								return true;
							}
							QUndo.formatMessage(player, ChatColor.RED + "No time point set");
						}

						// EXIT

						else if (args[0].equalsIgnoreCase("exit")) {
							if (player.hasMetadata("qundo.ability") && player.getMetadata("qundo.ability").get(0).value() instanceof ChangeManager) {
								player.setMetadata("qundo.ability", new FixedMetadataValue(plugin, null)); // Set the metadata to null, as to not take up memory that is not being used anyway. Also functions as a way to check if the player has an ongoing ChangeManager
								QUndo.formatMessage(player, ChatColor.GREEN + "Time point removed");
								return true; // Don't send the "No time point" message
							}
							QUndo.formatMessage(player, ChatColor.RED + "No time point to be removed");
						}

						// UNKNOWN

						else {
							QUndo.formatMessage(player, ChatColor.RED + "Command argument \"" + args[0] + "\" unknown");
						}
					} else {
						QUndo.formatMessage(sender, ChatColor.RED + "This command can only be used by players");
					}
				} else if (args.length > 1) { // This might be changed in the future, if the need to exit and set other peoples time points ever arises
					QUndo.formatMessage(sender, ChatColor.RED + "Too many arguments");
				} else {
					printInfo(sender);
				}
			} else {
				QUndo.formatMessage(sender, ChatColor.RED + "Insufficient permission");
			}
		}
		return true;
	}

	public void printInfo(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "-Quick Undo V" + this.plugin.getDescription().getVersion() + "-");
		sender.sendMessage(ChatColor.AQUA + "/qundo set" + ChatColor.GOLD + " | Setting time point");
		sender.sendMessage(ChatColor.AQUA + "/qundo undo" + ChatColor.GOLD + " | Undoing changes");
		sender.sendMessage(ChatColor.AQUA + "/qundo redo" + ChatColor.GOLD + " | Redoing changes");
		sender.sendMessage(ChatColor.AQUA + "/qundo exit" + ChatColor.GOLD + " | Removing time point");
		sender.sendMessage(ChatColor.GOLD + "\"" + ChatColor.AQUA + "qundo" + ChatColor.GOLD + "\" can be substituted with \"" + ChatColor.AQUA + "q" + ChatColor.GOLD + "\"");
	}

}
