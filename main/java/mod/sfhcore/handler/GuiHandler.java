package mod.sfhcore.handler;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import mod.sfhcore.blocks.Cube;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class GuiHandler implements IGuiHandler {
	
	protected static List<Pair<Class<TileEntity>, Class<Container>>> teco = new ArrayList<Pair<Class<TileEntity>, Class<Container>>>();
	
	public GuiHandler(Object mod) {
		NetworkRegistry.INSTANCE.registerGuiHandler(mod, this);
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity te1 = world.getTileEntity(new BlockPos(x, y, z));
		
			for(Pair<Class<TileEntity>, Class<Container>> tc : teco) {
				if(te1.getClass().equals(tc.getLeft().getClass())){
					
					Class<Container> c = (Class<Container>) tc.getRight();
					try {
					Constructor<Container> con = c.getConstructor((Class<InventoryPlayer>) player.inventory.getClass(), (Class<TileEntity>) te1.getClass());
					return con.newInstance(player.inventory, te1);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					
				}
			}
			
		return null;		
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity te1 = world.getTileEntity(new BlockPos(x, y, z));
		
		for(Pair<Class<TileEntity>, Class<Container>> tc : teco) {
			if(te1.getClass().equals(tc.getLeft().getClass())){
				
				Class<Container> c = (Class<Container>) tc.getRight();
				try {
				Constructor<Container> con = c.getConstructor((Class<InventoryPlayer>) player.inventory.getClass(), (Class<TileEntity>) te1.getClass());
				return con.newInstance(player.inventory, te1);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
			}
		}
		
		return null;
	}

	/**
	 * Use this to register your Tile Entity GUI's and their Container
	 * @param fuel
	 * @param time
	 * @return
	 */
	public static boolean addGUIRelation(Object tile, Object con) {
		return teco.add(new ImmutablePair<Class<TileEntity>, Class<Container>>((Class<TileEntity>)tile, (Class<Container>)con));
	}
}

