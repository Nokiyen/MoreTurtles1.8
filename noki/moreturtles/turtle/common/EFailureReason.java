package noki.moreturtles.turtle.common;


/**********
 * @class EFailureReason
 *
 * @description タートルのコマンドが失敗した理由を列挙するenumです。
 * @description_en ENUM class to list the failure reasons of the turtle's commands.
 */
public enum EFailureReason {

	//******************************//
	// define enums.
	//******************************//
	UNDEFINED("undefined", "Unimplemented function"),
	NO_FUEL("no_fuel", "Out of fuel"),
	NO_ITEM("no_item", "No item to use"),
	NO_DIG("no_dig", "No tool to dig with"),
	NO_DIG_TARGET("no_dig_TARGET", "Nothing to dig here"),
	NO_DIG_TOOL("no_dig_tool", "Nothing to dig with"),
	NO_ATTACK("no_attack", "No tool to attack with"),
	NO_ATTACK_TARGET("no_attack_target", "Nothing to attack here"),
	NO_ATTACK_TOOL("no_attack_tool", "Nothing to attack with"),
	UNKNOWN("unknown", "Failed for unknown reasons"),
	
	NO_WATER("no_water", "No Water"),
	NO_CAST("no_cast", "Haven't casted yet"),
	WRONG_DIR("wrong_dir", "Wrong direction"),
	NO_FISH("no_fish", "Failed to fish"),
	
	NO_MAID("no_maid", "Not your Maid"),
	NO_SUCK("no_suck", "No items to suck"),
	NO_SPACE("no_space", "No space for items"),
	NO_DROP("no_drop", "No items to drop"),
	FULL("full", "Inventory full"),
	
	NO_TRAVEL("no_travel", "Can't travel from here"),
	NO_DIM("no_dim", "The dimension doesn't exist"),
	
	LUA_WRONG_ARG("lua_wrong_arg", "wrong arguments"),
	JAVA("java", "Java exception thrown");
	
	
	//******************************//
	// define member variables.
	//******************************//
	private String name;
	private String message;

	
	//******************************//
	// define member methods.
	//******************************//
	private EFailureReason(String name, String message) {
		
		this.name = name;
		this.message = message;
		
	}
	
	public String getName() {
		
		return this.name;
		
	}
	
	public String getMessage() {
		
		return this.message;
		
	}

}
