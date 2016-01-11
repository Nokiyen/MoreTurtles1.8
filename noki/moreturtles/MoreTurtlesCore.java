package noki.moreturtles;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import noki.moreturtles.blocks.RegisterBlocks;
import noki.moreturtles.event.RegisterEvents;
import noki.moreturtles.items.RegisterItems;
import noki.moreturtles.proxy.ProxyCommon;
import noki.moreturtles.turtle.common.RegisterTurtles;


/**********
 * @ModName More Turtles
 * 
 * @description プレイヤーの代わりを務められるようなタートルを追加します。<br>
 * 各種Modとの連携も、積極的に行ないます。
 * @description_en Add turtles who work instead of players.<br>
 * Positively collaborate with other Mods.
 * 
 * @caution ここはコアファイルなので、原則、具体的な処理をしないよう気を付ける。
 * @caution_en Here is Core. So, don't write any too detail and complex code.
 */
@Mod(modid = ModInfo.ID, name = ModInfo.NAME, version = ModInfo.VERSION, dependencies = ModInfo.DEPENDENCY)
public class MoreTurtlesCore {
	
	//******************************//
	// define member variables.
	//******************************//
	@Instance(value = "MoreTurtles")
	public static MoreTurtlesCore instance;
	@SidedProxy(clientSide = ModInfo.PROXY_LOCATION + "ProxyClient", serverSide = ModInfo.PROXY_LOCATION + "ProxyServer")
	public static ProxyCommon proxy;

	
	//******************************//
	// define member methods.
	//******************************//
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		MoreTurtlesData.getDataPre(event);
		RegisterItems.registerPre();
		RegisterBlocks.registerPre();
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		
		MoreTurtlesData.getData();
		proxy.registerRenderers();
		RegisterItems.register();
		RegisterBlocks.register();
		RegisterTurtles.register();
		RegisterEvents.register();
		proxy.registerSidedEvent();
		
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
	}
	
}
