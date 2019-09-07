package mod.sfhcore.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityInfo
{
	private Class <? extends Entity > entityClass;

	public Class<? extends Entity> getEntityClass() {
		return entityClass;
	}

	private final String name;

	public EntityInfo(final String entityName){
		name = entityName;
		entityClass = entityName == null ? null : EntityList.getClass(new ResourceLocation(entityName));
	}

	/**
	 * Attempts to spawn entity located  within `range` of `pos`
	 * @param pos Position to spawn near
	 * @param range Distance from pos to attempt spawns at
	 * @param worldIn World to spawn in
	 * @return whether it did spawn the mob
	 */
	public boolean spawnEntityNear(final BlockPos pos, final int range, final World worldIn){
		if (entityClass == null || name == null)
			return false;

		if(!worldIn.isRemote && worldIn.getDifficulty() != EnumDifficulty.PEACEFUL){
			Entity entity = EntityList.newEntity(entityClass, worldIn);
			if(entity instanceof EntityLiving){
				EntityLiving entityLiving = (EntityLiving) entity;

				if(entityLiving instanceof EntitySlime)
					((EntitySlime) entityLiving).getEntityData().setInteger("Size", 1);

				double dx = (worldIn.rand.nextDouble() - worldIn.rand.nextDouble())*range + 0.5;
				double dy = (worldIn.rand.nextDouble() - worldIn.rand.nextDouble())*range;
				double dz = (worldIn.rand.nextDouble() - worldIn.rand.nextDouble())*range + 0.5;
				BlockPos spawnPos = new BlockPos(pos.getX()+dx, pos.getY()+dy, pos.getZ()+dz);

				entityLiving.setPosition(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());

				boolean canSpawn = worldIn.getCollisionBoxes(entityLiving, entityLiving.getEntityBoundingBox()).isEmpty();
				if(canSpawn) {
					worldIn.spawnEntity(entityLiving);
					worldIn.playEvent(2004, pos, 0);
					entityLiving.spawnExplosionParticle();
					return true;
				} else
					return false;
			}
			return false; // Not a Living Entity, not currently handled.
		}

		return false;
	}

	public static final EntityInfo EMPTY = new EntityInfo(null);

	public String getName() {
		return name;
	}
}
