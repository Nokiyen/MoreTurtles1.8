package noki.moreturtles.turtle.peripheral;

import net.minecraft.util.MathHelper;
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
 * @class PeripheralDanmakuHosted
 *
 * @description Danmaku Turtleの実際の動作などを定義するクラスです。
 */
public class PeripheralProjectileHosted implements IPeripheral {
	
	//******************************//
	// define member variables.
	//******************************//
	private MTTurtleAccess turtle;
	@SuppressWarnings("unused")
	private TurtleSide side;
	
	private int dir = 0;
	private float yaw = 0;
	private float pitch = 0;
	
	private boolean setCharge = false;
	private float charge;
		
	private final int[] dirSet = {0, 2, 3, 1};
		
	
	//******************************//
	// define member methods.
	//******************************//
	//----------//
	// default turtle's methods.
	//----------//
	public PeripheralProjectileHosted(ITurtleAccess turtle, TurtleSide side) {
		
		this.turtle = new MTTurtleAccess(turtle);
		this.side = side;
		
	}

	@Override
	public String getType() {
		
		return "MoreTurtles";
		
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
	public String[] getMethodNames() {
		
		return new String[] {
				"getName",
				"shoot",
				"setAngle",
				"setCharge",
				"unsetCharge"
		};
		
	}
	
	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments)
			throws LuaException, InterruptedException {
		
		this.turtle.setTurtleInfo();
		this.dir = this.dirSet[this.turtle.getDirection().getIndex()-2];
		this.applyAngle();

		switch(method) {
			case 0:
				return this.getName();
			case 1:
				return this.shoot(context);
			case 2:
				return this.setAngle(arguments);
			case 3:
				return this.setCharge(arguments);
			case 4:
				return this.unsetCharge();
			default:
				return new Object[] {false, EFailureReason.UNKNOWN.getMessage()};
		}

	}
	//----------//
	// end
	//----------//
	
	//----------//
	// unique lua methods for Danmaku Turtle.
	//----------//
	private Object[] getName() {
		
		return new Object[] {"Projetile"};
		
	}

	private Object[] shoot(ILuaContext context) throws LuaException, InterruptedException {
		
		return this.turtle.executeCommand(context, new PeripheralProjectileCommand(this.turtle, this.setCharge, this.charge));
		
	}
	
	private Object[] setCharge(Object[] arguments) throws LuaException {
		
		if(!HelperArgs.checkArguments(arguments, 1, new String[] {"number"})) {
			throw new LuaException(EFailureReason.LUA_WRONG_ARG.getMessage());
		}
		
		float charge = HelperArgs.getDouble(arguments[0]).floatValue();
		if(charge < 0 || 1 < charge) {
			throw new LuaException(EFailureReason.LUA_WRONG_ARG.getMessage());
		}
		
		this.setCharge = true;
		this.charge = charge;
		
		return new Object[] {null};
		
	}
	
	private Object[] unsetCharge() throws LuaException {
		
		this.setCharge = false;
		this.charge = 0.0F;
		
		return new Object[] {null};
		
	}
	
	private Object[] setAngle(Object[] arguments) throws LuaException {
		
		if(!HelperArgs.checkArguments(arguments, 2, new String[] {"number", "number"})) {
			throw new LuaException(EFailureReason.LUA_WRONG_ARG.getMessage());
		}
		
		this.yaw = MathHelper.clamp_float(HelperArgs.getDouble(arguments[0]).floatValue(), -90, 90);
		this.pitch = MathHelper.clamp_float(HelperArgs.getDouble(arguments[1]).floatValue(), -90, 90);
		
		return new Object[] {null};
		
	}
	//----------//
	// end
	//----------//
	
	//----------//
	//sub methods
	//----------//
	private void applyAngle() {
		
		this.turtle.playerTurtle.rotationYaw = MathHelper.wrapAngleTo180_float(this.yaw + (this.dir-2)*90);
		this.turtle.playerTurtle.rotationPitch = this.pitch;
		
	}
		
}
