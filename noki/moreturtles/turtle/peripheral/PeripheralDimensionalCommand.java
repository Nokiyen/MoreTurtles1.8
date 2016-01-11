package noki.moreturtles.turtle.peripheral;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldServer;
import noki.moreturtles.MoreTurtlesData;
import noki.moreturtles.turtle.common.EFailureReason;
import noki.moreturtles.turtle.common.MTTurtleAccess;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleCommand;
import dan200.computercraft.api.turtle.TurtleCommandResult;


/**********
 * @class PeripheralDimensionalCommand
 *
 * @description Dimensional Turtleのマンドを定義するクラスです。
 * @description_en Class of the command of Dimensional Turtle.
 */
public class PeripheralDimensionalCommand implements ITurtleCommand {
	
	//******************************//
	// define member variables.
	//******************************//
	public MTTurtleAccess turtle;
	public int dimensionID;
	
	
	//******************************//
	// define member methods.
	//******************************//
	public PeripheralDimensionalCommand(MTTurtleAccess turtle, int dimensionID) {
		
		this.turtle = turtle;
		this.dimensionID = dimensionID;
		
	}

	@Override
	public TurtleCommandResult execute(ITurtleAccess givenTurtle) {
		
		//check portal block.
		int newX = 0;
		int newY = 0;
		int newZ = 0;
		IBlockState state = null;
		String className = null;
		boolean flag1 = false;
		for(int i=0; i<6; i++) {
			newX = this.turtle.posX + EnumFacing.getFront(i).getFrontOffsetX();
			newY = this.turtle.posY + EnumFacing.getFront(i).getFrontOffsetY();
			newZ = this.turtle.posZ + EnumFacing.getFront(i).getFrontOffsetZ();
			state = this.turtle.world.getBlockState(new BlockPos(newX, newY, newZ));
			className = state.getBlock().getClass().getSimpleName();
			
			if(state.getBlock().getMaterial() == Material.portal
					|| className.indexOf("portal") != -1 || className.indexOf("Portal") != -1) {
				flag1 = true;
				break;
			}
		}
		if(flag1 == false) {
			return MTTurtleAccess.result(false, EFailureReason.NO_TRAVEL);
		}
		
		//check destination.
		if(this.turtle.world.provider.getDimensionId() == this.dimensionID) {
			return MTTurtleAccess.result(false, EFailureReason.NO_TRAVEL);
		}
		MinecraftServer minecraftServer = MinecraftServer.getServer();
		WorldServer currentWorld;
		WorldServer nextWorld;
		try {
			currentWorld = minecraftServer.worldServerForDimension(this.turtle.world.provider.getDimensionId());
			nextWorld = minecraftServer.worldServerForDimension(this.dimensionID);
		}
		catch (Exception e){
			return MTTurtleAccess.result(false, EFailureReason.NO_DIM);
		}
		if(currentWorld == null || nextWorld == null) {
			return MTTurtleAccess.result(false, EFailureReason.NO_DIM);
		}
		
		int nextWorldX = 0;
		int nextWorldY = 0;
		int nextWorldZ = 0;
		
		if(state.getBlock() == Blocks.end_portal) {
			BlockPos coords;
			if(this.turtle.world.provider.getDimensionId() == 0 && this.dimensionID == 1) {
				coords = nextWorld.getSpawnPoint();
				nextWorldX = coords.getX();
				nextWorldY = coords.getY()+2;
				nextWorldZ = coords.getZ();
			}
			else if(this.turtle.world.provider.getDimensionId() == 1 && this.dimensionID == 0){
				coords = nextWorld.getSpawnPoint();
				nextWorldX = coords.getX();
				nextWorldY = coords.getY();
				nextWorldZ = coords.getZ();
				
				if(nextWorld.getBlockState(new BlockPos(nextWorldX, nextWorldY, nextWorldZ)).getBlock() != Blocks.air) {
					return MTTurtleAccess.result(false, EFailureReason.NO_TRAVEL);
				}
			}
			else {
				return MTTurtleAccess.result(false, EFailureReason.NO_TRAVEL);				
			}
		}
		else {
			double moveFactor = currentWorld.provider.getMovementFactor() / nextWorld.provider.getMovementFactor();
			int nextWorldStartX = MathHelper.floor_double(this.turtle.posX * moveFactor);
			int nextWorldStartZ = MathHelper.floor_double(this.turtle.posZ * moveFactor);
	
			boolean flag2 = false;
			search:
			for(int i = nextWorldStartX-128; i < nextWorldStartX + 128; ++i) {
				for(int j = nextWorldStartZ-128; j < nextWorldStartZ + 128; ++j) {
					for(int k = nextWorld.getActualHeight() - 1; k >= 0; --k) {
						if(nextWorld.getBlockState(new BlockPos(i, k, j)).getBlock() == state.getBlock()) {
							nextWorldX = i;
							nextWorldZ = j;
							nextWorldY = k;
							while(nextWorld.getBlockState(new BlockPos(i, k-1, j)).getBlock() == state.getBlock()) {
								--nextWorldY;
								--k;
							}
							flag2 = true;
							break search;
						}
					}
				}
			}
			if(flag2 == false) {
				return MTTurtleAccess.result(false, EFailureReason.NO_TRAVEL);
			}
			
			boolean flag3 = false;
			int checkX = 0;
			int checkY = 0;
			int checkZ = 0;
			IBlockState checkState;
			for(int i=0; i<6; i++) {
				checkX = nextWorldX + EnumFacing.getFront(i).getFrontOffsetX();
				checkY = nextWorldY + EnumFacing.getFront(i).getFrontOffsetY();
				checkZ = nextWorldZ + EnumFacing.getFront(i).getFrontOffsetZ();
				checkState = nextWorld.getBlockState(new BlockPos(checkX, checkY, checkZ));
				if(checkState.getBlock() == Blocks.air) {
					nextWorldX = nextWorldX + EnumFacing.getFront(i).getFrontOffsetX();
					nextWorldY = nextWorldY + EnumFacing.getFront(i).getFrontOffsetY();
					nextWorldZ = nextWorldZ + EnumFacing.getFront(i).getFrontOffsetZ();
					flag3 = true;
					break;
				}
			}
			if(flag3 == false) {
				return MTTurtleAccess.result(false, EFailureReason.NO_TRAVEL);
			}
		}
		
		if(this.turtle.consumeFuel(MoreTurtlesData.consumedFuelLevel*10) == false) {
			return MTTurtleAccess.result(false, EFailureReason.NO_FUEL);
		}

		this.turtle.teleportTo(nextWorld, new BlockPos(nextWorldX, nextWorldY, nextWorldZ));
		
		return MTTurtleAccess.result(true);
		
	}

}
