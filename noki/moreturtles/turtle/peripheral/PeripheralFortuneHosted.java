package noki.moreturtles.turtle.peripheral;

import net.minecraft.util.EnumFacing;
import noki.moreturtles.turtle.common.EFailureReason;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;


/**********
 * @class PeripheralFortuneHosted
 *
 * @description Fortune Turtleの周辺機器部分を定義するクラスです。
 * @description_en Class of the peripheral of Fortune Turtle.
 */
public class PeripheralFortuneHosted implements IPeripheral {
	
	//******************************//
	// define member variables.
	//******************************//
	private ITurtleAccess turtle;
	private TurtleSide side;

	
	//******************************//
	// define member methods.
	//******************************//
	public PeripheralFortuneHosted(ITurtleAccess turtle, TurtleSide side) {
		
		this.turtle = turtle;
		this.side = side;
		
	}

	@Override
	public String getType() {
		
		return "MoreTurtles";
		
	}

	@Override
	public String[] getMethodNames() {
		
		return new String[] { "getName", "dig", "digUp", "digDown" };
		
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

		switch(method) {
			case 0:
				return this.getName(computer, context, arguments);
			case 1:
				return this.tryCommand(context, this.turtle.getDirection());
			case 2:
				return this.tryCommand(context, EnumFacing.UP);
			case 3:
				return this.tryCommand(context, EnumFacing.DOWN);
			default:
				return new Object[] {false, EFailureReason.UNKNOWN.getMessage()};
		}
	}
	
	private Object[] getName(IComputerAccess computer, ILuaContext context, Object[] arguments) {
		
		return new Object[] {"Fortune"};
		
	}
		
	private Object[] tryCommand(ILuaContext context, EnumFacing dir)
			throws LuaException, InterruptedException {
		
		return this.turtle.executeCommand(context, new PeripheralFortuneCommand(this.side, dir));
		
	}
	
}
