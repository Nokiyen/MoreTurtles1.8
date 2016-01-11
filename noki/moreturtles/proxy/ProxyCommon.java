package noki.moreturtles.proxy;


/**********
 * @class ProxyCommon
 *
 * @description 共通proxyクラス。
 * @description_en Interface of proxy classes.
 */
public interface ProxyCommon {
	
	//******************************//
	// define member variables.
	//******************************//

	
	//******************************//
	// define member methods.
	//******************************//
	abstract void registerRenderers();
	abstract void registerItemJsonPre();
	abstract void registerBlockJsonPre();
	abstract void registerItemJson();
	abstract void registerBlockJson();
	abstract void registerSidedEvent();
	
}
