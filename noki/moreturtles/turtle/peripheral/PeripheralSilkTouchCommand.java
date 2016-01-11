package noki.moreturtles.turtle.peripheral;

import java.util.Arrays;
import java.util.List;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleCommand;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.IShearable;
import noki.moreturtles.MoreTurtlesData;
import noki.moreturtles.turtle.common.EFailureReason;
import noki.moreturtles.turtle.common.MTTurtleAccess;


/**********
 * @class PeripheralSilkTouchCommand
 *
 * @description SilkTouch Turtleのdig系コマンドを定義するクラスです。
 * @description_en Class of the dig-type commands of SilkTouch Turtle.
 */
public class PeripheralSilkTouchCommand implements ITurtleCommand{
	
	//******************************//
	// define member variables.
	//******************************//
	private TurtleSide side;
	private EnumFacing dir;
	private static String[] allowedTool = {
		MoreTurtlesData.cc_Axe,
		MoreTurtlesData.cc_Hoe,
		MoreTurtlesData.cc_Pickaxe,
		MoreTurtlesData.cc_Shovel,
		"Shearing",
		"Tofu"
	};


	//******************************//
	// define member methods.
	//******************************//	
	public PeripheralSilkTouchCommand(TurtleSide side, EnumFacing dir) {
		
		this.side = side;
		this.dir = dir;
		
	}

	@Override
	public TurtleCommandResult execute(ITurtleAccess givenTurtle) {
		
		MTTurtleAccess turtle = new MTTurtleAccess(givenTurtle);
		
		//check the other side upgrade.
		if(!this.checkTool(turtle, this.side)) {
			return MTTurtleAccess.result(false, EFailureReason.NO_DIG_TOOL);
		}
		
		//try to dig.
		//set startup info.
		int newX = turtle.posX + this.dir.getFrontOffsetX();
		int newY = turtle.posY + this.dir.getFrontOffsetY();
		int newZ = turtle.posZ + this.dir.getFrontOffsetZ();
		BlockPos pos = new BlockPos(newX, newY, newZ);
		
		IBlockState state = turtle.world.getBlockState(pos);
		int metadata = state.getBlock().getMetaFromState(state);
		ItemStack item = turtle.getOtherUpgradeItem(this.side);
		String currentUpgradeName = turtle.getOtherUpgradeName(this.side);
		
		//check whether the block is air
		if(turtle.world.isAirBlock(pos)) {
			return MTTurtleAccess.result(false, EFailureReason.NO_DIG_TARGET);
		}			
		//check the block harness.
		if(state.getBlock().getBlockHardness(turtle.world, pos) <= -1.0F) {
			return MTTurtleAccess.result(false, EFailureReason.NO_DIG_TARGET);
		}
		
		//case of Tofu.
		if(currentUpgradeName == "Tofu") {
/*			//check whether it is tofu.
			if(block.getMaterial() != TcMaterial.tofu) {
				return MTTurtleAccess.result(false, EFailureReason.NO_DIG_TARGET);
			}
			//check fuel level.
			if(turtle.consumeFuel(MoreTurtlesData.consumedFuelLevel) == false) {
				return MTTurtleAccess.result(false, EFailureReason.NO_FUEL);
			}
			
			ItemStack droppedItem = new ItemStack(block, 1, metadata);
			if(droppedItem != null) {
				turtle.store(droppedItem);
			}
			turtle.playAnimation(MTTurtleAccess.getOtherSwingAnimation(this.side));
			turtle.world.setBlockToAir(newX, newY, newZ);
			turtle.world.playAuxSFX(2001, newX, newY, newZ, Block.getIdFromBlock(block) + metadata * 4096);*/
			
			return MTTurtleAccess.result(true);
		}
		else {
			//check weak upgrade.
			if(currentUpgradeName == "Shearing") {
				item = new ItemStack(Items.shears);
				if(!(state.getBlock() instanceof IShearable) &&
						item.getItem().getDigSpeed(item, state) <= 1.0F) {
					return MTTurtleAccess.result(false, EFailureReason.NO_DIG_TARGET);
					
				}
			}
			//check fuel level.
			if(turtle.consumeFuel(MoreTurtlesData.consumedFuelLevel) == false) {
				return MTTurtleAccess.result(false, EFailureReason.NO_FUEL);
			}
			
			if(turtle.canTurtleHarvest(item, state.getBlock(), pos)) {
				if(state.getBlock().canSilkHarvest(turtle.world, pos, state, turtle.playerTurtle)) {
					ItemStack droppedItem = new ItemStack(state.getBlock(), 1, metadata);
					if(droppedItem != null) {
						turtle.store(droppedItem);
					}
				}
				else if(currentUpgradeName == "Shearing" && state.getBlock() instanceof IShearable) {
					List<ItemStack> items = ((IShearable)state.getBlock()).onSheared(item, turtle.world, pos, 0);
					if(items != null) {
						for(ItemStack each : items) {
							turtle.store(each);
						}
					}
				}
				else {
					List<ItemStack> items = state.getBlock().getDrops(turtle.world, pos, state, 0);
					if(items != null) {
						for(ItemStack each : items) {
							turtle.store(each);
						}
					}
				}
			}
			turtle.playAnimation(MTTurtleAccess.getOtherSwingAnimation(this.side));
			turtle.world.setBlockToAir(pos);
			turtle.world.playAuxSFX(2001, pos, Block.getStateId(state));

			return MTTurtleAccess.result(true);
		}

	}
	
	public boolean checkTool(MTTurtleAccess turtle, TurtleSide side) {
		
		if(turtle.getOtherUpgrade(this.side) == null) {
			return false;
		}
		
		return Arrays.asList(allowedTool).contains(turtle.getOtherUpgradeName(this.side));
		
	}

}
