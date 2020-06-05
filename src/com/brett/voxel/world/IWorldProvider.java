package com.brett.voxel.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.MultiKeyMap;

import com.brett.voxel.world.blocks.Block;
import com.brett.voxel.world.chunk.IChunkProvider;
import com.brett.voxel.world.player.Player;
import com.brett.voxel.world.tileentity.TileEntity;

/**
*
* @author brett
* @date Jun. 1, 2020
*/

public class IWorldProvider {
	
	public List<TileEntity> tents = new ArrayList<TileEntity>();
	public MultiKeyMap<Integer, TileEntity> tileEntities = new MultiKeyMap<Integer, TileEntity>();
	public Random random = new Random();
	public volatile IChunkProvider chunk;
	public Player ply;
	
	public void spawnTileEntity(TileEntity e, int x, int y, int z) {
		tents.add(e);
		tileEntities.put(x, y, z, e);
		e.spawnTileEntity(x, y, z, this);
	}
	
	public TileEntity getTileEntity(int x, int y, int z) {
		return tileEntities.get(x, y, z);
	}
	
	public void destoryTileEntity(TileEntity e) {
		if (e == null)
			return;
		e.destroy();
		if (tents.contains(e))
			tents.remove(e);
		MapIterator<MultiKey<? extends Integer>,TileEntity> ents = tileEntities.mapIterator();
		try {
			while (ents.hasNext()) {
				MultiKey<? extends Integer> kes = ents.next();
				if (tileEntities.containsKey(kes.getKey(0), kes.getKey(1), kes.getKey(2))) {
					tileEntities.removeMultiKey(kes.getKey(0), kes.getKey(1), kes.getKey(2));
				}
			}
		} catch (Exception edd) {}
	}
	
	public void updateBlocksAround(int x, int y, int z) {
		Block.blocks.get(this.chunk.getBlockBIAS(x, y + 1, z)).onBlockUpdated(x, y + 1, z, this);
		Block.blocks.get(this.chunk.getBlockBIAS(x, y - 1, z)).onBlockUpdated(x, y - 1, z, this);
		Block.blocks.get(this.chunk.getBlockBIAS(x+1, y, z)).onBlockUpdated(x+1, y, z, this);
		Block.blocks.get(this.chunk.getBlockBIAS(x-1, y, z)).onBlockUpdated(x-1, y, z, this);
		Block.blocks.get(this.chunk.getBlockBIAS(x, y, z+1)).onBlockUpdated(x, y, z+1, this);
		Block.blocks.get(this.chunk.getBlockBIAS(x, y, z-1)).onBlockUpdated(x, y, z-1, this);
	}
	
}