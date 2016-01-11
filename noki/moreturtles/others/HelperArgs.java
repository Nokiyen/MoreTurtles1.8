package noki.moreturtles.others;


/**********
 * @class HelperArgs
 *
 * @description 周辺機器のメソッドに渡される引数を処理するヘルパークラスです。
 * @description_en Helper Class for the arguments given through a peripheral.
 */
public class HelperArgs {
	
	//******************************//
	// define member variables.
	//******************************//


	//******************************//
	// define member methods.
	//******************************//
	//	check arguments' count and each argument's type 
	public static boolean checkArguments(Object[] arguments, int num, String[] types) {
		
		if(arguments.length < num) {
			return false;
		}
		for(int i=0; i < types.length; i++) {
			String type = types[i];
			Object target = arguments[i];
			if(isType(type, target) == false) {
				return false;
			}
		}		
		return true;
		
	}

	//	check argument's type.
	public static boolean isType(String type, Object argument) {
		
		if(argument == null) {
			return false;
		}
		else if(type == "string" && argument instanceof String) {
			return true;
		}
		else if(type == "number" && argument instanceof Double) {
			return true;
		}
		else if(type == "booelan" && argument instanceof Boolean) {
			return true;
		}
		else {
			return false;
		}
		
	}
	
	//	get string value from object.
	public static String getString(Object object) {
		
		return (String)object;
		
	}
	
	//	get double value from object.
	public static Double getDouble(Object object) {
		
		return ((Double)object).doubleValue();
		
	}
	
	//	get boolean value from object.
	public static Boolean getBoolean(Object object) {
		
		return (Boolean)object;
		
	}

}
