package com.eci.quickundo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class ChangeManager {

	public Map<Location, BlockChange> changeMap; // Map for holding the different changes the player makes, and the plugin records. This makes it easy to locate previous changes by using the location as key
	
	public World world;
	public Player player;
	public boolean active; // Value for keeping track of whether undo has been done. Also functions as a checkpoint for whether the plugin should record mere changes in the main file

	public ChangeManager(Player player) {
		this.changeMap = new HashMap<Location, BlockChange>();
		this.player = player;
		this.world = player.getWorld();
		this.active = true;
	}
	
	public void undo() {
		if (active) { // Check if undo has been done before
			
			if (changeMap.size() != 0) { // Check if there has even been changes
				
				Collection<BlockChange> changeCollection = changeMap.values();
				
				for (BlockChange change : changeCollection) {
					world.getBlockAt(change.blockLocation).setType(change.prevBlockMaterial); // Change their material back to the previous
				}
				
				active = false; // Set active to false, as the plugin should no longer keep track of the blocks that are broken and placed
				QUndo.formatMessage(player, ChatColor.GREEN + "Undo complete");
				QUndo.formatMessage(player, changeMap.size() + " changes affected"); // Count the changes and send message
				
			} else {
				
				QUndo.formatMessage(player, ChatColor.RED + "Nothing to be undone");
				
			}
		} else {
			
			QUndo.formatMessage(player, ChatColor.RED + "Quick Undo has already been done");
			
		}
	}

	public void redo() {
		if (!active) {
			
			if (changeMap.size() != 0) {
				
				Collection<BlockChange> changeCollection = changeMap.values();
				
				for (BlockChange change : changeCollection) {
					world.getBlockAt(change.blockLocation).setType(change.currBlockMaterial); // Change their material back to the previous
				}
				
				active = true; // Set active to true, as the blocks that are changed after this point, should be remembered
				QUndo.formatMessage(player, ChatColor.GREEN + "Redo complete");
				QUndo.formatMessage(player, changeMap.size() + " changes affected"); // Count the changes and send message
				
			} else {
				
				QUndo.formatMessage(player, ChatColor.RED + "Nothing to be redone");
				
			}
		} else {
			
			QUndo.formatMessage(player, ChatColor.RED + "Quick Redo has already been done");
			
		}
	}

}
