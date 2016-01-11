package noki.moreturtles.turtle.peripheral;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import noki.moreturtles.MoreTurtlesData;
import noki.moreturtles.turtle.common.EFailureReason;
import noki.moreturtles.turtle.common.MTTurtleAccess;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleCommand;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;


/**********
 * @class PeripheralLootingCommand
 *
 * @description Looting Turtleのattack系コマンドを定義するクラスです。
 * @description_en Class of the attack-type commands of Looting Turtle.
 * 
 * @see TurtleEvent.onLivingDropsByLootingTurtle().
 */
public class PeripheralLootingCommand implements ITurtleCommand {
	
	//******************************//
	// define member variables.
	//******************************//
	private TurtleSide side;
	private EnumFacing dir;
	private static int[] yaw = {0, 0, 180, 0, 90, -90};
	private static int[] pitch = {90, -90, 0, 0, 0, 0};
	private static String[] allowedTool = {
		MoreTurtlesData.cc_Sword
	};
	
	
	//******************************//
	// define member methods.
	//******************************//
	public PeripheralLootingCommand(TurtleSide side, EnumFacing dir) {
		
		this.side = side;
		this.dir = dir;
		
	}
	
	@Override
	public TurtleCommandResult execute(ITurtleAccess givenTurtle) {
		
		MTTurtleAccess turtle = new MTTurtleAccess(givenTurtle);
		
		//check the other side upgrade.
		if(!this.checkTool(turtle, this.side)) {
			return MTTurtleAccess.result(false, EFailureReason.NO_ATTACK_TOOL);
		}
		
		//try to attack.
		//set startup info.
		turtle.setTurtleInfo();
		turtle.playerTurtle.posX += 0.5D;
		turtle.playerTurtle.posY += 0.5D;
		turtle.playerTurtle.posZ += 0.5D;
		turtle.playerTurtle.rotationYaw = yaw[this.dir.getIndex()];
		turtle.playerTurtle.rotationPitch = pitch[this.dir.getIndex()];
		
		Vec3 turtlePos = new Vec3(turtle.playerTurtle.posX, turtle.playerTurtle.posY, turtle.playerTurtle.posZ);
		Vec3 rayDir = turtle.playerTurtle.getLook(1.0F);
		Vec3 rayStart = turtlePos.addVector(rayDir.xCoord*0.4D, rayDir.yCoord*0.4D, rayDir.zCoord*0.4D);
		EntityLivingBase hitEntity = this.getNearestLivingEntity(turtle.world, rayStart, rayDir, 1.1D);
		if(hitEntity != null) {
			if(turtle.consumeFuel(MoreTurtlesData.consumedFuelLevel) == false) {
				return MTTurtleAccess.result(false, EFailureReason.NO_FUEL);
			}
			
			HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
			map.put(Enchantment.looting.effectId, 3);
			ItemStack stack = new ItemStack(Items.diamond_sword);
			EnchantmentHelper.setEnchantments(map, stack);
			
			turtle.setCurrentItem(stack);
			float damage =
					(float)turtle.playerTurtle.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
			//	*9.0F is Melee Turle's damage multiplier.
			damage *= 9.0F;
			
			boolean flag = false;
			if(damage > 0.0F) {
				flag = hitEntity.attackEntityFrom(DamageSource.causePlayerDamage(turtle.playerTurtle), damage);
			}
			if(flag == true) {
				//	also see TurtleEvent.onLivingDropByLootingTurtle().
				if(hitEntity.getHealth() <= 0.0F) {
					for(EntityItem each: hitEntity.capturedDrops) {
						turtle.store(each.getEntityItem());
					}
				}
				return MTTurtleAccess.result(true);
			}
		}
		
		return MTTurtleAccess.result(false, EFailureReason.NO_ATTACK_TARGET);
		
	}
	
	private EntityLivingBase getNearestLivingEntity(World world, Vec3 vecStart, Vec3 vecDir, double distance) {
		
		Vec3 vecEnd = vecStart.addVector(vecDir.xCoord*distance, vecDir.yCoord*distance, vecDir.zCoord*distance);

		//in case that there is a block near the turtle (which could shorten the turtle's hit range).
		MovingObjectPosition result =
				world.rayTraceBlocks(vecStart.addVector(0.0D, 0.0D, 0.0D), vecEnd.addVector(0.0D, 0.0D, 0.0D));
		if(result != null && result.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
			distance = vecStart.distanceTo(result.hitVec);
			vecEnd = vecStart.addVector(vecDir.xCoord*distance, vecDir.yCoord*distance, vecDir.zCoord*distance);
		}
		
		//range. It's a box of 0.75*0.75*distance. The 0.25D is not an important value.
		//(because turtle's pitch and yaw are always n*90, each coord is about 0 or about 1.)
		float xStretch = Math.abs(vecDir.xCoord) > 0.25D ? 0.0F : 1.0F;
		float yStretch = Math.abs(vecDir.yCoord) > 0.25D ? 0.0F : 1.0F;
		float zStretch = Math.abs(vecDir.zCoord) > 0.25D ? 0.0F : 1.0F;
		AxisAlignedBB rangeBox = new AxisAlignedBB(
				Math.min(vecStart.xCoord, vecEnd.xCoord) - 0.375F * xStretch,
				Math.min(vecStart.yCoord, vecEnd.yCoord) - 0.375F * yStretch,
				Math.min(vecStart.zCoord, vecEnd.zCoord) - 0.375F * zStretch,
				Math.max(vecStart.xCoord, vecEnd.xCoord) + 0.375F * xStretch,
				Math.max(vecStart.yCoord, vecEnd.yCoord) + 0.375F * yStretch,
				Math.max(vecStart.zCoord, vecEnd.zCoord) + 0.375F * zStretch
		);
		
		EntityLivingBase closest = null;
		double closestDist = 99.0D;
		@SuppressWarnings("rawtypes")
		List list = world.getEntitiesWithinAABBExcludingEntity(null, rangeBox);
		for(Object target: list) {
			if(target instanceof EntityPlayer || !(target instanceof EntityLivingBase)) {
				continue;
			}
			EntityLivingBase entity = (EntityLivingBase)target;
			if(entity.isEntityAlive()) {
				AxisAlignedBB entityBox = entity.getBoundingBox();
				//the blow is Dan's code.
/*				if(entityBox.isVecInside(vecStart)) {
					closest = entity;
					closestDist = 0.0D;
				}
				else {
					MovingObjectPosition entityBoxResult = rangeBox.calculateIntercept(vecStart, vecEnd);
					if(entityBoxResult != null) {
						double dist = vecStart.distanceTo(entityBoxResult.hitVec);
						if(closest == null || dist <= closestDist) {
							closest = entity;
							closestDist = dist;
						}
					}
					else if(entityBox.intersectsWith(rangeBox)) {
						if(closest == null) {
							closest = entity;
							closestDist = distance;
						}
					}
				}*/
				//this is Noki's code. a bit looser comparing to Dan's.
				if(entityBox.isVecInside(vecStart)) {
					closest = entity;
					closestDist = 0.0D;
					break;
				}
				else {
					Vec3 vecEntity = new Vec3(entity.posX, entity.posY, entity.posZ);
					if(vecStart.distanceTo(vecEntity) < closestDist) {
						closest = entity;
						closestDist = vecStart.distanceTo(vecEntity);
					}
				}
			}
		}
/*		if(closest != null && closestDist <= distance) {
			return closest;
		}*/
		if(closest != null) {
			return closest;
		}
		
		return null;
		
	}
	
	private boolean checkTool(MTTurtleAccess turtle, TurtleSide side) {
		
		if(turtle.getOtherUpgrade(side) == null) {
			return false;
		}
		
		return Arrays.asList(allowedTool).contains(turtle.getOtherUpgradeName(side));
		
	}

}
