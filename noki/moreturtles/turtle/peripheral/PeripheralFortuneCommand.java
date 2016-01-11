package noki.moreturtles.turtle.peripheral;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import noki.moreturtles.MoreTurtlesData;
import noki.moreturtles.turtle.common.EFailureReason;
import noki.moreturtles.turtle.common.MTTurtleAccess;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleCommand;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;


/**********
 * @class PeripheralFortuneCommand
 *
 * @description Fortune Turtleのdig系コマンドを定義するクラスです。
 * @description_en Class of the dig-type commands of Fortune Turtle.
 */
public class PeripheralFortuneCommand implements ITurtleCommand {
	
	//******************************//
	// define member variables.
	//******************************//
	private TurtleSide side;
	private EnumFacing dir;
	private static String[] allowedTool = {
		MoreTurtlesData.cc_Axe,
		MoreTurtlesData.cc_Hoe,
		MoreTurtlesData.cc_Pickaxe,
		MoreTurtlesData.cc_Shovel
	};
	
	
	//******************************//
	// define member methods.
	//******************************//
	public PeripheralFortuneCommand(TurtleSide side, EnumFacing dir) {
		
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
		ItemStack item = turtle.getOtherUpgradeItem(this.side);
		
		//check whether the block is air
		if(turtle.world.isAirBlock(pos)) {
			return MTTurtleAccess.result(false, EFailureReason.NO_DIG_TARGET);
		}
		//check the block hardness.
		if(state.getBlock().getBlockHardness(turtle.world, pos) <= -1.0F) {
			return MTTurtleAccess.result(false, EFailureReason.NO_DIG_TARGET);
		}
		//check fuel level.
		if(turtle.consumeFuel(MoreTurtlesData.consumedFuelLevel) == false) {
			return MTTurtleAccess.result(false, EFailureReason.NO_FUEL);
		}
		
		//dig.
		if(turtle.canTurtleHarvest(item, state.getBlock(), pos)) {
			List<ItemStack> items = state.getBlock().getDrops(turtle.world, pos, state, 3);
			if(items != null) {
				for(ItemStack each : items) {
					turtle.store(each);
				}
			}
		}
		turtle.playAnimation(MTTurtleAccess.getOtherSwingAnimation(this.side));
		turtle.world.setBlockToAir(pos);
		turtle.world.playAuxSFX(2001, pos, Block.getStateId(state));

		return MTTurtleAccess.result(true);
		
	}
	
	private boolean checkTool(MTTurtleAccess turtle, TurtleSide side) {
		
		if(turtle.getOtherUpgrade(side) == null) {
			return false;
		}
		
		return Arrays.asList(allowedTool).contains(turtle.getOtherUpgradeName(side));
		
	}

}
