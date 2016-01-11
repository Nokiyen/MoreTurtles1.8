package noki.moreturtles;


/**********
 * @class ModInfo
 *
 * @description このModのごく基本的な情報を格納します。
 * @description_en Contains the Mod's basic information.
 */
public class ModInfo {

	//*****
	//define member variables.
	//*****	
	public static final String ID = "MoreTurtles";
	public static final String NAME = "More Turtles";
	public static final String VERSION = "2.1.0";
	public static final String CHANNEL = ID;
//	public static final String DEPENDENCY = "after:ComputerCraft,BambooMod,TofuCraft,mod_LMM_littleMaidMob,mod_thKaguya";
	public static final String DEPENDENCY = "after:ComputerCraft,TofuCraft,littleMaidMob,THKaguyaMod";
	
	public static final String PROXY_LOCATION = "noki.moreturtles.proxy.";

}
