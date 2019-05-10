package mod.sfhcore.network;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import mod.sfhcore.blocks.tiles.TileFluidInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageFluidTankContents implements IMessage {

	private IFluidTankProperties[] fluidTankProperties;
	private TileFluidInventory te;
	int x, y, z;
	
	public MessageFluidTankContents() {
	}

	public MessageFluidTankContents(IFluidTankProperties[] fluidTankProperties, TileFluidInventory te) {
		this.fluidTankProperties = fluidTankProperties;
		this.te = te;
	}


	/**
	 * Convert from the supplied buffer into your specific message type
	 *
	 * @param buf The buffer
	 */
	@Override
	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		final int numProperties = buf.readInt();
		fluidTankProperties = new IFluidTankProperties[numProperties];

		for (int i = 0; i < numProperties; i++) {
			final NBTTagCompound tagCompound = ByteBufUtils.readTag(buf);
			final FluidStack contents = FluidStack.loadFluidStackFromNBT(tagCompound);

			final int capacity = buf.readInt();
			
			fluidTankProperties[i] = new FluidTankProperties(contents, capacity);
		}
		
	}

	/**
	 * Deconstruct your message into the supplied byte buffer
	 *
	 * @param buf The buffer
	 */
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.te.getPos().getX());
		buf.writeInt(this.te.getPos().getY());
		buf.writeInt(this.te.getPos().getZ());
		buf.writeInt(fluidTankProperties.length);

		for (final IFluidTankProperties properties : fluidTankProperties) {
			final FluidStack contents = properties.getContents();
			final NBTTagCompound tagCompound = new NBTTagCompound();

			if (contents != null) {
				contents.writeToNBT(tagCompound);
			}

			ByteBufUtils.writeTag(buf, tagCompound);

			buf.writeInt(properties.getCapacity());
		}
	}

	public static class Handler implements IMessageHandler<MessageFluidTankContents, IMessage> {

		/**
		 * Called when a message is received of the appropriate type. You can optionally return a reply message, or null if no reply
		 * is needed.
		 *
		 * @param message The message
		 * @param ctx     The context
		 * @return an optional return message
		 */
		@Nullable
		@Override
		public IMessage onMessage(MessageFluidTankContents message, MessageContext ctx) {
			TileFluidInventory entity =  (TileFluidInventory) Minecraft.getMinecraft().player.world.getTileEntity(new BlockPos(message.x, message.y, message.z));
			FluidStack contents = message.fluidTankProperties[0].getContents();
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("FluidName", FluidRegistry.getFluidName(contents.getFluid()));
		    nbt.setInteger("Amount", contents.amount);
		    entity.tank.readFromNBT(nbt);
			return null;
		}
	}
}