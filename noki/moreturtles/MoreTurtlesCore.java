package noki.moreturtles;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.Metadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
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
@Mod(modid = ModInfo.ID, name = ModInfo.NAME, version = ModInfo.VERSION,
	dependencies = ModInfo.DEPENDENCY, acceptedMinecraftVersions = ModInfo.MC_VERSIONS)
public class MoreTurtlesCore {
	
	//******************************//
	// define member variables.
	//******************************//
	@Instance(value = ModInfo.ID)
	public static MoreTurtlesCore instance;
	@Metadata
	public static ModMetadata metadata;	//	extract from mcmod.info file, not java internal coding.
	@SidedProxy(clientSide = ModInfo.PROXY_LOCATION + "ProxyClient", serverSide = ModInfo.PROXY_LOCATION + "ProxyServer")
	public static ProxyCommon proxy;
	
	public static VersionInfo versionInfo;
	
	
	//******************************//
	// define member methods.
	//******************************//
	@SuppressWarnings("deprecation")
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		MoreTurtlesData.getDataPre(event);
		RegisterItems.registerPre();
		RegisterBlocks.registerPre();
		
		versionInfo = new VersionInfo(metadata.modId.toLowerCase(), metadata.version, metadata.updateUrl);
		
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
		
		MinecraftForge.EVENT_BUS.register(versionInfo);
		
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
	}
	
	@EventHandler
	public void onServerStart(FMLServerStartingEvent event) {
		
		versionInfo.notifyUpdate(Side.SERVER);
		
	}
	
}
