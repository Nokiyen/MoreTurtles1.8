package noki.moreturtles;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import noki.moreturtles.others.TabMoreTurtles;


/**********
 * @class MoreTurtlesData
 *
 * @description このModで使用するstatic値を格納します。<br>
 * 連携Modのアイテム等も格納します。<br>
 * MoreTurtlesのBlock及びItemのインスタンスは、各々のRegisterクラスに格納されています。
 * @description_en Contains this Mod's static values, <br>
 * and also the instances of other Mod's items etc.<br>
 * The instances of Blocks and Items of this Mod are contained in each register class.
 */
public class MoreTurtlesData {
	
	//******************************//
	// define member variables.
	//******************************//
	//--Creative Tabs.---------//
	public static CreativeTabs tabMoreTurtles;
	
	//--Render ID.-------------// (given from Proxy)
	public static int renderID_extendedBlocks;
/*	public static int renderID_lightDetector;
	public static int renderID_biomeDetector;*/

	//--Configs.---------------//
	//	vanila tool turtle's ID.
	public static int liquidTurtleTID;
	public static int shearsTurtleTID;
	public static int feedingTurtleTID;
	public static int milkingTurtleTID;
	public static int fisheryTurtleTID;
	//	co-mod tool turtle's ID.
	public static int takeTurtleTID;
	public static int tofuTurtleTID;
	public static int maidTurtleTID;
	//	vanila peripheral turtle's ID.
	public static int silkTouchTurtlePID;
	public static int fortuneTurtlePID;
	public static int lootingTurtlePID;
	public static int luckTurtlePID;
	public static int lightTurtlePID;
	public static int biomeTurtlePID;
	public static int dimensionalTurtlePID;
	public static int projectileTurtlePID;
	//	co-mod peripheral turtle's ID.
	public static int danmakuTurtlePID;	
	//	fuel consumption.
	public static boolean consumeFuel;
	public static int consumedFuelLevel;
	
	//--Other Mods Data.-------//
	//	Mod dependency.
	public static String modName_CC = "ComputerCraft";
//	public static String modName_Bamboo = "BambooMod";
	public static String modName_Tofu = "TofuCraft";
	public static String modName_Maid = "littleMaidMob";
	public static String modName_Toho = "THKaguyaMod";
	public static boolean dependency_CC = false;
//	public static boolean dependency_Bamboo = false;
	public static boolean dependency_Tofu = false;
	public static boolean dependency_Maid = false;
	public static boolean dependency_Toho = false;	
	//	ComputerCraft.
	public static Item cc_disk;
	public static Block cc_turtle;
	public static Block cc_turtleExpanded;
	public static String cc_Pickaxe = "upgrade.minecraft:diamond_pickaxe.adjective";
	public static String cc_Shovel = "upgrade.minecraft:diamond_shovel.adjective";
	public static String cc_Axe = "upgrade.minecraft:diamond_axe.adjective";
	public static String cc_Hoe = "upgrade.minecraft:diamond_hoe.adjective";
	public static String cc_Sword = "upgrade.minecraft:diamond_sword.adjective";
	//	BambooMod.
/*	public static Block bamboo_bambooB;
	public static Item bamboo_bambooI;
	public static Item bamboo_takenokoI;*/
	//	TofuCraft.
	public static Block tofu_kinuB;
	public static Block tofu_hellB;
	public static Block tofu_soymilkB;
	public static Block tofu_soymilkHellB;
	public static Block tofu_soysauceB;
	public static Block tofu_misoBarrelB;
	public static Item tofu_bucketSoymilkI;
	public static Item tofu_bucketSoymilkHellI;
	public static Item tofu_bucketSoysauceI;
	public static Item tofu_nigariI;
	public static Item tofu_tofuStickI;	
	//	thKaguya.
	public static Block toho_danmakuCraftingB;


	//******************************//
	// define member methods.
	//******************************//

	//--Methods for PreInitialization.//
	//	getDataPre()
	//	getMoreTUrtlesConf()
	//	getModDependencies()
	public static void getDataPre(FMLPreInitializationEvent event) {
		
		tabMoreTurtles = new TabMoreTurtles();
		getMoreTurtlesConf(event);
		getModDependencies();		
		
	}
	
	private static void getMoreTurtlesConf(FMLPreInitializationEvent event) {
		
		Property prop;
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
		cfg.load();
		
		//	tool type turtle ID.
		prop = cfg.get("upgrade", "liquidTurtleTID", 1001);
		liquidTurtleTID = prop.getInt();
		prop = cfg.get("upgrade", "shearsTurtleTID", 1002);
		shearsTurtleTID = prop.getInt();
		prop = cfg.get("upgrade", "feedingTurtleTID", 1003);
		feedingTurtleTID = prop.getInt();
		prop = cfg.get("upgrade", "milkingTurtleTID", 1004);
		milkingTurtleTID = prop.getInt();
		prop = cfg.get("upgrade", "fisheryTurtleTID", 1005);
		fisheryTurtleTID = prop.getInt();
		
		//	peripheral type turtle ID.
		prop = cfg.get("upgrade", "silkTouchTurtlePID", 1101);
		silkTouchTurtlePID = prop.getInt();
		prop = cfg.get("upgrade", "fortuneTurtlePID", 1102);
		fortuneTurtlePID = prop.getInt();
		prop = cfg.get("upgrade", "lootingTurtlePID", 1103);
		lootingTurtlePID = prop.getInt();
		prop = cfg.get("upgrade", "luckTurtlePID", 1104);
		luckTurtlePID = prop.getInt();
		prop = cfg.get("upgrade", "lightTurtlePID", 1105);
		lightTurtlePID = prop.getInt();
		prop = cfg.get("upgrade", "biomeTurtlePID", 1106);
		biomeTurtlePID = prop.getInt();
		prop = cfg.get("upgrade", "dimensionalTurtlePID", 1107);
		dimensionalTurtlePID = prop.getInt();
		prop = cfg.get("upgrade", "projectileTurtlePID", 1108);
		projectileTurtlePID = prop.getInt();

		//	mod-dependent turtle ID.
		prop = cfg.get("upgrade", "takeTurtleTID", 1201);
		takeTurtleTID = prop.getInt();
		prop = cfg.get("upgrade", "tofuTurtleTID", 1202);
		tofuTurtleTID = prop.getInt();
		prop = cfg.get("upgrade", "maidTurtleTID", 1203);
		maidTurtleTID = prop.getInt();
		prop = cfg.get("upgrade", "danmakuTurtlePID", 1301);
		danmakuTurtlePID = prop.getInt();
				
		//	fuel consumption conf.
		prop = cfg.get("fuel", "consumeFuel", true);
		consumeFuel = prop.getBoolean(true);
		prop = cfg.get("fuel", "consumedFuelLevel", 1);
		consumedFuelLevel = prop.getInt();
		if(consumeFuel == false) {
			consumedFuelLevel = 0;
		}
		
		cfg.save();
		
	}
	
	private static void getModDependencies() {
	
		if (Loader.isModLoaded(modName_CC)) {
			dependency_CC = true;
		}
/*		if (Loader.isModLoaded(modName_Bamboo)) {
			dependency_Bamboo = true;
		}*/
		if (Loader.isModLoaded(modName_Tofu)) {
			dependency_Tofu = true;
		}
		if (Loader.isModLoaded(modName_Maid)) {
			dependency_Maid = true;
		}
		if (Loader.isModLoaded(modName_Toho)) {
			dependency_Toho = true;
		}
	
	}

	
	//--Methods for Initialization.//
	//	getData()
	//	getDataCC()
	//	getDataBamboo()
	//	getDataTofu()
	//	getDataMaid()
	//	getDataToho()
	public static void getData() {
		
		getDataCC();
//		getDataBamboo();
		getDataTofu();
		getDataMaid();
		getDataToho();
				
	}
	
	private static void getDataCC() {
		
		if(dependency_CC) {
			cc_disk = GameRegistry.findItem(modName_CC, "disk");
			cc_turtle = GameRegistry.findBlock(modName_CC, "CC-Turtle");
			cc_turtleExpanded = GameRegistry.findBlock(modName_CC, "CC-TurtleExpanded");
		}
		
	}
	
/*	private static void getDataBamboo() {
		
		if(dependency_Bamboo) {
			bamboo_bambooB = GameRegistry.findBlock(modName_Bamboo, "bamboo");
			bamboo_bambooI = GameRegistry.findItem(modName_Bamboo, "itembamboo");
			bamboo_takenokoI = GameRegistry.findItem(modName_Bamboo, "bambooshoot");
		}
		
	}*/
	
	private static void getDataTofu() {
		
		if(dependency_Tofu) {
			tofu_kinuB = GameRegistry.findBlock(modName_Tofu, "blockTofuKinu");
			tofu_hellB = GameRegistry.findBlock(modName_Tofu, "blockTofuHell");
			tofu_soymilkB = GameRegistry.findBlock(modName_Tofu, "soymilkStill");
			tofu_soymilkHellB = GameRegistry.findBlock(modName_Tofu, "soymilkHellStill");
			tofu_soysauceB = GameRegistry.findBlock(modName_Tofu, "soySauceStill");
			tofu_misoBarrelB = GameRegistry.findBlock(modName_Tofu, "blockBarrelMiso");
			tofu_bucketSoymilkI = GameRegistry.findItem(modName_Tofu, "bucketSoymilk");
			tofu_bucketSoymilkHellI = GameRegistry.findItem(modName_Tofu, "bucketSoymilkHell");
			tofu_bucketSoysauceI = GameRegistry.findItem(modName_Tofu, "bucketSoySauce");
			tofu_nigariI = GameRegistry.findItem(modName_Tofu, "nigari");
			tofu_tofuStickI = GameRegistry.findItem(modName_Tofu, "tofuStick");
		}
		
	}
	
	private static void getDataMaid() {
	
		if(dependency_Maid) {
		}
	
	}
	
	private static void getDataToho() {
		
		if(dependency_Toho) {
			toho_danmakuCraftingB = GameRegistry.findBlock(modName_Toho, "Danmaku Crafting Table");
		}
		
	}	

}
