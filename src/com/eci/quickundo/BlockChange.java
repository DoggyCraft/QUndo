package com.eci.quickundo;

import org.bukkit.Location;
import org.bukkit.Material;

public class BlockChange {

	private Location blockChangeLocation; // This might someday be removed and handled completely outside of this class
	private Material prevBlockMaterial; // Variables for keeping track of the materials. When undoing, the blocks are changed to prevBlockMaterial and when redoing the blocks are changed to currBlockMaterial
	private Material currBlockMaterial;

	public BlockChange(Location blockLocation, Material prevBlockMaterial, Material currBlockMaterial) {
		this.blockChangeLocation = blockLocation;
		this.prevBlockMaterial = prevBlockMaterial;
		this.currBlockMaterial = currBlockMaterial;
	}
	
	public Location getBlockChangeLocation() {
		return blockChangeLocation;
	}
	
	public Material getPrevBlockMaterial() {
		return prevBlockMaterial;
	}
	
	public Material getCurrBlockMaterial() {
		return currBlockMaterial;
	}
	
	public String toString() { // For later use
		return "[" + blockChangeLocation.getBlockX() + ", " + blockChangeLocation.getBlockY() + ", " + blockChangeLocation.getBlockZ() + "] " + prevBlockMaterial + " -> " + currBlockMaterial;
	}
	
}
