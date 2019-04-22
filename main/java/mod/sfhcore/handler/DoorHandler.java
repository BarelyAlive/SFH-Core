package mod.sfhcore.handler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

class Door
{
	private Item iDoor;
	private Block bDoor;
	
	public Item getiDoor() {
		return iDoor;
	}

	public Block getbDoor() {
		return bDoor;
	}

	protected Door(Item item, Block block)
	{
		this.iDoor = item;
		this.bDoor = block;
	}
}

public class DoorHandler
{
	private static List<Door> doors = new ArrayList<Door>();
	
	public static Item getItemDoor(Block b)
	{
		for(Door d : doors)
		{
			if(d.getbDoor().equals(b))
				return d.getiDoor();
		}
		return null;
	}
	
	public static Block getBlockDoor(Item i)
	{
		for(Door d : doors)
		{
			if(d.getiDoor().equals(i))
				return d.getbDoor();
		}
		return null;
	}
}
