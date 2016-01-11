package noki.moreturtles.turtle.peripheral;

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
 * @class PeripheralDimensionalHosted
 *
 * @description Dimensional Turtleの周辺機器部分を定義するクラスです。
 * @description_en Class of the peripheral of Dimensional Turtle.
 */
public class PeripheralDimensionalHosted implements IPeripheral {
	
	//******************************//
	// define member variables.
	//******************************//
	private MTTurtleAccess turtle;
	@SuppressWarnings("unused")
	private TurtleSide side;

	
	//******************************//
	// define member methods.
	//******************************//
	public PeripheralDimensionalHosted(ITurtleAccess turtle, TurtleSide side) {
		
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
				"getDimension",
				"getDimensionID",
				"travel"
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
				return this.getDimension();
			case 2:
				return this.getDimensionID();
			case 3:
				return this.travel(arguments, context);
			default:
				return new Object[] {false, EFailureReason.UNKNOWN.getMessage()};
		}

	}
	
	private Object[] getName() {
		
		return new Object[] {"Dimensional"};
		
	}
	
	private Object[] getDimension() {
		
		return new Object[] {this.turtle.world.provider.getDimensionName()};
		
	}
	
	private Object[] getDimensionID() {
		
		return new Object[] {this.turtle.world.provider.getDimensionId()};
		
	}
	
	private Object[] travel(Object[] arguments, ILuaContext context) throws LuaException, InterruptedException {
		
		if(!HelperArgs.checkArguments(arguments, 1, new String[] {"number"})) {
			throw new LuaException(EFailureReason.LUA_WRONG_ARG.getMessage());
		}		
		int dimensionID = (int)Math.round(HelperArgs.getDouble(arguments[0]));
		
		return this.turtle.executeCommand(context, new PeripheralDimensionalCommand(this.turtle, dimensionID));
		
	}

}
