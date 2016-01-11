package noki.moreturtles.turtle.peripheral;

import net.minecraft.util.EnumFacing;
import noki.moreturtles.turtle.common.EFailureReason;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleVerb;


/**********
 * @class PeripheralLuckHosted
 *
 * @description Luck Turtleの周辺機器部分を定義するクラスです。
 * @description_en Class of the peripheral of Luck Turtle.
 */
public class PeripheralLuckHosted implements IPeripheral {
	
	//******************************//
	// define member variables.
	//******************************//
	private ITurtleAccess turtle;
	private TurtleSide side;

	
	//******************************//
	// define member methods.
	//******************************//
	public PeripheralLuckHosted(ITurtleAccess turtle, TurtleSide side) {
		
		this.turtle = turtle;
		this.side = side;
		
	}

	@Override
	public String getType() {
		
		return "MoreTurtles";
		
	}

	@Override
	public String[] getMethodNames() {
		
		return new String[] { "getName", "dig", "digUp", "digDown", "attack", "attackUp", "attackDown" };
		
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
				return this.tryCommand(context, TurtleVerb.Dig, this.turtle.getDirection());
			case 2:
				return this.tryCommand(context, TurtleVerb.Dig, EnumFacing.UP);
			case 3:
				return this.tryCommand(context, TurtleVerb.Dig, EnumFacing.DOWN);
			case 4:
				return this.tryCommand(context, TurtleVerb.Attack, this.turtle.getDirection());
			case 5:
				return this.tryCommand(context, TurtleVerb.Attack, EnumFacing.UP);
			case 6:
				return this.tryCommand(context, TurtleVerb.Attack, EnumFacing.DOWN);
			default:
				return new Object[] {false, EFailureReason.UNKNOWN};
		}

	}
	
	private Object[] getName(IComputerAccess computer, ILuaContext context, Object[] arguments) {
		
		return new Object[] {"Luck"};
		
	}
	
	private Object[] tryCommand(ILuaContext context, TurtleVerb verb, EnumFacing dir)
			throws LuaException, InterruptedException {
		
		return this.turtle.executeCommand(context, new PeripheralLuckCommand(this.side, verb, dir));
		
	}
		
}
