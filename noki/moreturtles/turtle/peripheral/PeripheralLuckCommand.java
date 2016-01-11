package noki.moreturtles.turtle.peripheral;

import java.util.Arrays;

import net.minecraft.util.EnumFacing;
import noki.moreturtles.turtle.common.EFailureReason;
import noki.moreturtles.turtle.common.MTTurtleAccess;
import noki.moreturtles.turtle.tool.ToolFishery;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleCommand;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleVerb;


/**********
 * @class PeripheralLuckCommand
 *
 * @description Luck Turtleのコマンドを定義するクラスです。
 * @description_en Class of the commands of Luck Turtle.
 */
public class PeripheralLuckCommand implements ITurtleCommand {
	
	//******************************//
	// define member variables.
	//******************************//
	private TurtleSide side;
	private EnumFacing dir;
	private TurtleVerb verb;
	private static String[] allowedTool = {
		"Fishery"
	};
	
	
	//******************************//
	// define member methods.
	//******************************//
	public PeripheralLuckCommand(TurtleSide side, TurtleVerb verb, EnumFacing dir) {
		
		this.side = side;
		this.dir = dir;
		this.verb = verb;
		
	}
	
	@Override
	public TurtleCommandResult execute(ITurtleAccess turtle) {
		
		MTTurtleAccess turtleAccess = new MTTurtleAccess(turtle);

		switch(this.verb) {
		case Dig:
			return this.dig(turtleAccess);
		case Attack:
			return this.attack(turtleAccess);
		default: 
			return MTTurtleAccess.result(false, EFailureReason.UNKNOWN);
		}
		
	}
	
	private TurtleCommandResult dig(MTTurtleAccess turtle) {
		
		//check the other side upgrade.
		if(!this.checkTool(turtle, this.side)) {
			return MTTurtleAccess.result(false, EFailureReason.NO_DIG_TOOL);
		}
		
		ToolFishery otherUpgrade = (ToolFishery)turtle.getOtherUpgrade(this.side);

		otherUpgrade.setLuckLevel(3);
		otherUpgrade.setAddedFuelLevel(1);
		TurtleCommandResult result =
				otherUpgrade.useTool(turtle.turtle, MTTurtleAccess.getOppositeSide(this.side), this.verb, this.dir);
		if(result.isSuccess()) {
			turtle.playAnimation(MTTurtleAccess.getOtherSwingAnimation(this.side));
		}
		otherUpgrade.setLuckLevel(0);
		otherUpgrade.setAddedFuelLevel(0);
		
		return result;
		
	}
	
	private TurtleCommandResult attack(MTTurtleAccess turtle) {
		
		//check the other side upgrade.
		if(!this.checkTool(turtle, this.side)) {
			return MTTurtleAccess.result(false, EFailureReason.NO_ATTACK_TOOL);
		}
		
		ToolFishery otherUpgrade = (ToolFishery)turtle.getOtherUpgrade(this.side);
		
		TurtleCommandResult result =
				otherUpgrade.useTool(turtle.turtle, MTTurtleAccess.getOppositeSide(this.side), this.verb, this.dir);
		if(result.isSuccess()) {
			turtle.playAnimation(MTTurtleAccess.getOtherSwingAnimation(this.side));
		}
		return result;
		
	}
	
	private boolean checkTool(MTTurtleAccess turtle, TurtleSide side) {
		
		if(turtle.getOtherUpgrade(side) == null) {
			return false;
		}
		
		return Arrays.asList(allowedTool).contains(turtle.getOtherUpgradeName(this.side));
		
	}
	
}
