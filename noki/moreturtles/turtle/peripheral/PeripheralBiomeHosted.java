package noki.moreturtles.turtle.peripheral;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.TempCategory;
import noki.moreturtles.turtle.common.EFailureReason;
import noki.moreturtles.turtle.common.MTTurtleAccess;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;


/**********
 * @class PeripheralBiomeHosted
 *
 * @description Biome Turtleの周辺機器部分を定義するクラスです。
 * @description_en Class of the peripheral of Biome Turtle.
 */
public class PeripheralBiomeHosted implements IPeripheral {
	
	//******************************//
	// define member variables.
	//******************************//
	private MTTurtleAccess turtle;
	@SuppressWarnings("unused")
	private TurtleSide side;

	
	//******************************//
	// define member methods.
	//******************************//
	public PeripheralBiomeHosted(ITurtleAccess turtle, TurtleSide side) {
		
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
				"getBiome",
				"getBiomeID",
				"getClimate",
				"getTemperature",
				"getStandardTemperature",
				"getRainfall",
				"isRaining",
				"isThundering"
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
			case 5:
			case 6:
				return this.getAboutBiome(method, arguments);
			case 7:
			case 8:
				return this.getWeather(method, arguments);
			default:
				return new Object[] {false, EFailureReason.UNKNOWN.getMessage()};
		}

	}
	
	private Object[] getName() {
		
		return new Object[] {"Biome"};
		
	}
	
	private Object[] getAboutBiome(int method, Object[] arguments) throws LuaException, InterruptedException {
		
		BiomeGenBase biome = this.turtle.world.getBiomeGenForCoords(this.turtle.getPosition());
		
		switch(method) {
		case 1:
			return new Object[] {biome.biomeName};
		case 2:
			return new Object[] {biome.biomeID};
		case 3:
			return new Object[] {this.getClimateString(biome.getTempCategory(), biome)};
		case 4:
			return new Object[] {biome.getFloatTemperature(this.turtle.getPosition())};
		case 5:
			return new Object[] {biome.temperature};
		case 6:
			return new Object[] {biome.rainfall};
		}
		
		return new Object[] {false, EFailureReason.UNKNOWN.getMessage()};
		
	}
	
	private String getClimateString(TempCategory category, BiomeGenBase biome) {
		
		switch(category) {
		case COLD:
			if(biome.temperature < 2.0F) {
				return "snowy";
			}
			return "cold";
		case MEDIUM:
			return "medium";
		case WARM:
			return "warm";
		case OCEAN:
		default:
			return "ocean";
		}
		
	}
	
	private Object[] getWeather(int method, Object[] arguments) throws LuaException, InterruptedException {
		
		switch(method) {
		case 7:
			return new Object[] {this.turtle.world.isRaining()};
		case 8:
			return new Object[] {this.turtle.world.isThundering()};
		}
		
		return new Object[] {false, EFailureReason.UNKNOWN.getMessage()};
		
	}
			
}
