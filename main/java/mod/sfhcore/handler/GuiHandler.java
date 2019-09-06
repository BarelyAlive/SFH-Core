package mod.sfhcore.handler;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	
	private static List<Pair<Class<GuiContainer>, Class<Container>>> teco = new ArrayList<Pair<Class<GuiContainer>, Class<Container>>>();
	
	public static List<Pair<Class<GuiContainer>, Class<Container>>> getTeco() {
		return teco;
	}

	@Override
	public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity te1 = world.getTileEntity(new BlockPos(x, y, z));
		
			for(Pair<Class<GuiContainer>, Class<Container>> tc : teco) {
				if(te1.getClass().equals(tc.getLeft().getClass())){
					
					Class<Container> c = tc.getRight();
					try {
						Constructor<Container> con = c.getConstructor(player.inventory.getClass(), te1.getClass());
						return con.newInstance(player.inventory, te1);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					
				}
			}
			
		return null;
	}

	@Override
	public GuiContainer getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity te1 = world.getTileEntity(new BlockPos(x, y, z));
		
		for(Pair<Class<GuiContainer>, Class<Container>> tc : teco) {
			if(te1.getClass().equals(tc.getLeft().getClass())){
				
				Class<GuiContainer> c = tc.getLeft();
				
				try {
					Constructor<GuiContainer> con = c.getConstructor(player.inventory.getClass(), te1.getClass());
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
	public static int addGUIRelation(@Nonnull Object gui, @Nonnull Object con) {
		teco.add(new ImmutablePair<Class<GuiContainer>, Class<Container>>((Class<GuiContainer>)gui, (Class<Container>)con));
		return (teco.size() - 1);
	}
}

