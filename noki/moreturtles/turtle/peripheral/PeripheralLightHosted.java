package noki.moreturtles.turtle.peripheral;

import java.util.Arrays;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import noki.moreturtles.others.HelperArgs;
import noki.moreturtles.turtle.common.EFailureReason;
import noki.moreturtles.turtle.common.MTTurtleAccess;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;


/**********
 * @class PeripheralLightHosted
 *
 * @description Light Detecting Turtleの周辺機器部分を定義するクラスです。
 * @description_en Class of the peripheral of Light Detecting Turtle.
 */
public class PeripheralLightHosted implements IPeripheral {
	
	//******************************//
	// define member variables.
	//******************************//
	private MTTurtleAccess turtle;
	@SuppressWarnings("unused")
	private TurtleSide side;
	
	private static String[] dirs = new String[] {"bottom", "top", "front", "back", "left", "right"};
	
	
	//******************************//
	// define member methods.
	//******************************//
	public PeripheralLightHosted(ITurtleAccess turtle, TurtleSide side) {
		
		this.turtle = new MTTurtleAccess(turtle);
		this.side = side;
		
	}
	
	@Override
	public String getType() {
		
		return "MoreTurtles";
		
	}
	
	@Override
	public String[] getMethodNames() {
		
		return new String[] {
				"getName",
				"detectLight",
				"detectSkyLight",
				"detectSkyLightRaw",
				"detectBlockLight", 
				"canSeeTheSky",
				"setLight"
		};
		
	}
	
	@Override
	public void attach(IComputerAccess computer) {
		
	}
	
	@Override
	public void detach(IComputerAccess computer) {
		
	}
	
	@Override
	public boolean equals(IPeripheral other) {
		
		return false;
		
	}
	
	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments)
			throws LuaException, InterruptedException {
		
		this.turtle.setTurtleInfo();
		
		switch(method) {
			case 0:
				return this.getName();
			case 1:
			case 2:
			case 3:
			case 4:
				return this.detectLight(method, arguments);				
			case 5:
				return this.canSeeTheSky(arguments);
			case 6:
				return this.setLight(arguments);
			default:
				return new Object[] {false, EFailureReason.UNKNOWN.getMessage()};
		}
		
	}
	
	private Object[] getName() {
		
		return new Object[] {"Light"};
		
	}
	
	private Object[] detectLight(int method, Object[] arguments) throws LuaException, InterruptedException {
		
		if(!HelperArgs.checkArguments(arguments, 1, new String[] {"string"})) {
			throw new LuaException(EFailureReason.LUA_WRONG_ARG.getMessage());
		}
		
		String dirName = HelperArgs.getString(arguments[0]);
		if(!Arrays.asList(dirs).contains(dirName)) {
			throw new LuaException(EFailureReason.LUA_WRONG_ARG.getMessage());
		}
		
		int dirNum = Arrays.asList(dirs).indexOf(dirName);
		EnumFacing dir = EnumFacing.DOWN;
		EnumFacing turtleDir = this.turtle.getDirection();
		switch(dirNum) {
		case 0:	//bottom.
			dir = EnumFacing.DOWN;
			break;
		case 1:	//top.
			dir = EnumFacing.UP;
			break;
		case 2:	//front.
			dir = turtleDir;
			break;
		case 3:	//back.
			dir = turtleDir.getOpposite();
			break;
		case 4:	//left.
			switch (turtleDir) {
				case NORTH:
					dir = EnumFacing.WEST;
					break;
				case SOUTH:
					dir = EnumFacing.EAST;
					break;
				case EAST:
					dir = EnumFacing.NORTH;
					break;
				case WEST:
					dir = EnumFacing.SOUTH;
					break;
				default:
			}
			break;
		case 5:	//right.
			switch (turtleDir) {
				case NORTH:
					dir = EnumFacing.WEST;
					break;
				case SOUTH:
					dir = EnumFacing.EAST;
					break;
				case EAST:
					dir = EnumFacing.NORTH;
					break;
				case WEST:
					dir = EnumFacing.SOUTH;
					break;
				default:
			}
			break;
		default:
			dir = EnumFacing.DOWN;
		}
		
		int newX = this.turtle.posX + dir.getFrontOffsetX();
		int newY = this.turtle.posY + dir.getFrontOffsetY();
		int newZ = this.turtle.posZ + dir.getFrontOffsetZ();
		BlockPos pos = new BlockPos(newX, newY, newZ);
		
		//	return the light level for each method.
		int lightLevel = 0;
		switch(method) {
		case 1:
			lightLevel = this.turtle.world.getLight(pos);
			break;
		case 2:
			lightLevel = this.turtle.world.getLightFor(EnumSkyBlock.SKY, pos);
			lightLevel -= this.turtle.world.getSkylightSubtracted();
			if(lightLevel < 0) {
				lightLevel = 0;
			}
			break;
		case 3:
			lightLevel = this.turtle.world.getLightFor(EnumSkyBlock.SKY, pos);
			break;
		case 4:
			lightLevel = this.turtle.world.getLightFor(EnumSkyBlock.BLOCK, pos);
			break;
		}
		
		return new Object[] {lightLevel};
		
	}
	
	private Object[] canSeeTheSky(Object[] arguments) throws LuaException, InterruptedException {
		
		boolean result  = this.turtle.world.canSeeSky(new BlockPos(this.turtle.posX, this.turtle.posY, this.turtle.posZ));
		
		return new Object[] {result};
		
	}
	
	private Object[] setLight(Object[] arguments) throws LuaException, InterruptedException {
		
		if(!HelperArgs.checkArguments(arguments, 1, new String[] {"number"})) {
			throw new LuaException(EFailureReason.LUA_WRONG_ARG.getMessage());
		}
		
		int lightLevel = (int)MathHelper.clamp_double(HelperArgs.getDouble(arguments[0]), 0.0D, 15.0D);
		this.turtle.world.setLightFor(EnumSkyBlock.BLOCK, this.turtle.getPosition(), lightLevel);
		
		return new Object[] {true};
		
	}
	
}
