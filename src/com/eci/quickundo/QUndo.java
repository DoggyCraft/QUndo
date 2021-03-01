// IMPORTANT
// During a server reload, all player undo lists are removed.
// When a player disconnects the players undo list is removed.

// FUTURE ? (These things are not guaranteed)
// Maybe make so it is possible for players with certain permission, to set and exit time points for other players. This might never become necessary
// Check if the block above the one broken, has gravity. Check how many blocks above and which material they are made of. Then they can become part of the ChangeMap

package com.eci.quickundo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class QUndo extends JavaPlugin implements Listener {

	@Override
	public void onEnable() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(this, this);
		
		CommandManager cmdManager = new CommandManager(this);
		this.getCommand("qundo").setExecutor(cmdManager); // The tab completion should probably be overridden with the custom command arguments
		this.getCommand("q").setExecutor(cmdManager); // For cutting down code in the main file.
		this.getCommand("qundo").setTabCompleter(this);
		this.getCommand("q").setTabCompleter(this);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (player.hasMetadata("qundo.ability") && player.getMetadata("qundo.ability").get(0).value() instanceof ChangeManager) { // Check whether the player actually has the metadata for safety. Also check whether the metadata value is of the right type (again for safety)
			ChangeManager changeManager = (ChangeManager) player.getMetadata("qundo.ability").get(0).value();
			Map<Location, BlockChange> changeMap = changeManager.changeMap;
			if (changeManager.active) {
				Block block = event.getBlock();
				Location location = block.getLocation();
				if (changeMap.containsKey(location)) { // Check if a previous change has been made on this exact location
					BlockChange oldChange = changeMap.get(location); // Temporarily keep the old change, as to use it for inserting the old previous material of the block, into the new change.
					changeMap.replace(location, new BlockChange(location, oldChange.prevBlockMaterial, Material.AIR));
					return; // Don't run the code below, if there has been a previous change
				}

				BlockChange change = new BlockChange(location, block.getType(), Material.AIR); // Create the new change on the location, and record the materials
				changeMap.put(location, change); // Insert the change into the change map
			}
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (player.hasMetadata("qundo.ability") && player.getMetadata("qundo.ability").get(0).value() instanceof ChangeManager) {
			ChangeManager changeManager = (ChangeManager) player.getMetadata("qundo.ability").get(0).value();
			Map<Location, BlockChange> changeMap = changeManager.changeMap;
			if (changeManager.active) { // Check whether undo has been done or not. Redo activates this again, so in case you want to make more changes after redoing, you can.
				Block block = event.getBlockPlaced();
				Location location = block.getLocation();
				
				if (changeMap.containsKey(location)) {
					BlockChange oldChange = changeMap.get(location);
					changeMap.replace(location, new BlockChange(location, oldChange.prevBlockMaterial, block.getType()));
					return;
				}

				BlockChange change = new BlockChange(location, event.getBlockReplacedState().getType(), block.getType()); // This makes the new change, however, it is important to note, the change contains the block the placed block replaced. Such as water or grass
				changeMap.put(location, change);
			}
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> tabCommands = new ArrayList<String>();
		tabCommands.add("set"); // This is ew
		tabCommands.add("undo");
		tabCommands.add("redo");
		tabCommands.add("exit");
		tabCommands.add("help"); // ew ew
		return tabCommands;
	}

	public static void formatMessage(CommandSender sender, String message) {
		sender.sendMessage(ChatColor.GOLD + "[QUndo] " + message);
	}

}
