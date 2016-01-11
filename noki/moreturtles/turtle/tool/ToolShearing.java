package noki.moreturtles.turtle.tool;
 
import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.IShearable;
import noki.moreturtles.MoreTurtlesData;
import noki.moreturtles.items.ItemExtendedItems;
import noki.moreturtles.items.RegisterItems;
import noki.moreturtles.turtle.common.EFailureReason;
import noki.moreturtles.turtle.common.MTTurtleAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleUpgradeType;
import dan200.computercraft.api.turtle.TurtleVerb;


/**********
 * @class ToolShearing
 *
 * @description Shearing Turtleを定義するクラスです。
 * @description_en Class of Shearing Turtle.
 */
public class ToolShearing implements ITurtleUpgrade {
	
	//******************************//
	// define member variables.
	//******************************//
	public int upgradeMeta = 2;
	public ItemStack upgradeItem = new ItemStack(RegisterItems.extendedItems, 1, this.upgradeMeta);
	
	
	//******************************//
	// define member methods.
	//******************************//
	@Override
	public ResourceLocation getUpgradeID() {
		
		return new ResourceLocation("moreturtles:shearing_turtle");
		
	}
	
	@Override
	public int getLegacyUpgradeID() {
		
		return MoreTurtlesData.shearsTurtleTID;
		
	}
	
	@Override
	public String getUnlocalisedAdjective() {
		
		return "Shearing";
		
	}
	
	@Override
	public TurtleUpgradeType getType() {
		
		return  TurtleUpgradeType.Tool;
		
	}
	
	@Override
	public ItemStack getCraftingItem() {
		
		return this.upgradeItem;
		
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public Pair<IBakedModel, Matrix4f> getModel(ITurtleAccess turtle, TurtleSide side) {
		
		float xOffset = (side == TurtleSide.Left) ? -0.40625F : 0.40625F;
		Matrix4f transform = new Matrix4f(0.0F, 0.0F, -1.0F, 1.0F + xOffset, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0F, 0.0F, 1.0F,
				0.0F, 0.0F, 0.0F, 1.0F);
		Minecraft mc = Minecraft.getMinecraft();
		return Pair.of(mc.getRenderItem().getItemModelMesher().getItemModel(
				new ItemStack(((ItemExtendedItems)this.upgradeItem.getItem()).getEachExtendedItem(this.upgradeMeta))), transform);		
	}
	
	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		
		return null;
		
	}
	
	@Override
	public void update(ITurtleAccess turtle, TurtleSide side) {
		
	}
	
	@Override
	public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, EnumFacing direction) {
		
		MTTurtleAccess turtleAccess = new MTTurtleAccess(turtle);
		
		switch( verb ) {
			case Dig:
				return this.dig(turtleAccess, direction);
			case Attack:
				return this.attack(turtleAccess, direction);
		}
		return MTTurtleAccess.result(false, EFailureReason.UNKNOWN);
		
	}

	private TurtleCommandResult dig(MTTurtleAccess turtle, EnumFacing dir) {
		
		int newX = turtle.posX + dir.getFrontOffsetX();
		int newY = turtle.posY + dir.getFrontOffsetY();
		int newZ = turtle.posZ + dir.getFrontOffsetZ();
		BlockPos pos = new BlockPos(newX, newY, newZ);
		
		IBlockState state = turtle.world.getBlockState(pos);
		int metadata = state.getBlock().getMetaFromState(state);
		ItemStack item = new ItemStack(Items.shears);
		
		//check whether the block is air
		if(turtle.world.isAirBlock(pos)) {
			return MTTurtleAccess.result(false, EFailureReason.NO_DIG_TARGET);
		}
		
		//check the block hardness.
		if(state.getBlock().getBlockHardness(turtle.world, pos) <= -1.0F) {
			return MTTurtleAccess.result(false, EFailureReason.NO_DIG_TARGET);
		}
		
		if(!(state.getBlock() instanceof IShearable) &&
				item.getItem().getDigSpeed(item, state) <= 1.0F) {
			return MTTurtleAccess.result(false, EFailureReason.NO_DIG_TARGET);
		}
		
		List<ItemStack> items;
		if(state.getBlock() instanceof IShearable) {
			items = ((IShearable)state.getBlock()).onSheared(item, turtle.world, pos, 0);
		}
		else {
			items = state.getBlock().getDrops(turtle.world, pos, state, 0);
		}
		turtle.world.setBlockToAir(pos);
		turtle.world.playAuxSFX(2001, pos, Block.getStateId(state));
		if(items != null) {
			for(ItemStack each : items) {
				turtle.store(each);
			}
		}
		
		return MTTurtleAccess.result(true);
		
	}
	
	private TurtleCommandResult attack(MTTurtleAccess turtle, EnumFacing dir) {
		
		int newX = turtle.posX + dir.getFrontOffsetX();
		int newY = turtle.posY + dir.getFrontOffsetY();
		int newZ = turtle.posZ + dir.getFrontOffsetZ();
		
		//define the region for searching targets.
		AxisAlignedBB aabb = new AxisAlignedBB(newX, newY, newZ, newX+1.0D, newY+1.0D, newZ+1.0D);
		//get entities.
		@SuppressWarnings("rawtypes")
		List list = turtle.world.getEntitiesWithinAABBExcludingEntity(turtle.playerTurtle, aabb);
		
		if(list == null || list.size() == 0) {
			return MTTurtleAccess.result(false, EFailureReason.NO_ATTACK_TARGET);
		}
		
		for(Object each: list) {
			if(each != null && each instanceof IShearable) {
				ItemStack shears = new ItemStack(Items.shears);
				Entity entity = (Entity)each;
				if(((IShearable)entity).isShearable(shears, entity.worldObj,
						new BlockPos((int)entity.posX, (int)entity.posY, (int)entity.posZ))) {
					List<ItemStack> ret = ((IShearable)entity).onSheared(shears, entity.worldObj,
								new BlockPos((int)entity.posX, (int)entity.posY, (int)entity.posZ), 0);
					if(ret != null) {
						for(ItemStack item: ret) {
							turtle.store(item);
						}
					}
					return MTTurtleAccess.result(true);
				}
			}
		}

		return MTTurtleAccess.result(false, EFailureReason.NO_ATTACK_TARGET);
		
	}
 
}
