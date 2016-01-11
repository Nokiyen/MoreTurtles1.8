package noki.moreturtles.turtle.tool;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
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
 * @class TurtleLiquid
 *
 * @description Liquid Turtleを定義するクラスです。
 * @description_en Class of Liquid Turtle.
 */
public class ToolLiquid implements ITurtleUpgrade {

	//******************************//
	// define member variables.
	//******************************//
	public int upgradeMeta = 1;
	public ItemStack upgradeItem = new ItemStack(RegisterItems.extendedItems, 1, this.upgradeMeta);
	
	
	//******************************//
	// define member methods.
	//******************************//
	@Override
	public ResourceLocation getUpgradeID() {
		
		return new ResourceLocation("moreturtles:liquid_turtle");
		
	}
	
	@Override
	public int getLegacyUpgradeID() {
		
		return MoreTurtlesData.liquidTurtleTID;
		
	}
	
	@Override
	public String getUnlocalisedAdjective() {
		
		return "Liquid";
		
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
		int currentSlot = turtle.getSelectedSlot();
		ItemStack currentItem = turtle.getSlotContents(currentSlot);
		
		if(currentItem == null) {
			return MTTurtleAccess.result(false, EFailureReason.NO_ITEM);
		}
		
		//	case of buckets.
		if(FluidContainerRegistry.isBucket(currentItem) && FluidContainerRegistry.isEmptyContainer(currentItem)) {
			int bucketVolume = FluidContainerRegistry.BUCKET_VOLUME;
			
			//	directly.
			Fluid fluid = FluidRegistry.lookupFluidForBlock(state.getBlock());
			if(state.getBlock() == Blocks.water || state.getBlock() == Blocks.flowing_water) {
				fluid = FluidRegistry.WATER;
			}
			if(state.getBlock() == Blocks.lava || state.getBlock() == Blocks.flowing_lava) {
				fluid = FluidRegistry.LAVA;
			}
			if(fluid != null && metadata == 0) {
				ItemStack filled = FluidContainerRegistry.fillFluidContainer(new FluidStack(fluid, bucketVolume), currentItem);
				if(filled != null) {
					turtle.consume(currentSlot);
					turtle.store(filled);
					turtle.world.setBlockToAir(new BlockPos(newX, newY, newZ));
					
					return MTTurtleAccess.result(true);
				}
			}
			
			//	to Tank.
			TileEntity tile = turtle.world.getTileEntity(pos);
			if(tile != null && tile instanceof IFluidHandler) {
				IFluidHandler tank = (IFluidHandler)tile;
				FluidTankInfo info = tank.getTankInfo(dir)[0];
				Fluid tankFluid = info.fluid.getFluid();
				
				FluidStack reFluidStack = tank.drain(dir, new FluidStack(tankFluid, bucketVolume), false);
				ItemStack filled = FluidContainerRegistry.fillFluidContainer(new FluidStack(tankFluid, bucketVolume), currentItem);
				if(reFluidStack.amount == bucketVolume && filled != null) {
					tank.drain(dir, new FluidStack(tankFluid, bucketVolume), true);
					turtle.consume(currentSlot);
					turtle.store(filled);
					
					return MTTurtleAccess.result(true);
				}
			}
		}
		
		//	case of glass bottle for water.
		if((state.getBlock() == Blocks.water || state.getBlock() == Blocks.flowing_water) && metadata == 0) {
			if(currentItem.getItem() == Items.glass_bottle) {
				turtle.consume(currentSlot);
				turtle.store(new ItemStack(Items.potionitem, 1, 0));
				
				return MTTurtleAccess.result(true);
			}
		}
		
		//	case of glass bottle for Cauldron.
		//	a player can't get water bucket by empty bucket from filled Cauldron.
		if(state.getBlock() == Blocks.cauldron) {
/*			if(currentItem.getItem() == Items.bucket && metadata == 3) {
				this.world.setBlockMetadataWithNotify(newX, newY, newZ, 0, 3);
				this.consume(turtle, currentSlot);
				this.store(turtle, new ItemStack(Items.water_bucket));
				
				return result(true);
			}*/
			if(currentItem.getItem() == Items.glass_bottle && metadata > 0) {
				turtle.world.setBlockState(pos, state.getBlock().getStateFromMeta(metadata-1));
				turtle.consume(currentSlot);
				turtle.store(new ItemStack(Items.potionitem, 1, 0));
				
				return MTTurtleAccess.result(true);
			}
		}
		
		return MTTurtleAccess.result(false, EFailureReason.NO_DIG_TARGET);
		
	}
	
	private TurtleCommandResult attack(MTTurtleAccess turtle, EnumFacing dir) {

		int newX = turtle.posX + dir.getFrontOffsetX();
		int newY = turtle.posY + dir.getFrontOffsetY();
		int newZ = turtle.posZ + dir.getFrontOffsetZ();
		BlockPos pos = new BlockPos(newX, newY, newZ);
		
		IBlockState state = turtle.world.getBlockState(pos);
		int metadata = state.getBlock().getMetaFromState(state);
		int currentSlot = turtle.getSelectedSlot();
		ItemStack currentItem = turtle.getSlotContents(currentSlot);
		
		if(currentItem == null) {
			return MTTurtleAccess.result(false, EFailureReason.NO_ITEM);
		}
		
		//	case of bucket.
		if(FluidContainerRegistry.isBucket(currentItem) && FluidContainerRegistry.isFilledContainer(currentItem)) {
			//	directly.
			ItemBucket currentBucket = (ItemBucket)currentItem.getItem();
			boolean flag = currentBucket.tryPlaceContainedLiquid(turtle.world, pos);			
			if(flag == true) {
				turtle.consume(currentSlot);
				turtle.store(new ItemStack(Items.bucket));
				
				return MTTurtleAccess.result(true);
			}
			
			//	to Tank.
			TileEntity tile = turtle.world.getTileEntity(pos);
			if(tile != null && tile instanceof IFluidHandler) {
				IFluidHandler tank = (IFluidHandler)tile;
				int amount = tank.fill(dir, FluidContainerRegistry.getFluidForFilledItem(currentItem), false);
				if(amount != 0) {
					tank.fill(dir, FluidContainerRegistry.getFluidForFilledItem(currentItem), true);
					turtle.consume(currentSlot);
					turtle.store(new ItemStack(Items.bucket));
					
					return MTTurtleAccess.result(true);
				}
			}
		}
		
		//	case of water bucket for Cauldron.
		//	a player can't fill fully filled Cauldron with water bucket.
		//	a player can't fill Cauldron with water bottle.
		if(state.getBlock() == Blocks.cauldron) {
			//	water bucket.
			if(currentItem.getItem() == Items.water_bucket && metadata < 3) {
				turtle.consume(currentSlot);
				turtle.store(new ItemStack(Items.bucket));
				turtle.world.setBlockState(pos, state.getBlock().getStateFromMeta(3));
				
				return MTTurtleAccess.result(true);
			}
			//	water bottle.
/*			if(currentItem.getItem() == Items.potionitem && currentItem.getItemDamage() == 0) {
				if(metadata < 3) {
					this.consume(turtle, currentSlot);
					this.store(turtle, new ItemStack(Items.glass_bottle));
					this.world.setBlockMetadataWithNotify(newX, newY, newZ, metadata+1, 3);
					
					return result(true);
				}
				if(metadata >=3) {
					this.consume(turtle, currentSlot);
					this.store(turtle, new ItemStack(Items.glass_bottle));
	
					return result(true);
				}
			}*/
		}
		
		return MTTurtleAccess.result(false, EFailureReason.NO_ATTACK_TARGET);
		
	}
	
}
