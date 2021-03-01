package com.eci.quickundo;

import org.bukkit.Location;
import org.bukkit.Material;

public class BlockChange {

	public Location blockLocation; // This might someday be removed and handled completely outside of this class
	public Material prevBlockMaterial, currBlockMaterial; // Variables for keeping track of the materials. When undoing, the blocks are changed to prevBlockMaterial and when redoing the blocks are changed to currBlockMaterial
	
	public BlockChange(Location blockLocation, Material prevBlockMaterial, Material currBlockMaterial) {
		this.blockLocation = blockLocation;
		this.prevBlockMaterial = prevBlockMaterial;
		this.currBlockMaterial = currBlockMaterial;
	}
	
}
