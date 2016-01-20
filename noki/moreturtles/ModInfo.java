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
	public static final String VERSION = "2.1.1";
	public static final String CHANNEL = ID;
//	public static final String DEPENDENCY = "after:ComputerCraft,BambooMod,TofuCraft,mod_LMM_littleMaidMob,mod_thKaguya";
	public static final String DEPENDENCY =
			"required-after:Forge@[1.8-11.14.0.1239,);after:ComputerCraft;after:TofuCraft;after:littleMaidMob;after:THKaguyaMod";
	public static final String MC_VERSIONS = "[1.8,1.8.9]";
	
	public static final String PROXY_LOCATION = "noki.moreturtles.proxy.";

}
