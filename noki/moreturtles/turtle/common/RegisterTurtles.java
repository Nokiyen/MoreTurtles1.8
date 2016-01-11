package noki.moreturtles.turtle.common;

import noki.moreturtles.MoreTurtlesData;
import noki.moreturtles.turtle.peripheral.*;
import noki.moreturtles.turtle.tool.*;
import dan200.computercraft.api.ComputerCraftAPI;


/**********
 * @class RegisterTurtles
 *
 * @description 追加タートルを登録するクラスです。
 * @description_en Registers added turtles.
 */
public class RegisterTurtles {
	
	//******************************//
	// define member variables.
	//******************************//


	//******************************//
	// define member methods.
	//******************************//
	public static void register() {
		
		//--Register Tool-Type Turtles.//
		//	unique turtles.
		ComputerCraftAPI.registerTurtleUpgrade(new ToolLiquid());
		ComputerCraftAPI.registerTurtleUpgrade(new ToolShearing());
		ComputerCraftAPI.registerTurtleUpgrade(new ToolFeeding());
		ComputerCraftAPI.registerTurtleUpgrade(new ToolMilking());
		ComputerCraftAPI.registerTurtleUpgrade(new ToolFishery());
		
		//dependent on mods.
/*		if(MoreTurtlesData.dependency_Bamboo == true) {
	    	ComputerCraftAPI.registerTurtleUpgrade(new ToolTake());
		}*/
		if(MoreTurtlesData.dependency_Tofu == true) {
//			ComputerCraftAPI.registerTurtleUpgrade(new ToolTofu());
		}
		if(MoreTurtlesData.dependency_Maid == true) {
//			ComputerCraftAPI.registerTurtleUpgrade(new ToolMaid());
		}
		
		//--Register Peripheral-Type Turtles.//
		//	unique turtles.
		ComputerCraftAPI.registerTurtleUpgrade(new PeripheralSilkTouch());
		ComputerCraftAPI.registerTurtleUpgrade(new PeripheralFortune());
		ComputerCraftAPI.registerTurtleUpgrade(new PeripheralLooting());
		ComputerCraftAPI.registerTurtleUpgrade(new PeripheralLuck());
		ComputerCraftAPI.registerTurtleUpgrade(new PeripheralLight());
		ComputerCraftAPI.registerTurtleUpgrade(new PeripheralBiome());
		ComputerCraftAPI.registerTurtleUpgrade(new PeripheralDimensional());
		ComputerCraftAPI.registerTurtleUpgrade(new PeripheralProjectile());
		
		//	dependent on mods.
		if(MoreTurtlesData.dependency_Toho) {
//			ComputerCraftAPI.registerTurtleUpgrade(new PeripheralDanmaku());
		}
		
	}

}
